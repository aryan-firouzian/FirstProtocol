package uni.oulu.firstprotocol;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class FirstprotocolMainActivity extends Activity {

    Spinner cyclesSpinner;
    Spinner frequencySpinner;
    TextView log_textview;
    int blink_time=3;
    double frequency=1;
    String LogString="";

    private Hashtable<String,DirectionPattern> directions = new Hashtable<String,DirectionPattern>();
    private HmdBtCommunicator mBtCommunicator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstprotocol_main);
     
        cyclesSpinner =(Spinner)findViewById(R.id.cycles_spinner);
        frequencySpinner =(Spinner)findViewById(R.id.freq_spinner);
        log_textview = (TextView)findViewById(R.id.log_textview);
        
        mBtCommunicator = new HmdBtCommunicator(this, null);
        directions =  mBtCommunicator.getDirections();
    }


    @Override
    protected void onStart() {
    	super.onStart();
    	if (mBtCommunicator != null)
    		mBtCommunicator.doStart();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if (mBtCommunicator != null)
    		mBtCommunicator.doResume();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	if (mBtCommunicator != null)
    		mBtCommunicator.doStop();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.firstprotocol_main, menu);
        
       return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_connect:
            	mBtCommunicator.findDevice();
                return true;
                
            case R.id.action_reset:
                LogString="";

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goOnClick(View v)
    {
        int [] values = new int[12];
        values[0] = ((LedButton) findViewById(R.id.button1)).getLedState();
        values[1] = ((LedButton) findViewById(R.id.button2)).getLedState();
        values[2] = ((LedButton) findViewById(R.id.button3)).getLedState();
        values[3] = ((LedButton) findViewById(R.id.button4)).getLedState();
        values[4] = ((LedButton) findViewById(R.id.button5)).getLedState();
        values[5] = ((LedButton) findViewById(R.id.button6)).getLedState();
        values[6] = ((LedButton) findViewById(R.id.button7)).getLedState();
        values[7] = ((LedButton) findViewById(R.id.button8)).getLedState();
        values[8] = ((LedButton) findViewById(R.id.button9)).getLedState();
        values[9] = ((LedButton) findViewById(R.id.button10)).getLedState();
        values[10] = ((LedButton) findViewById(R.id.button11)).getLedState();
        values[11] = ((LedButton) findViewById(R.id.button12)).getLedState();

        blink_time = Integer.parseInt(cyclesSpinner.getSelectedItem().toString());
        //delay = Integer.parseInt(delaySpinner.getSelectedItem().toString());
        frequency = Double.valueOf(frequencySpinner.getSelectedItem().toString());

        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss.SSS");
        Date now = new Date();
        LogString=LogString+"_"+ compileLedValues(values)+","+frequency+","+blink_time+":"+String.valueOf(sdfDate.format(now));
        log_textview.setText(LogString);

        DirectionPattern p = new DirectionPattern(new long [] {
                compileLedValues(values),0x0 }, (int)((0.5/frequency)*1000),
                (int)((0.5/frequency)*1000), (int)(frequency*blink_time));

        directions.put(HmdBtCommunicator.DIR_GOAL_KEY, p);

        mBtCommunicator.setDirections(directions);

        mBtCommunicator.sendData(HmdBtCommunicator.DIR_GOAL_KEY);

    }

    public void storeOnClick(View v)
    {

    }


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case HmdBtCommunicator.REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
            	if (mBtCommunicator != null)	
            		mBtCommunicator.connectDevice(data, true);
            }
            break;
        case HmdBtCommunicator.REQUEST_CONNECT_DEVICE_INSECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
            	if (mBtCommunicator != null)
            		mBtCommunicator.connectDevice(data, false);
            }
            break;
        case HmdBtCommunicator.REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
            	if (mBtCommunicator != null)
            		mBtCommunicator.doStart();
            }
            break;
        default:
        	break;
        }
    }	

	private long compileLedValues(int [] values) {
		long ret = 0x0;
		for (int i=0; i<values.length; i++) {
			ret |= (long) ((values[i]&0x3) << (i*2));
		}
		
		return ret|0x80000000;
	}
}
