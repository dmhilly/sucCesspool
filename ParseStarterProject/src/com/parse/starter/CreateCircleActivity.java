package com.parse.starter;

//import android.support.v7.app.ActionBarActivity;
import com.parse.ParseObject;

import com.parse.ParseUser;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;

//trying to get alarm to run in background
/*import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;*/

public class CreateCircleActivity extends Activity {

ParseUser currentUser = ParseUser.getCurrentUser();
Circle circle = new Circle();

//trying to get alarm to run in background
/*final static private long ONE_SECOND = 1000;
final static private long TWENTY_SECONDS = ONE_SECOND * 20;
PendingIntent pi;
BroadcastReceiver br;
AlarmManager am;*/


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_circle);  
        
        final Intent serviceIntent;
        
        serviceIntent = new Intent(CreateCircleActivity.this, MyService.class);

        
      /*  private void setup() {
            br = new BroadcastReceiver() {
                   @Override
                   public void onReceive(Context c, Intent i) {
                          Toast.makeText(c, "Rise and Shine!", Toast.LENGTH_LONG).show();
                          }
                   };
            registerReceiver(br, new IntentFilter("com.authorwjf.wakeywakey") );
            pi = PendingIntent.getBroadcast( this, 0, new Intent("com.authorwjf.wakeywakey"),
      0 );
            am = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
      }*/
        
       // setup();
        Button buttonCreatePool = (Button)findViewById(R.id.buttonCreatePool);
        buttonCreatePool.setOnClickListener(new View.OnClickListener()
        

        {
        	public void onClick(View v)
        	{
        		EditText inputCircleName = (EditText)findViewById(R.id.inputCircleName);
                EditText inputCycleLength = (EditText)findViewById(R.id.inputCycleLength);
                EditText inputMoneyCommitted = (EditText)findViewById(R.id.inputMoneyCommitted);
                EditText inputCharity = (EditText)findViewById(R.id.inputCharity);

                //c1.setFirstUser(currentUser);
                circle.setUserId(currentUser.getObjectId());
                circle.setArchived(false);
                
                //int cycleLength=0;
        		//double moneyCommitted=0;
        		boolean validationError = false;
        		StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro2));

        		
                //makes sure inputCycleLength is an integer
                try{
                	 circle.setCycleLength(Integer.parseInt(inputCycleLength.getText().toString()));
                } catch (Exception e){
                	validationError=true;
                	validationErrorMessage.append(getString(R.string.cycle_length_error));
                }
                
                //makes sure inputMoneyCommitted is an integer
                try{
                	 circle.setDollarsCommitted(Double.parseDouble(inputMoneyCommitted.getText().toString()));
                } catch (Exception e){
                	validationError=true;
                	validationErrorMessage.append(getString(R.string.money_committed_error));
                }
                      
                //makes sure inputcircleName is a real string
                String name = new String(inputCircleName.getText().toString());
                if (name.length() == 0)
                {
                	validationError=true;
                	validationErrorMessage.append(getString(R.string.circle_name_error));
                }
                else if (name.length() != 0 && name.charAt(0) == (' '))
                {
                	char firstCharacter = name.charAt(0);
                	while (firstCharacter == ' ' && name.length() > 0)
                	{
                		name = name.substring(1,name.length());
                	}
                	if (name.length() == 0)
                    {
                    	validationError=true;
                    	validationErrorMessage.append(getString(R.string.circle_name_error));
                    }
                	circle.setCircleName(inputCircleName.getText().toString());
                }
                circle.setCircleName(inputCircleName.getText().toString());
                
                //makes sure inputCharity is a real string
                String charity = new String(inputCharity.getText().toString());
                if (charity.length() == 0)
                {
                	validationError=true;
                	validationErrorMessage.append(getString(R.string.charity_input_error));
                }
                else if (charity.length() != 0 && charity.charAt(0) == (' '))
                {
                	char firstCharacter = charity.charAt(0);
                	while (firstCharacter == ' ' && charity.length() > 0)
                	{
                		charity = charity.substring(1,charity.length());
                	}
                	if (charity.length() == 0)
                	{
                		validationError=true;
                    	validationErrorMessage.append(getString(R.string.charity_input_error));
                	}
                	circle.setCharity(inputCharity.getText().toString());
                }
                circle.setCharity(inputCharity.getText().toString());
                
                //displays validation error
                if (validationError) {
                    Toast.makeText(CreateCircleActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                        .show();
                    return;
                }

                circle.saveInBackground();
                
        	    Intent intent = new Intent(CreateCircleActivity.this, CircleDisplayActivity.class);
        		startActivity(intent);
        		
        		//startService(serviceIntent);
        	}  
     });
    
   }
}








//when user clicks "Click me to calculate money per day",
//app takes user input for inputCycle Length and inputMoneyCommitted
//to calculate what each day is worth and to print it out to the screen
//Button buttonCalculate = (Button)findViewById(R.id.buttonCalculate);      
//buttonCalculate.setOnClickListener(new View.OnClickListener()

/*        {
        	public void onClick(View v)
        	{
                
                double moneyPerDay = Math.round(moneyCommitted / cycleLength);
            	//the following line from Stack Overflow rounds the money value to 2 decimal points
                String moneyPerDayRounded = String.format("%.2f", moneyPerDay);
                //put the doubles inside of a try catch in case users input a non-integer. put the money per day and rounded in there
                //because otherwise there were errors saying that cycle length and money committed didn't exist

                TextView display = (TextView)findViewById(R.id.displayMoneyPerDay);
                display.setText("Each day is worth $" + moneyPerDayRounded + ".");
                

                Circle c1= new Circle();
                c1.setCircleName(inputCircleName.getText().toString());
                c1.setFirstUsersPoints((int)moneyCommitted);
                c1.setMoneyPerDay(moneyPerDay);
                c1.setFirstUser(ParseUser.getCurrentUser());
                c1.saveInBackground();

        	}
        });
    }
*/

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } */

