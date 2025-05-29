package Matthew.comp3200.Network;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;

import Matthew.comp3200.Tracker;

public class BTUtil {
    static String TAG = "BTUtil";
    public static BluetoothAdapter BTADAPTER;
    public static BluetoothDevice HOST;

    public static boolean initBT() {
        BTADAPTER = BluetoothAdapter.getDefaultAdapter();
        if (BTADAPTER == null) {

            return false;
        }

        return true;
    }



    public static void discoverHosts() {
        //return list of discovered devices

        if (ActivityCompat.checkSelfPermission(Tracker.getCurrentContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "No permission for scanning");
            return;
        }

        if(BTADAPTER.startDiscovery()){//starts discovery process, do it for ~12 sec
            Log.d(TAG, "Scanning for Hosts");
        }
        else{
            Log.d(TAG, "Scanning for Hosts failed");
        }

        TimerTask closeDis = new TimerTask() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(Tracker.getCurrentContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if(BTADAPTER.isDiscovering()){
                    BTADAPTER.cancelDiscovery();
                    Log.d(TAG, "Scanning turned off (timeout)");
                }
            }
        };
        Timer executor = new Timer("Timer");
        executor.schedule(closeDis,10000);

        //make sure to end discovery at the end of use since it takes a lot of resources from the bt adapter
    }



}
