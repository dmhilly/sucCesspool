//CircleDisplayActivity displays information about the pool the user created
	//pool name, cycle length, money committed, and charity are saved into the Circle class after user inputs them in CreateCircleActivity
	//CircleDisplayActivity pulls this information from the Circle class and displays them on the "View Your Pool" page

package com.parse.starter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.GetCallback;
import com.parse.ParseException;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

	public class CircleDisplayActivity extends Activity
	{ 
		//displays the pool created by the current logged in user
		ParseUser currentUser = ParseUser.getCurrentUser();

		long timePassedInMillis;
			
		public void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_circle_display); //sets layout of the page to activity_circle_display.xml
			
			//learned how to connect front-end and back-end of displayed user input and buttons from Android Development Tutorial
			final TextView inputCircleName = (TextView)findViewById(R.id.inputCircleName);
			final TextView inputCycleLength = (TextView)findViewById(R.id.inputCycleLength);
			final TextView inputDollarsCommitted = (TextView)findViewById(R.id.inputMoneyCommitted);
			final TextView inputCharity = (TextView)findViewById(R.id.inputCharity);
			final TextView timeRemaining= (TextView) findViewById(R.id.timeRemaining);
		
		   
			
		    
		    //learned how to set up a ParseQuery from Parse Android tutorial
	    	ParseQuery<Circle> query = ParseQuery.getQuery("Circle");	    	
	    	query.whereEqualTo("userId", currentUser.getObjectId());
	    	query.whereEqualTo("archive", false);
	    	query.getFirstInBackground(new GetCallback<Circle>() 
	    	{

		    	public void done(Circle circle, ParseException e) 
		    	{
		    		if (e == null) 
		    		{
		    			//gets pool name, cycle length, money committed, and charity name from the Circle class
		    			String circleName = circle.getString("name");
	    	            int cycleLength = circle.getInt("cycleLength");
	    	            int dollarsCommitted = circle.getInt("dollars");
		    	        String charity = circle.getString("charity");
		    	        
		    	        //sets textview of relevant variables in activity_circle_display.xml to what was pulled from the Circle class
		    	     	inputCircleName.setText(circleName);
		    	     	inputCycleLength.setText("" + cycleLength);
		    	     	inputDollarsCommitted.setText("" + dollarsCommitted);
		    	     	inputCharity.setText(charity);		    		    
		    	     	
		    	     	//sets up a timer
		    	    
		    	     	final int millisecondsInCycle=cycleLength*24*60*60*1000;
				     	
		    	     	//declares integers that represent current time (time user opened circle display page)
		    	     	//and the launch time (time the circle was created)
		    	     	//launch time was saved in and then queried from Parse
		    	     	//Recall that launch time values were put into the cloud in Create Circle Activity
		    	     	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				     	int currentYear=Integer.parseInt(timeStamp.substring(0,4));
				     	int launchYearCopy=circle.getInt("launchYear");
				     	int currentMonth=Integer.parseInt(timeStamp.substring(4,6));
				     	int launchMonthCopy=circle.getInt("launchMonth");
				     	int currentDay=Integer.parseInt(timeStamp.substring(6,8));
				     	int launchDayCopy=circle.getInt("launchDay");
				     	long currentTime=Calendar.getInstance().getTimeInMillis();
				     	long launchTimeCopy=circle.getLong("launchTime");
				     	
				     	//CountDownTimer does not run when the application closes, so we must update the time until the cycle is over
				     	//each time the application is opened. This is achieved by using the data above to find the time passed
				     	//between the time the circle was created and the time the circle page is opened.
				     	if (currentYear==launchYearCopy&&currentMonth==launchMonthCopy&&launchDayCopy==currentDay){
				     		timePassedInMillis = currentTime - launchTimeCopy;
				     		
				     	}
				     	if(currentYear==launchYearCopy&&currentMonth==launchMonthCopy&&launchDayCopy<currentDay){
				     		timePassedInMillis = (currentDay-launchDayCopy)*24*3600*1000+(currentTime-launchTimeCopy);
				     		
				     	}
				     	if(currentYear==launchYearCopy&&launchMonthCopy<currentDay){
				     		timePassedInMillis=(currentMonth-launchMonthCopy)*30*24*3600*1000+(currentDay-launchDayCopy)*24*3600*1000+(currentTime-launchTimeCopy);
				     		
				     	}
				     	if(currentYear<launchYearCopy){
				     		timePassedInMillis=(currentYear-launchYearCopy)*365*24*3600*1000+(currentMonth-launchMonthCopy)*30*24*3600*1000+(currentDay-launchDayCopy)*24*3600*1000+(currentTime-launchTimeCopy);
				     		
				     	}
				     	
				     //sets up a timer
				       
				    	CountDownTimer aCounter = new CountDownTimer(millisecondsInCycle-timePassedInMillis, 1000) {
						    public void onTick(long millisUntilFinished) {
						    	// casts long as an int
						    	int millisUntilFinishedInt= (int) millisUntilFinished;
						    	//sets values up for the display
						    	int hours= millisUntilFinishedInt/3600000;
						    	int minutes= (millisUntilFinishedInt%3600000)/60000;
						    	int seconds= ((millisUntilFinishedInt%3600000)%60000)/1000;
						        timeRemaining.setText(hours+" hours "+minutes+" minutes "+seconds+" seconds");
						        
						   }
						    
						  public void onFinish() {
						     timeRemaining.setText("done!");
						     Intent intent = new Intent (CircleDisplayActivity.this, CircleExpired.class);
						     startActivity (intent);
						   }
						 };
				    	 aCounter.start();
				 
		    	     	//serviceIntent = new Intent(CircleDisplayActivity.this, MyService.class);

		    	     	//btnStart.setOnClickListener(new View.OnClickListener() {
		    	        //});
		    	    }
		    	}
		    	
	    	});
		    	
	
	//creates the "View Your Goals" button that links to the front-end layout for the button in activity_circle_display
    Button buttonViewGoals = (Button)findViewById(R.id.buttonSetGoals);
    //when button is clicked, sets up an intent that takes the user to GoalListActivity
    buttonViewGoals.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v)
	    	{
	    		Intent intent = new Intent(CircleDisplayActivity.this, GoalListActivity.class);
	    		startActivity(intent);
	    	}
		});
	}
}

