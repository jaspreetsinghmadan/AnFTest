package com.sample.anftest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapCache {

	
	
	Activity mActivity;
	private BitmapCache(Activity activity){
		mActivity = activity;
//		File file=new File(Environment.getExternalStorageDirectory()+"/.AnF_images");
//		if(!file.isDirectory()){
//			file.mkdir();
//		}
		 
	}
	private static BitmapCache instance;
	
	public static BitmapCache getInstance(Activity activity){
		if(instance == null)
			instance = new BitmapCache(activity);
		return instance;
	}
	
	private void writeBitmap(String filename,Bitmap bmp){
		try {
			File file = new File(mActivity.getExternalFilesDir(null),filename);
			FileOutputStream outputStream;
			outputStream = new FileOutputStream(file,true);
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			outputStream.write(bytes.toByteArray());
			outputStream.close();
			
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	private Bitmap readBitmap(String filename){
		File file = new File(mActivity.getExternalFilesDir(null) ,filename);
		if(file.exists())
		 return BitmapFactory.decodeFile(file.getAbsolutePath());
		else
			return null;
	      
	}
	
	void put(String key,Bitmap bmp){
		writeBitmap(key, bmp);
	} 
	
	Bitmap get(String key){
		return readBitmap(key);
	}
	
}
