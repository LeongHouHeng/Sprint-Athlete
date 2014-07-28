package com.app.runningtraining;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class database {
	
	private SQLiteDatabase db;
	
	public database(Context context){
		db = context.openOrCreateDatabase("a", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS acc (second INTEGER, acc DOUBLE, ms DOUBLE ,  remove DOUBLE ,date char(11))");
	}
	
	public void insert(int second, double acc, double ms ,  double remove ,String date){
		db.execSQL("INSERT INTO acc(second,acc,ms,remove,date) VALUES(" + second + "," + acc+ " ," + ms + ","+  remove + ",'"+date+"')");
	}
	
	public Cursor getAll(){
		return db.rawQuery("SELECT * FROM acc", null);
		
	}
	
	public void delete(){
		db.execSQL("DROP TABLE IF EXISTS acc");
	
	}

}
