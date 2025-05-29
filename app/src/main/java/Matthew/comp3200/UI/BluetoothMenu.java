package Matthew.comp3200.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Matthew.comp3200.Controllers.DIGamepad;
import Matthew.comp3200.Controllers.GamePad;
import Matthew.comp3200.Controllers.Mouse;
import Matthew.comp3200.Controllers.Wheel;
import Matthew.comp3200.Network.BTUtil;
import Matthew.comp3200.Tracker;
import Matthew.comp3200.Network.HidUtil;
import Matthew.comp3200.R;
import Matthew.comp3200.UI.Components.DeviceListAdapter;

public class BluetoothMenu extends AppCompatActivity {
    RecyclerView deviceListUI;
    DeviceListAdapter deviceListAdapter;
    Button scanButton;
    String TAG = "Bluetooth Screen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tracker.changeContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.bluetooth_menu);
        if (!BTUtil.initBT()) {
            Toast.makeText(this, "The phone does not have a Bluetooth adapter, application cannot function", Toast.LENGTH_LONG);
        }
        checkPerms();
        setUpUI();
        getDevices();
        scanAttempt();

    }

    @SuppressLint("MissingPermission")
    private void setUpUI() {
        deviceListUI = findViewById(R.id.device_list);

        deviceListAdapter = new DeviceListAdapter(this);
        deviceListAdapter.setClickListener((view, selectedDevice) -> {
            BTUtil.HOST = selectedDevice;
            BTUtil.BTADAPTER.cancelDiscovery(); //in case it wasn't already closed
            if(!BTUtil.BTADAPTER.getProfileProxy(Tracker.getCurrentContext(),HidUtil.listener, BluetoothProfile.HID_DEVICE)){
                Toast.makeText(Tracker.getCurrentContext(),"Phone does not have the required HID Bluetooth profile, application will not function",Toast.LENGTH_LONG);
            }
            Log.d(TAG, "Selected Device: "+selectedDevice.getName());
            toController();
        });

        deviceListUI.setLayoutManager(new LinearLayoutManager(this));
        deviceListUI.setAdapter(deviceListAdapter);

        Switch btToggle = (Switch) findViewById(R.id.bluetooth_toggle);
        btToggle.setChecked(BTUtil.BTADAPTER.isEnabled());
        btToggle.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 0);   //apparently this is old so i have no clue if this will work
            } else {
                BTUtil.BTADAPTER.disable();
            }
        });

        scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(v -> {
            if(BTUtil.BTADAPTER.isDiscovering()){
                return;
            }
            scanAttempt();

        });
    }

    /**
     * Check permissions are given and request them if not given
     */
    void checkPerms() {
        String[] perms = new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADMIN
        };
        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Missing Perm detected: " + perm);
                requestPermissions(perms, 100);
//                ActivityCompat.requestPermissions(this,perms,100);
                break;
            }
        }
    }

    //What to do when permission is given or rejected
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                boolean allPermissionsGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
                //TODO add actions in case of permission rejection
                if (allPermissionsGranted) {
                    Log.d(TAG, "All permissions granted");
                } else {
                    Log.d(TAG, "Not all permissions granted");
                }
            }
        }
    }

    final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(Tracker.getCurrentContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if(device.getName() != null){
                    deviceListAdapter.addEntry(device);
                }
            }
        }
    };

    void getDevices() {
        Log.d(TAG, "Starting Discovery");
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }
    void scanAttempt(){
        if(BTUtil.BTADAPTER.isEnabled()){
            deviceListAdapter.empty();
            BTUtil.discoverHosts();
        }
    }


    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    void toController(){
        Class screenClass;
        if(HidUtil.controller.getClass() == GamePad.class){
            screenClass = GamepadScreen.class;
        }
        else if(HidUtil.controller.getClass() == Mouse.class){
            screenClass = MouseScreen.class;
        }
        else if(HidUtil.controller.getClass() == Wheel.class){
            screenClass = WheelScreen.class;
        }
        else if(HidUtil.controller.getClass() == DIGamepad.class){
            screenClass = DIGamepadScreen.class;
        }
        else{
            screenClass = MouseScreen.class;
        }

        Intent intent = new Intent(this,screenClass);
        startActivity(intent);
    }



}
