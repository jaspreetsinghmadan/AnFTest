package com.sample.anftest;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PromotionDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private PromotionSQLHelper dbHelper;
	  private String[] allColumns = { PromotionSQLHelper.COLUMN_ID,
	      PromotionSQLHelper.COLUMN_BUTTON_TITLE,PromotionSQLHelper.COLUMN_BUTTON_TARGET,
	      PromotionSQLHelper.COLUMN_TITLE,PromotionSQLHelper.COLUMN_DESC,PromotionSQLHelper.COLUMN_FOOTER,
	      PromotionSQLHelper.COLUMN_IMAGE};

	  public PromotionDataSource(Context context) {
	    dbHelper = new PromotionSQLHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public Promotion createPromotion(Promotion promotion) {
	    ContentValues values = new ContentValues();
	    values.put(PromotionSQLHelper.COLUMN_BUTTON_TARGET, promotion.getButton().mButtonTarget);
	    values.put(PromotionSQLHelper.COLUMN_BUTTON_TITLE, promotion.getButton().mButtonTitle);
	    values.put(PromotionSQLHelper.COLUMN_DESC, promotion.getDescription());
	    values.put(PromotionSQLHelper.COLUMN_FOOTER, promotion.getFooter());
	    values.put(PromotionSQLHelper.COLUMN_IMAGE, promotion.getImage());
	    values.put(PromotionSQLHelper.COLUMN_TITLE, promotion.getTitle());
	    long insertId = database.insert(PromotionSQLHelper.TABLE_PROMOTIONS, null,
	        values);
	    Cursor cursor = database.query(PromotionSQLHelper.TABLE_PROMOTIONS,
	        allColumns, PromotionSQLHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Promotion newPromotion = cursorToPromotion(cursor);
	    cursor.close();
	    return newPromotion;
	  }

	  public void deletePromotion(Promotion promotion) {
	    long id = promotion.getId();
	    System.out.println("Promotion deleted with id: " + id);
	    database.delete(PromotionSQLHelper.TABLE_PROMOTIONS, PromotionSQLHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<Promotion> getAllPromotions() {
	    List<Promotion> promotionList = new ArrayList<Promotion>();

	    Cursor cursor = database.query(PromotionSQLHelper.TABLE_PROMOTIONS,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Promotion promotion = cursorToPromotion(cursor);
	      promotionList.add(promotion);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return promotionList;
	  }

	  private Promotion cursorToPromotion(Cursor cursor) {
	    Promotion promotion = new Promotion();
	    promotion.setId(cursor.getLong(0));
		Promotion.Button button = new Promotion.Button(
				 cursor.getString(2),
				cursor.getString(1));
		promotion.setButton(button);
	    promotion.setTitle(cursor.getString(3));
	    promotion.setDescription(cursor.getString(4));
	    promotion.setFooter(cursor.getString(5));
	    promotion.setImage(cursor.getString(6));		
	    return promotion;
	  }
}
