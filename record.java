import android.os.SystemClock;


		   seconds = seconds % 60;
		   int milliseconds = (int) (finalTime % 1000);
		   
	  public void run(){
	   textTime.setText("" + minutes + ":"
	     + String.format("%02d", seconds) + ":"
	     + String.format("%03d", milliseconds));
	   handler.postDelayed(this, 0);
	   if(motion[0] <= -1){
		   timeSwap += timeInMillies;
		    handler.removeCallbacks(updateTimerMethod);
		
	     }
	   
	  }
