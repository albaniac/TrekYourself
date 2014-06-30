package com.example.trekyourself;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.util.Log;
import org.apache.http.HttpResponse;

import java.util.Date;
import java.util.HashMap;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;

import android.os.AsyncTask;
import android.os.Handler;

import java.util.List;

import android.net.http.AndroidHttpClient;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

public class MainActivity extends Activity {

	public Handler mHandler = new Handler() {
	    public void handleMessage(Message msg) {
	        updateTime();
	    }
	};	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.primary_layout);	//textViewTrekDate
        Log.d("TREK", "State : Here");
        updateTime();
        
        ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);
        Runnable periodicTask = new Runnable(){
            @Override
            public void run() {
                try{
                	mHandler.obtainMessage(1).sendToTarget();
                    Thread.sleep(30 * 1000);

                } catch(Exception e){
                     
                }
            }
        };        
        ScheduledFuture<?> periodicFuture = sch.scheduleAtFixedRate(periodicTask, 5, 5, TimeUnit.SECONDS);
        //updateTime();
    }

	private class HttpGetTask extends AsyncTask<Void, Void, HashMap<String, MonitoredServer>> {

		private static final String TAG = "HttpGetTask";

		private static final String URL = "http://63.226.80.152:8080/nagios/status.xml";	

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected HashMap<String, MonitoredServer> doInBackground(Void... params) {
			HttpGet request = new HttpGet(URL);
			XMLResponseHandler responseHandler = new XMLResponseHandler();
			try {
				return mClient.execute(request, responseHandler);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute( HashMap<String, MonitoredServer> result) {
			if (null != mClient)
				mClient.close();
			//setListAdapter(new ArrayAdapter<String>(
			//		NetworkingAndroidHttpClientXMLActivity.this,
			//		R.layout.list_item, result));
		}
	}
       
    
    
    private void updateTime() {
        TextViewTrek tvDate = (TextViewTrek) findViewById(R.id.textViewTrekDate);
        TextViewTrek tvTime = (TextViewTrek) findViewById(R.id.textViewTrekTime);
        //MMM dd hh:mm:ss yyyy 
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        String currentDateStr = sdf.format(new Date());
        tvDate.setText(currentDateStr);

        SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
        String currentTimeStr = stf.format(new Date());
        tvTime.setText(currentTimeStr);
    	
    	
    }

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
