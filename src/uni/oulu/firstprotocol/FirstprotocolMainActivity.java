package uni.oulu.firstprotocol;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class FirstprotocolMainActivity extends Activity {

	TextView LED_OrderTextView;
    Spinner BlinkingTimeSpinner;
    Spinner NotBlinkingTimeSpinner;
    Spinner FrequencySpinner;
    int BlinkingTime=3;
    int NotBlinkingTime=5;
    double Frequency=1;
    int FirstCycle;
    int SecondCycle;
    int jNumber;
    

    int i =0;
    int j=0;
	Timer myTimer = new Timer();
	Timer FrequecyTimer = new Timer();
    
	public static final long G1_VAL = 0x80000002;
	public static final long R1_VAL = 0x80000001;
	public static final long G2_VAL = 0x80000008;
	public static final long R2_VAL = 0x80000004;
	public static final long G3_VAL = 0x80000020;
	public static final long R3_VAL = 0x80000010;
	public static final long G4_VAL = 0x80000080;
	public static final long R4_VAL = 0x80000040;
	public static final long G5_VAL = 0x80000200;
	public static final long R5_VAL = 0x80000100;
	public static final long G6_VAL = 0x80000800;
	public static final long R6_VAL = 0x80000400;
	public static final long G7_VAL = 0x80002000;
	public static final long R7_VAL = 0x80001000;
	public static final long G8_VAL = 0x80008000;
	public static final long R8_VAL = 0x80004000;
	public static final long G9_VAL = 0x80020000;
	public static final long R9_VAL = 0x80010000;
	public static final long G10_VAL = 0x80080000;
	public static final long R10_VAL = 0x80040000;
	public static final long G11_VAL = 0x80200000;
	public static final long R11_VAL = 0x80100000;
	public static final long G12_VAL = 0x80800000;
	public static final long R12_VAL = 0x80400000;
	
	public static final String G1_KEY = "G1";
	public static final String R1_KEY = "R1";
	public static final String G2_KEY = "G2";
	public static final String R2_KEY = "R2";
	public static final String G3_KEY = "G3";
	public static final String R3_KEY = "R3";
	public static final String G4_KEY = "G4";
	public static final String R4_KEY = "R4";
	public static final String G5_KEY = "G5";
	public static final String R5_KEY = "R5";
	public static final String G6_KEY = "G6";
	public static final String R6_KEY = "R6";
	public static final String G7_KEY = "G7";
	public static final String R7_KEY = "R7";
	public static final String G8_KEY = "G8";
	public static final String R8_KEY = "R8";
	public static final String G9_KEY = "G9";
	public static final String R9_KEY = "R9";
	public static final String G10_KEY = "G10";
	public static final String R10_KEY = "R10";
	public static final String G11_KEY = "G11";
	public static final String R11_KEY = "R11";
	public static final String G12_KEY = "G12";
	public static final String R12_KEY = "R12";
	
	private Hashtable<String,Long> LEDsLong = new Hashtable<String,Long>();
	
	private String LED_OrderString="";
    private ArrayList<String> ArrayOfPressedBtnId=new ArrayList<String>();
    private ArrayList<Button> PressesdButtons=new ArrayList<Button>();
    
    private HmdBtCommunicator mBtCommunicator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstprotocol_main);
     
        
        
        LED_OrderTextView = (TextView)findViewById(R.id.LED_OrderTextView);
        BlinkingTimeSpinner =(Spinner)findViewById(R.id.BlinkingTime);
        NotBlinkingTimeSpinner =(Spinner)findViewById(R.id.NotBlinkingTime);
        FrequencySpinner =(Spinner)findViewById(R.id.Frequency);
        
        LEDsLong.put(G1_KEY, G1_VAL);
        LEDsLong.put(R1_KEY, R1_VAL);
        LEDsLong.put(G2_KEY, G2_VAL);
        LEDsLong.put(R2_KEY, R2_VAL);
        LEDsLong.put(G3_KEY, G3_VAL);
        LEDsLong.put(R3_KEY, R3_VAL);
        LEDsLong.put(G4_KEY, G4_VAL);
        LEDsLong.put(R4_KEY, R4_VAL);
        LEDsLong.put(G5_KEY, G5_VAL);
        LEDsLong.put(R5_KEY, R5_VAL);
        LEDsLong.put(G6_KEY, G6_VAL);
        LEDsLong.put(R6_KEY, R6_VAL);
        LEDsLong.put(G7_KEY, G7_VAL);
        LEDsLong.put(R7_KEY, R7_VAL);
        LEDsLong.put(G8_KEY, G8_VAL);
        LEDsLong.put(R8_KEY, R8_VAL);
        LEDsLong.put(G9_KEY, G9_VAL);
        LEDsLong.put(R9_KEY, R9_VAL);
        LEDsLong.put(G10_KEY, G10_VAL);
        LEDsLong.put(R10_KEY, R10_VAL);
        LEDsLong.put(G11_KEY, G11_VAL);
        LEDsLong.put(R11_KEY, R11_VAL);
        LEDsLong.put(G12_KEY, G12_VAL);
        LEDsLong.put(R12_KEY, R12_VAL);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.firstprotocol_main, menu);
        return true;
        

    }
    
    public void onLEDBtnClicked(View v){

    	Button PressedBtn = (Button)findViewById(v.getId());
    	PressedBtn.setBackgroundColor(Color.BLUE);
    	PressedBtn.setEnabled(false);
    	LED_OrderTextView.setText(PressedBtn.getText().toString()+LED_OrderString);
    	LED_OrderString=LED_OrderTextView.getText().toString();
    	
    	ArrayOfPressedBtnId.add(PressedBtn.getText().toString());
    	PressesdButtons.add(PressedBtn);
    	//if(v.getId() == R.id.my_btn){
            //handle the click here
        //}
    }
    
    public void onStartBtnClicked(View v){

        mBtCommunicator.connectDevice(((MyApplication) this.getApplication()).getDeviceAddress(), true);
        //
        BlinkingTime = Integer.parseInt(BlinkingTimeSpinner.getSelectedItem().toString());
        NotBlinkingTime = Integer.parseInt(NotBlinkingTimeSpinner.getSelectedItem().toString());
        Frequency = Double.valueOf(FrequencySpinner.getSelectedItem().toString());
        FirstCycle=(BlinkingTime*1000)+(NotBlinkingTime*1000);
        SecondCycle=(int)((1/Frequency)*1000);
        jNumber=(int)((BlinkingTime*1000)/((1/Frequency)*1000));
    	
        myTimer = new Timer();
    	myTimer.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethod();
			}
		}, 3000, FirstCycle);
    }
    private void TimerMethod()
	{	
    	this.runOnUiThread(Timer_Tick);
	}
    private Runnable Timer_Tick = new Runnable() {
		public void run() {
			i=i+1;
			//LED_OrderTextView.setText(String.valueOf(i));
			if(i==ArrayOfPressedBtnId.size()){myTimer.cancel();}
			FrequecyTimer = new Timer();
			FrequecyTimer.schedule(new TimerTask(){
				@Override
				public void run() {
				FrequencyTimerMethod();
			}
				} ,0,SecondCycle);
		}
	};
	 private void FrequencyTimerMethod()
		{	
	    	this.runOnUiThread(Frequency_Timer_Tick);
		}
	 private Runnable Frequency_Timer_Tick = new Runnable() {
			public void run() {
				j=j+1;
		    	if(j==jNumber){FrequecyTimer.cancel();j=0;}
		    	long longData = LEDsLong.get(ArrayOfPressedBtnId.get(i-1));
		    	//byte[] byteData = toBytes(longData);
		    	mBtCommunicator.sendData(longData);
                }
			};
	 
    public void onResetBtnClicked(View v){
    	myTimer.cancel();
    	FrequecyTimer.cancel();
    	i=0;
    	j=0;
    	LED_OrderString="";
    	LED_OrderTextView.setText("");
    	ArrayOfPressedBtnId.clear();
    	for(int i=1; i<=PressesdButtons.size(); i++)
    	{
    		(PressesdButtons.get(i-1)).setEnabled(true);
    		(PressesdButtons.get(i-1)).setBackgroundColor(Color.LTGRAY);
    	}
    	PressesdButtons.clear();
    }
    
    public void onConnectBtnClicked(View v){
    	if (mBtCommunicator == null)
    	{
    		
    		mBtCommunicator = new HmdBtCommunicator(this);
    	}
    	mBtCommunicator.findDevice();

    }
    
	protected byte[] toBytes(long d) {

		byte [] ret = new byte[4];
			
		ret[0] = (byte) (d  & 0xff);
		ret[1] = (byte) ((d >>> 8) & 0xff);
		ret[2] = (byte) ((d >>> 16) & 0xff);
		ret[3] = (byte) ((d >>> 24) & 0xff);
		return ret;
	 }
    
}
