package uni.oulu.firstprotocol;

import android.app.Application;

/**
 * Created by aryan on 3/11/14.
 */
public class MyApplication extends Application {

    private String deviceAddress;

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }
}
