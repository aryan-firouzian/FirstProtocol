/*
 * Author: Jari Tervonen <jjtervonen@gmail.com> 2013-
 * 
 * Mostly adapted from Android API sample's BluetoothChat:
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uni.oulu.firstprotocol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HmdBtCommunicator implements HmdCommunicator {

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    public static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    public static final int REQUEST_ENABLE_BT = 3;
    
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothChatService mChatService = null;
	
	private Activity activity_context = null;
	private boolean bConnected = false;
	private long last_data = 0x0;

	
	public HmdBtCommunicator(Activity a) {
		activity_context = a;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        doStart();
		
		/*if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            a.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}else {*/
        /*    if (mBluetoothAdapter.isEnabled())
            	if (mChatService == null) 
            		setupChat();
        }*/
		
	}
	
	@Override
	public void doStart() {

		// If BT is not on, request that it be enabled.
	    // setupChat() will then be called during onActivityResult
	    if (!mBluetoothAdapter.isEnabled()) {
	        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	        activity_context.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	    // Otherwise, setup the chat session
	    } else {
	        if (mChatService == null) setupChat();
	    }
	    
	}
	
	@Override
	public void doResume() {
		if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
	}
	
	@Override
	public void doStop() {
		if (mChatService != null) mChatService.stop();
	}

	@Override
	public boolean isConnected() {
		return bConnected;
	}
	
	private void setupChat() {
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(activity_context, mHandler);

        // Initialize the buffer for outgoing messages
        //mOutStringBuffer = new StringBuffer("");
    }
	
	public void connectDevice(String address, boolean secure) {

        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        Log.d("BTBLOP", "DEVICE:"+device.toString());
        if (device != null)
        	mChatService.connect(device, secure);
    }
	
	public void findDevice() {
		Intent serverIntent = null;
		serverIntent = new Intent(activity_context, DeviceListActivity.class);
        activity_context.startActivityForResult(serverIntent, HmdBtCommunicator.REQUEST_CONNECT_DEVICE_INSECURE);
        //connectDevice(serverIntent,false);
	}
	
	@Override
	public boolean sendData(long data) {
		// Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
           return false;
        }

        // Get the message bytes and tell the BluetoothChatService to write
        //byte[] send = toBytes(data);
        //mChatService.write(send);
        if (last_data != data) {
        	new SendDataTask().execute(data);
        	last_data = data;
        }
        return true;
	}
	
	
	// The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                //if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                	bConnected = true;
                    //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                    //mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    //setStatus(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    //setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                //mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                //mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                //Toast.makeText(getApplicationContext(), "Connected to "
                //               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                //Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                //               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
    
    private class SendDataTask extends AsyncTask <Long, Void, Void> {

		@Override
		protected Void doInBackground(Long... data) {
			byte[] send = toBytes(data[0]);
	        mChatService.write(send);
			return null;
		}
		
		protected void onPostExecute() {
	        //mImageView.setImageBitmap(result);
	    }
	}

	@Override
	public boolean sendData(int data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendData(byte data) {
		// TODO Auto-generated method stub
		return false;
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
