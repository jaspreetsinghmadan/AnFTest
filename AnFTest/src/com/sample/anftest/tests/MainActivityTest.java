package com.sample.anftest.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import com.sample.anftest.DataDownloadComplete;
import com.sample.anftest.MainActivity;
import com.sample.anftest.MainActivityAdapter;
import com.sample.anftest.PromotionCardActivity;
import com.sample.anftest.R;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> implements DataDownloadComplete {

	private MainActivity mMainActivityActivity;
	DataDownloadComplete mListener;
	final CountDownLatch signal = new CountDownLatch(1);
	public MainActivityTest() {
		super(MainActivity.class);
		mListener = this;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mMainActivityActivity = getActivity();

	}

	public void testSomeAsynTask() throws Throwable {

		signal.await(5, TimeUnit.SECONDS);
		RecyclerView recyclerview = (RecyclerView) mMainActivityActivity.findViewById(R.id.my_recycler_view);
		
		MainActivityAdapter adapter = (MainActivityAdapter) recyclerview.getAdapter();
		assertNotNull(adapter);
		assertEquals(2, adapter.getItemCount()) ;
		
		
		/*Intent intent = new Intent(mMainActivityActivity, PromotionCardActivity.class);
		intent.putExtra("PromotionData",
				mPromotionList.get(recyclerview.get));
		mMainActivityActivity.startActivity(intent);*/
		
		/* final Intent launchIntent = getStartedActivityIntent();
		    assertNotNull("Intent was null", launchIntent);
		    assertTrue(isFinishCalled());*/
		
	}

	@Override
	public void onTaskCompleted() {
		signal.countDown();
		
	}
}
