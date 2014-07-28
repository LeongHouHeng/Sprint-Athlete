package com.app.runningtraining;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class view extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);
		final TextView tseconds = (TextView)findViewById(R.id.seconds);
		final TextView tacc = (TextView)findViewById(R.id.acc);
		final TextView tdate = (TextView)findViewById(R.id.date);
		final TextView tms = (TextView)findViewById(R.id.ms);
		final TextView rem = (TextView)findViewById(R.id.remove);
		
		Button resetbtn = (Button)findViewById(R.id.dbreset);
		Button backbtn = (Button)findViewById(R.id.back);
		 final database db = new database(getApplicationContext());
		
		Cursor dbCursor = db.getAll();
		if(dbCursor.getCount() > 0){
			dbCursor.moveToFirst();
			
			int i = 0;
			for(i = 0; i < dbCursor.getCount(); i ++){
				tseconds.setText(tseconds.getText() + "\n" + dbCursor.getString(0));
				tacc.setText(tacc.getText() + "\n"  + dbCursor.getString(1));//here need to change (m/s^2 -->m/s)
				tms.setText(tms.getText() + "\n" + dbCursor.getString(2));
				rem.setText(rem.getText() + "\n" + dbCursor.getString(3));
				tdate.setText(tdate.getText() + "\n" + dbCursor.getString(4));
				dbCursor.moveToNext();
				
			}
		}
		dbCursor.close();
		
		
		backbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(view.this, MainActivity.class);
				startActivity(intent);
			}
			
		});
		
		resetbtn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				tseconds.setText("");
				tacc.setText("");
				tdate.setText("");
				tms.setText("");
				rem.setText("");
				
				db.delete();
			}
		});	
		
		
	}

}
