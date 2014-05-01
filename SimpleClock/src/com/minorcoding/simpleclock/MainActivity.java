package com.minorcoding.simpleclock;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private TextView[] mTimeLabels = new TextView[9]; // UI Labels to display days, time and full date.
	private boolean mVisualTick = true; // Used to toggle a '.' or ':'
	private String mSeperator = ":";	// Used to store the mVisualTick indicator
	private Time clock = new Time();			// Time class
	private final Handler messenger = new Handler(); // Handler used to call runner
	private Timer interval = new Timer(); // Timer class used to refresh the time every second.
	private dayMonthFormatter formatDate = new dayMonthFormatter(); // class to format day numbers '1st' '2nd' '3rd' '4th' etc...
																	// And resolve month numbers 1 = January etc...
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initilise String Array
        mTimeLabels[0] = (TextView) findViewById(R.id.timeLabel);
        mTimeLabels[1] = (TextView) findViewById(R.id.textMon);
        mTimeLabels[2] = (TextView) findViewById(R.id.textTue);
        mTimeLabels[3] = (TextView) findViewById(R.id.textWed);
        mTimeLabels[4] = (TextView) findViewById(R.id.textThu);
        mTimeLabels[5] = (TextView) findViewById(R.id.textFri);
        mTimeLabels[6] = (TextView) findViewById(R.id.textSat);
        mTimeLabels[7] = (TextView) findViewById(R.id.textSun);
        mTimeLabels[8] = (TextView) findViewById(R.id.textDate);
        
        // Define Timer schedule
        interval.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// call Handler
				runnerCall();
			}
		}, 0, 1000);
    }

    private void runnerCall(){
    	// post to runner method
    	messenger.post(runner);
    }
    
    private final Runnable runner = new Runnable() {
    	public void run() {
    		// Set HH && MM separator
    		if (mVisualTick){
    			mSeperator = ":";
    		}
    		else{
    			mSeperator = " ";
    		}
    		// update the time
    		clock.setToNow();
    		mTimeLabels[0].setText(String.format("%02d%s%02d%s%02d\n%s", clock.hour,mSeperator,clock.minute,mSeperator,clock.second,clock.isDst==1?"DST":"ST")); // Update the time label
    		mTimeLabels[8].setText(String.format("%d%s\n%s", clock.monthDay,formatDate.getDayPrefix(clock.monthDay),
    				formatDate.getMonthName(clock.month + 1))); // Update the day/month label.
    		mTimeLabels[clock.weekDay].setTextColor(Color.parseColor("#FF4242")); // brighten up the color of relevant day label. 
    		mTimeLabels[clock.weekDay-1	].setTextColor(Color.parseColor("#18522B")); // make the previous day label darker (for when the day changes)
    				
    		// toggle the separator on / off e.g HH:MM or HH MM
    		mVisualTick = !mVisualTick;
    	}
    };
    
    
    @Override
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
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            return rootView;
        }
    }

}
