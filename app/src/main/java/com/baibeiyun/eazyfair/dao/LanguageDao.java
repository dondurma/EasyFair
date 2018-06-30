package com.baibeiyun.eazyfair.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baibeiyun.eazyfair.database.MyDataBaseHelper;
import com.baibeiyun.eazyfair.entities.Language;


public class LanguageDao {
	MyDataBaseHelper helper;
	Context context;

	public LanguageDao(Context context) {
		super();
		this.context = context;
		helper = new MyDataBaseHelper(context);
	}



	// 修改的方法
	public int updateData(Language language) {

		SQLiteDatabase db = helper.getWritableDatabase();
		db.isOpen();
		ContentValues values = new ContentValues();
		values.put("id", language.getId());
		values.put("tag", language.getTag());
		int row = db.update("switch_language", values, "id=?", new String[] { language.getId()});
		return row;
	}

	// 查询的方法
	public Cursor selectById(String id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "select * from switch_language where id=?";
		String[] selectionArgs = { id };
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		return cursor;

	}

}
