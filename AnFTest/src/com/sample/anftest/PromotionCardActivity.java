package com.sample.anftest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PromotionCardActivity extends Activity implements OnClickListener{
	Promotion mPromotion;
	ImageView mImageView;
	TextView mTitle;
	TextView mDes;
	TextView mFooter;
	Button mButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promotioncard);
		Intent intent = getIntent();
		mPromotion = (Promotion) intent.getSerializableExtra("PromotionData");
	    mImageView = (ImageView) findViewById(R.id.promotion_image);
	    String imageName = mPromotion.getImage();
	    
	    if(imageName.lastIndexOf("/")!=-1)
	    	imageName = imageName.substring(imageName.lastIndexOf("/")+1, imageName.length());
	    
    
	    mImageView.setImageBitmap(BitmapCache.getInstance(this).get(imageName));
	    
	    mTitle = (TextView) findViewById(R.id.promotion_title);
	    mTitle.setText(mPromotion.getTitle());
	    
	    mDes = (TextView) findViewById(R.id.promotion_des);
	    mDes.setText(mPromotion.getDescription());
	    
	    mFooter = (TextView) findViewById(R.id.promotion_footer);
	    if(mPromotion.getFooter()!=null)
	    mFooter.setText(Html.fromHtml(mPromotion.getFooter()));
	    mFooter.setMovementMethod(LinkMovementMethod.getInstance());
	    
	    mButton = (Button) findViewById(R.id.promotion_button);
	    mButton.setText(mPromotion.getButton().mButtonTitle);
	    mButton.setOnClickListener(this);
    
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.promotion_button:
			Intent intent = new Intent(this, PromotionWebActivity.class);
			intent.putExtra("url",mPromotion.getButton().mButtonTarget );
			startActivity(intent);
			break;
		
		default:
			break;
		}
		
	}
	
}
