package com.app.runningtraining;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.math.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("unused")
@SuppressLint("DefaultLocale")
public class MainActivity extends Activity implements SensorEventListener {
	private SensorManager mgr;
    private Sensor accelerometer;
    private TextView text;
	private float[] gravity = new float[3];
	float[] motion = new float[3];
	private double ratio;
	private double mAngle;
	private int counter = 0;
	private Button button;
	private Button btnReset;
	private Button resultbtn;
	private TextView tview;
	//Timer
	private Time time;
	private TextView textTime ;
	long startTime;
	Handler handler = new Handler();
	long timeInMillies;
	long timeSwap;
	long finalTime;
	
	double X;
	double Y;
	double Z;
	int tempSecond = 0;

	private database db;
	
	//remove
	float dx = 0.0f;
	float vx = 0.0f;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mgr = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        text = (TextView) findViewById(R.id.tsensor);
        tview = (TextView) findViewById(R.id.view);
        textTime = (TextView)findViewById(R.id.vtime);
        btnReset = (Button) findViewById(R.id.reset);
        resultbtn = (Button) findViewById(R.id.result);
        btnReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finalTime = 0;
				tempSecond = 0;
				textTime.setText("00:00:00");
				tview.setText(" ");

			}
		});
         final Time time = new Time();
        button = (Button)findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startTime = SystemClock.uptimeMillis();
			    handler.postDelayed(updateTimerMethod, 0);
			}
		});
        db = new database(getApplicationContext());
        
        resultbtn.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				  
				  Intent intent = new Intent();
				  intent.setClass(MainActivity.this, view.class);
				  startActivity(intent);
			}
        	
        });
    }
    @Override
    protected void onResume() {
        mgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    	super.onResume();
    }
    @Override
    protected void onPause() {
        mgr.unregisterListener(this, accelerometer);
    	super.onPause();
    }
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	

	public void onSensorChanged(SensorEvent event) {
		for(int i=0; i<3; i++) {
            gravity [i] = (float) (0.1 * event.values[i] + 0.9 * gravity[i]);
            motion[i] = event.values[i] - gravity[i];
		}
    	ratio = gravity[1]/SensorManager.GRAVITY_EARTH;
    	if(ratio > 1.0) ratio = 1.0;
    	if(ratio < -1.0) ratio = -1.0;
    	mAngle = Math.toDegrees(Math.acos(ratio));
    	if(gravity[2] < 0) {
    		mAngle = -mAngle;
    	}
    	if(counter++ % 10 == 0) {
    		X = motion[0] - motion[0] % 0.01;
    		Y = motion[1] - motion[1] % 0.01;
    		Z = motion[2] - motion[2] % 0.01;
    		
    		//X = Math.abs(X);	
    		
    		String msg = String.format(
    			"Motion\nX: %8.4f\nY: %8.4f\nZ: %8.4f\n",
	           X, Y, Z
	            );
		    text.setText(msg);
		    text.invalidate();
		    counter=1;
		    	
		    }
    	}
	
	private Runnable updateTimerMethod = new Runnable() {

	  public void run() {
	   timeInMillies = SystemClock.uptimeMillis() - startTime;
	   finalTime = timeSwap + timeInMillies;

	   int seconds = (int) (finalTime / 1000);
	   int minutes = seconds / 60;
	   seconds = seconds % 60;
	   int milliseconds = (int) (finalTime % 1000);
	   double X1 = 0;
	   double X2 = 0;
	   float dx = 0.0f;
   	   float vx = 0.0f;
	   
	   textTime.setText("" + minutes + ":"
	     + String.format("%02d", seconds) + ":"
	     + String.format("%03d", milliseconds));
	   handler.postDelayed(this, 0);
	   if(motion[0] <= -5){ 					 //stop Timer
		   timeSwap += timeInMillies;
		    handler.removeCallbacks(updateTimerMethod);
	     }	
	   
	   if(tempSecond != seconds){
		   tempSecond = seconds;
		   if(X < 0){
		   	   //double _ms =  Math.sqrt(Math.abs(X)) * (X/Math.abs(X));// m/s^2 --> m/s
		   	   double _ms = X * seconds;
		   	    
		   	   X1 = X2;
		   	   X2 = X;
		   	   
			   
		   		   vx += (X1 + X2 ) / 2.0f*1;
		   		   dx += vx*1;	
		   		   
		   	 
		   	   double remove = dx / 0.1;
		   	   _ms -= _ms % 0.001;
		   	   remove -= remove % 0.001;
		   	   
		   	   
		   	   DecimalFormat df = new DecimalFormat("##.###");
		   	   String ms__ = df.format(_ms);
		   	   double __ms = Double.parseDouble(ms__);
		   	   String _distance = df.format(remove);
		   	   double __distance = Double.parseDouble(_distance);
		   	   String _X = df.format(X);
		   	   double X_ = Double.parseDouble(_X);
		   	   
		   	   
		   	   String tempStr = seconds + "s: " + X_ + "m/s^2	"+  __ms + "m/s	" +/* "distance:" + __distance  + */" \n";
		   	   tview.setText(tview.getText() + tempStr);
 
		   	   Calendar c=Calendar.getInstance();
		   	   String date = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);	
		   	   
		   	  // _ms = Math.abs(_ms);
		   	   
		   	   db.insert(seconds, X_ , __ms ,  __distance , date);
		   }else if(X >= 0){
			   //double _ms = Math.sqrt(X);
			   double _ms = X * seconds;

			   
			   X1 = X2;
			   X2 = X;
			   
			  
		   		   vx += (X1 + X2 ) / 2.0f*1;
		   		   dx += vx*1;	
		   		   
		   	  
		   	   double remove = dx / 0.1;
			   _ms -= _ms % 0.001;
			   remove -= remove % 0.001;
		   	   
			   DecimalFormat df = new DecimalFormat("##.###");
			   String ms_ = df.format(_ms);
			   double __ms = Double.parseDouble(ms_);
			   String distance = df.format(remove);
			   double _distance = Double.parseDouble(distance);
			   String _X = df.format(X);
			   double X_ = Double.parseDouble(_X);
			   
			   
			   	String tempStr = seconds + "s: " + X_ + "m/s^2      " + __ms + "m/s     " + /*"distance:" + _distance +*/" \n";
				tview.setText(tview.getText() + tempStr);

			   Calendar c=Calendar.getInstance();
			   String date = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
			   
			   BigDecimal bigD = new BigDecimal(_ms);
			   bigD.setScale(2,BigDecimal.ROUND_HALF_UP);
			   
			  // __ms = Math.abs(__ms);
			   
			   db.insert(seconds, X_ , __ms , _distance ,date);
			   
			   
			  
			   
		   }

	   }
	   /*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	  }
	  
	  
	 };

	 
}