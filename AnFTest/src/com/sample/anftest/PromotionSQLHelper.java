package com.sample.anftest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PromotionSQLHelper extends SQLiteOpenHelper	{

	 public static final String TABLE_PROMOTIONS = "promotions";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_BUTTON_TITLE = "button_title";
	  public static final String COLUMN_BUTTON_TARGET = "button_target";
	  public static final String COLUMN_TITLE = "title";
	  public static final String COLUMN_DESC = "desc";
	  public static final String COLUMN_FOOTER = "footer";
	  public static final String COLUMN_IMAGE = "image";

	  private static final String DATABASE_NAME = "commments.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_PROMOTIONS + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_BUTTON_TITLE
	      + " text, "+ COLUMN_BUTTON_TARGET+ " text, "+ COLUMN_TITLE
	      + " text, " + COLUMN_DESC + " text, "+ COLUMN_FOOTER
	      + " text, "+ COLUMN_IMAGE + " text );";

	  public  PromotionSQLHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(PromotionSQLHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTIONS);
	    onCreate(db);
	  }

}
