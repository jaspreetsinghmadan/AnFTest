package com.sample.anftest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements DataDownloadComplete{
	private static RecyclerView mRecyclerView;
	private static RecyclerView.Adapter<MainActivityAdapter.ViewHolder> mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	List<Promotion> mList;

	// self reference used in adapter
	private static MainActivity mSelf;

	// Thumbnail height used in adapter
	public int mThumbnailHeight;

	static PromotionDataSource mDataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Define Thumbnail height.
		mThumbnailHeight = 200;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		mSelf = this;
		mDataSource = new PromotionDataSource(this);
		mDataSource.open();
		List<Promotion> promotionList = mDataSource.getAllPromotions();
		if (NetworkManager.getInstance(this).isConnectingToInternet()) {

			// In case of network access database is cleaned
			int size = promotionList.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					mDataSource.deletePromotion(promotionList.get(i));
				}
			}

			// Exec async load task
			(new AsyncListViewLoader(this))
					.execute("http://www.abercrombie.com/anf/nativeapp/Feeds/promotions.json");
		} else {
			//In case of no network check if Data is present in DB
			if (promotionList.size() > 0) {
				mAdapter = new MainActivityAdapter(promotionList, mSelf);
				mRecyclerView.setAdapter(mAdapter);
			} else {
				//In case of first run or cache clean show alert
				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
				dlgAlert.setMessage("Please connect to internet, App requires to fetch data from internet on first run or after cache clean.");
				dlgAlert.setTitle("Information");
				dlgAlert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return super.onOptionsItemSelected(item);
	}

	
	//Asyn task to fetch data from web service and populate adapter
	public static class AsyncListViewLoader extends AsyncTask<String, Void, List<Promotion>> {
		private final ProgressDialog dialog = new ProgressDialog(
				mSelf);
		private DataDownloadComplete listener;
		public AsyncListViewLoader(DataDownloadComplete listener){
			this.listener = listener;
		}
		@Override
		protected void onPostExecute(List<Promotion> result) {
			super.onPostExecute(result);
			dialog.dismiss();
			mAdapter = new MainActivityAdapter(result, mSelf);
			mRecyclerView.setAdapter(mAdapter);
			listener.onTaskCompleted();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Downloading promotions...");
			dialog.show();
		}

		@Override
		protected List<Promotion> doInBackground(String... params) {
			if (android.os.Debug.isDebuggerConnected())
				android.os.Debug.waitForDebugger();
			List<Promotion> result = new ArrayList<Promotion>();

			try {
				URL u = new URL(params[0]);

				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setUseCaches(true);
				conn.setRequestMethod("GET");
				conn.addRequestProperty("Cache-Control", "max-stale="
						+ (60 * 60 * 24));
				conn.connect();
				InputStream is = conn.getInputStream();

				// Read the stream
				byte[] b = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				while (is.read(b) != -1)
					baos.write(b);

				String JSONResp = new String(baos.toByteArray());
				JSONObject resp = new JSONObject(JSONResp);
				JSONArray arr = resp.optJSONArray("promotions");
				for (int i = 0; i < arr.length(); i++) {
					Promotion promotion = convertPromotions(arr
							.getJSONObject(i));
					result.add(promotion);
					mDataSource.createPromotion(promotion);
				}

				// Add result to database for offline access

				return result;
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return result;
		}

		private Promotion convertPromotions(JSONObject obj)
				throws JSONException {
			Object buttonObj = obj.get("button");
			JSONObject jsonButtonObj = null;
			if (buttonObj instanceof JSONObject)
				jsonButtonObj = obj.getJSONObject("button");
			else if (buttonObj instanceof JSONArray) {
				JSONArray arr = obj.optJSONArray("button");
				jsonButtonObj = arr.getJSONObject(0);
			}

			Promotion.Button button = new Promotion.Button(
					jsonButtonObj.getString("target"),
					jsonButtonObj.getString("title"));

			String description = obj.getString("description");
			String footer = null;
			if (obj.has("footer"))
				footer = obj.getString("footer");

			String image = obj.getString("image");
			String title = obj.getString("title");

			return new Promotion(button, description, footer, image, title);
		}
	}


	@Override
	public void onTaskCompleted() {
		// TODO Auto-generated method stub
		
	}
}
