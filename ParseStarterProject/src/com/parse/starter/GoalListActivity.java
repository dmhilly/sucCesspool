package com.parse.starter;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class GoalListActivity extends ListActivity {
	ParseUser currentUser = ParseUser.getCurrentUser();
	private CustomAdapter mainAdapter;
	private ListView listView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goal_list);
		mainAdapter = new CustomAdapter(this);

		listView = (ListView) findViewById(R.id.goal_list);
		listView.setAdapter(mainAdapter);
		mainAdapter.loadObjects();
		listView.setOnItemClickListener((OnItemClickListener) mainAdapter);

		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_goal_list, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.action_refresh: {
			updateGoalList();
			break;
		}

		case R.id.action_new: {
			newGoal();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	//http://www.michaelevans.org/blog/2013/08/14/tutorial-building-an-android-to-do-list-app-using-parse/
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Goal goal = mainAdapter.getItem(position);
		goal.setCompleted(!goal.isCompleted());
		
		if(goal.isCompleted()) {
		//	goal.setTitle("Goal Completed!");
		}
		goal.saveInBackground();
		
		boolean[] goalsCompleted = areAllGoalsCompleted();
		boolean allGoalsCompleted = true;
		for (int i = 0; i < goalsCompleted.length; i++) {
			if (goalsCompleted[i] == false) {
				allGoalsCompleted = false;
			}
			if (allGoalsCompleted) {
				popUp();
			}
			
		}
		updateGoalList();
	}
	
	private void updateGoalList() {
		mainAdapter.loadObjects();
		setListAdapter(mainAdapter);
	}
	
	private void popUp() {
		LayoutInflater layoutInflater 
	     = (LayoutInflater)getBaseContext()
	      .getSystemService(LAYOUT_INFLATER_SERVICE);  
	    View popupView = layoutInflater.inflate(R.layout.popup, null);  
	             final PopupWindow popupWindow = new PopupWindow(
	               popupView, 
	               LayoutParams.WRAP_CONTENT,  
	                     LayoutParams.WRAP_CONTENT); 
	             final TextView message = (TextView) findViewById (R.id.goals_completed);
	      	           
	             ParseQuery<Circle> query = ParseQuery.getQuery("Circle");
		 			query.whereEqualTo("userId", currentUser.getObjectId());
		 			query.getFirstInBackground(new GetCallback<Circle>() {
		 			public void done(Circle circle, ParseException e) {
		 				// TODO Auto-generated method stub
		 				   if (e == null) {
		 			            String dollarsCommitted = circle.getString("dollars");
		 			            String charity = circle.getString("charity");
		 			 
		 			           message.setText("Congratulations! You have completed your goals for this cycle and earned back $" + dollarsCommitted +
		 			        		   "/n Please still consider donating to " + charity + " though! :)");
		 				      	 
		 				   } 
		 			}
		 		});
	            
	    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				archiveCompletedGoals();// TODO Auto-generated method stub			
				updateGoalList();
				ParseQuery<Circle> query = ParseQuery.getQuery("Circle");
		 			query.whereEqualTo("userId", currentUser.getObjectId());
		 			query.getFirstInBackground(new GetCallback<Circle>() {
		 			public void done(Circle circle, ParseException e) {
		 				// TODO Auto-generated method stub
		 				   if (e == null) {
		 			            circle.setArchived(true); 
		 			            circle.saveInBackground();
		 				   } 
		 			}
		 		});
		 		Intent intent = new Intent(GoalListActivity.this, CreateCircleActivity.class);
		 		startActivity(intent);
				
			}
	    	
	    });
		        
	                
	             Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
	             btnDismiss.setOnClickListener(new Button.OnClickListener(){

	     @Override
	     public void onClick(View v) {
	      // TODO Auto-generated method stub
	      popupWindow.dismiss();
	     }});
	               
	}
	
	private boolean[] areAllGoalsCompleted() {
		boolean[] goalCompletion = new boolean[mainAdapter.getCount()];
		for (int i = 0; i< mainAdapter.getCount(); i++) {
			if (mainAdapter.getItem(i).isCompleted()) {
				goalCompletion[i] = true;
			}
		}
		return goalCompletion;
	}
	
	private void archiveCompletedGoals() {
		for (int i=0; i < mainAdapter.getCount(); i++) {
			mainAdapter.getItem(i).setCompleted(true);
			mainAdapter.getItem(i).saveInBackground();
		}
	}
	
	private void newGoal() {
		Intent i = new Intent(this, NewGoalActivity.class);
		startActivityForResult(i, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			updateGoalList();
		}
	}

}
