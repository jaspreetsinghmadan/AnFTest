package com.sample.anftest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivityAdapter extends
		RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
	private static List<Promotion> mPromotionList;
	private static MainActivity mParentActivity;

	public MainActivityAdapter(List<Promotion> list, MainActivity activity) {
		mPromotionList = list;
		mParentActivity = activity;

	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements
			OnClickListener {
		public ImageView mImage;
		public TextView mTitle;

		public ViewHolder(View view) {
			super(view);
			this.mImage = (ImageView) view.findViewById(R.id.promotion_image);
			this.mTitle = (TextView) view.findViewById(R.id.promotion_title);
			view.setOnClickListener(this);
		}

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(mParentActivity,
					PromotionCardActivity.class);
			intent.putExtra("PromotionData",
					mPromotionList.get(getAdapterPosition()));
			mParentActivity.startActivity(intent);
		}

	}

	@Override
	public int getItemCount() {
		return (mPromotionList != null ? mPromotionList.size() : 0);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Promotion promotion = mPromotionList.get(position);
		Bitmap bmp = BitmapFactory.decodeResource(
				mParentActivity.getResources(), R.drawable.placeholder);
		bmp = decodeSampledBitmapFromResource(bmp,
				mParentActivity.mThumbnailHeight,
				mParentActivity.mThumbnailHeight);
		holder.mImage.setImageBitmap(bmp);
		loadBitmap(promotion.getImage(), holder.mImage);
		holder.mTitle.setText(promotion.getTitle());

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = LayoutInflater.from(arg0.getContext()).inflate(
				R.layout.promotion_list_item, arg0, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	public void loadBitmap(String imageUrl, ImageView imageView) {
		BitmapWorkerTask task = new BitmapWorkerTask(imageView);
		task.execute(imageUrl);
	}

	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		private String imageUrl;

		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			Bitmap bmp = null;
			URL url;
			try {
				String imagePath = null;
				if (imageUrl.lastIndexOf("/") != -1) {
					imagePath = imageUrl.substring(
							imageUrl.lastIndexOf("/") + 1, imageUrl.length());
				} else {
					imagePath = imageUrl;
				}

				bmp = BitmapCache.getInstance(mParentActivity).get(imagePath);
				if (bmp == null) {
					url = new URL(imageUrl);
					bmp = BitmapFactory.decodeStream(url.openConnection()
							.getInputStream());
					BitmapCache.getInstance(mParentActivity)
							.put(imagePath, bmp);
				}
				bmp = decodeSampledBitmapFromResource(bmp,
						mParentActivity.mThumbnailHeight,
						mParentActivity.mThumbnailHeight);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bmp;

		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	public static Bitmap decodeSampledBitmapFromResource(Bitmap bitmap,
			int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,
				options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
}
