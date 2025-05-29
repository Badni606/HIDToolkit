package Matthew.comp3200.Network;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import Matthew.comp3200.Controllers.Device;
import Matthew.comp3200.Tracker;

public class HidUtil extends BluetoothHidDevice.Callback implements BluetoothProfile.ServiceListener {
    static String TAG = "HID Util";

    static BluetoothHidDevice EMULATOR;

    //needed for listening to connection status
    public static HidUtil listener;
    public static Device controller;

    static boolean connected = false;
    static long SEND_DELAY = 15; //milliseconds was 10
    static  boolean transmitting = false;

    public static void initDevice(Device device) {
        controller = device;
        controller.setDescriptor();
        listener = new HidUtil();
    }

    static int reportID_holder;
    static byte[] report_holder;
    @SuppressLint("MissingPermission")
    public static void sendReport(byte[] report, int reportID,boolean button,boolean last) {
        if(!connected){
            return;
        }

        report_holder = report;
        reportID_holder = reportID;
        if(button && !transmitting){
            EMULATOR.sendReport(BTUtil.HOST, reportID, report);
            Log.d(TAG, "Button report sent");
        }
        else if(!transmitting){
            transmitting = true;
            Log.d(TAG, "Reporting started");
        }
        else if(last){
            transmitting = false;
            EMULATOR.sendReport(BTUtil.HOST, reportID, report);
            Log.d(TAG, "Last report sent!");
        }

//        if(!connected){
//            Log.d(TAG, "Unable to send report : EMULATOR not connected to host!");
//            return;
//        }
//        boolean sent = EMULATOR.sendReport(BTUtil.HOST, reportID, report);
//        Log.d(TAG, "Report "+(sent?"sent":"not sent"));
    }


    //-------------------------HID callback functions------------------------------
    //called when status proxy device's connection state changes
    @Override
    public void onConnectionStateChanged(BluetoothDevice device, int state) {
        super.onConnectionStateChanged(device, state);
        String status = null;
        switch (state) {
            case BluetoothProfile.STATE_CONNECTED:
                connected = true;
                status = "Connected";
                new Thread(HidUtil.listener.senderTask).start();
                controller.setConnection(true);
                break;
            case BluetoothProfile.STATE_CONNECTING:
                status = "Connecting";
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                status = "Disconnected";
                connected = false;
                controller.setConnection(false);
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                status = "Disconnecting";
                break;
        }
        Log.d(TAG, "HID device connection status with other device changed to : " + status);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onAppStatusChanged (BluetoothDevice pluggedDevice,boolean registered){
        if(registered){
            Log.d(TAG, "App successfully registered");
            Log.d(TAG, "Connection sent = "+ EMULATOR.connect(BTUtil.HOST)+
                    "\nPlugged device is: "+pluggedDevice);

        }
        else{
            Log.d(TAG, "App not registered");
        }
    }

    @Override
    public void onGetReport(BluetoothDevice device, byte type, byte id, int bufferSize) {
        super.onGetReport(device, type, id, bufferSize);
        Log.d(TAG, "Get report received from HOST");
    }

    //--------------------Service listener(For establishing BT connection)----------------------------------------
    //is called when change to the bluetooth connection is made
    @SuppressLint("MissingPermission")
    @Override
    public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
        Log.d(TAG, "--------------------------------------------\n" +
                "Profile is "+(i==BluetoothProfile.HID_DEVICE?"HID ":"not HID:"+i));
        
        EMULATOR = (BluetoothHidDevice) bluetoothProfile;
        Log.d(TAG, "Service Check:"+
                "\nController subclass: "+controller.subclass+
                "\nAdapter enabled = "+BTUtil.BTADAPTER.isEnabled());


        EMULATOR.unregisterApp();
        boolean registered = EMULATOR.registerApp(generateSDP(), null, null,Tracker.getCurrentContext().getMainExecutor(), listener);

        Log.d(TAG, "HID "+(registered?"registering":"not registering")+
                "\n-------------------------------------------");
    }

    @Override
    public void onServiceDisconnected(int i) {

    }


    //--------------------------------------------------------------------------
    BluetoothHidDeviceAppSdpSettings generateSDP(){
        return new BluetoothHidDeviceAppSdpSettings("Mobile Emulator","Controller Emulator",
                "Meth Studios",controller.subclass,controller.hidDescriptor);
    }


    @SuppressLint("MissingPermission")
    public Runnable senderTask = () -> {
        while(connected){
//            Log.d(TAG, "Report made");
            if(transmitting){
//                controller.makeReport();
                EMULATOR.sendReport(BTUtil.HOST, reportID_holder, report_holder);
                try {
                    Thread.sleep(SEND_DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
//            else{
//                EMULATOR.sendReport(BTUtil.HOST, reportID_holder, report_holder);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
            //TODO might need to send periodic reports to prevent disconnection due to inactivity
        }
        Log.i(TAG, "No longer Sending Reports");
    };

    @SuppressLint("MissingPermission")
    public static void connect(){
        EMULATOR.connect(BTUtil.HOST);
    }

}
  /*
05 01        (GLOBAL) USAGE_PAGE         0x0001 Generic Desktop Page
09 05        (LOCAL)  USAGE              0x00010005 Game Pad (Application Collection)

A1 01        (MAIN)   COLLECTION         0x01 Application (Usage=0x00010005: Page=Generic Desktop Page, Usage=Game Pad, Type=Application Collection)
A1 00          (MAIN)   COLLECTION         0x00 Physical (Usage=0x0: Page=, Usage=, Type=) <-- Error: COLLECTION must be preceded by a USAGE <-- Warning: USAGE type should be CP (Physical Collection)
09 30            (LOCAL)  USAGE              0x00010030 X (Dynamic Value)
09 31            (LOCAL)  USAGE              0x00010031 Y (Dynamic Value)
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0)  <-- Info: Consider replacing 15 00 with 14
27 FFFF0000      (GLOBAL) LOGICAL_MAXIMUM    0x0000FFFF (65535)
95 02            (GLOBAL) REPORT_COUNT       0x02 (2) Number of fields
75 10            (GLOBAL) REPORT_SIZE        0x10 (16) Number of bits per field
81 02            (MAIN)   INPUT              0x00000002 (2 fields x 16 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
C0             (MAIN)   END_COLLECTION     Physical

A1 00          (MAIN)   COLLECTION         0x00 Physical (Usage=0x0: Page=, Usage=, Type=) <-- Error: COLLECTION must be preceded by a USAGE <-- Warning: USAGE type should be CP (Physical Collection)
09 33            (LOCAL)  USAGE              0x00010033 Rx (Dynamic Value)
09 34            (LOCAL)  USAGE              0x00010034 Ry (Dynamic Value)
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
27 FFFF0000      (GLOBAL) LOGICAL_MAXIMUM    0x0000FFFF (65535) <-- Redundant: LOGICAL_MAXIMUM is already 65535
95 02            (GLOBAL) REPORT_COUNT       0x02 (2) Number of fields <-- Redundant: REPORT_COUNT is already 2
75 10            (GLOBAL) REPORT_SIZE        0x10 (16) Number of bits per field <-- Redundant: REPORT_SIZE is already 16
81 02            (MAIN)   INPUT              0x00000002 (2 fields x 16 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
C0             (MAIN)   END_COLLECTION     Physical


05 01          (GLOBAL) USAGE_PAGE         0x0001 Generic Desktop Page <-- Redundant: USAGE_PAGE is already 0x0001
09 32          (LOCAL)  USAGE              0x00010032 Z (Dynamic Value)
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
26 FF03        (GLOBAL) LOGICAL_MAXIMUM    0x03FF (1023)
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields
75 0A          (GLOBAL) REPORT_SIZE        0x0A (10) Number of bits per field
81 02          (MAIN)   INPUT              0x00000002 (1 field x 10 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
padding
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 00          (GLOBAL) LOGICAL_MAXIMUM    0x00 (0)  <-- Info: Consider replacing 25 00 with 24
75 06          (GLOBAL) REPORT_SIZE        0x06 (6) Number of bits per field
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
81 03          (MAIN)   INPUT              0x00000003 (1 field x 6 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap


05 01          (GLOBAL) USAGE_PAGE         0x0001 Generic Desktop Page <-- Redundant: USAGE_PAGE is already 0x0001
09 35          (LOCAL)  USAGE              0x00010035 Rz (Dynamic Value)
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
26 FF03        (GLOBAL) LOGICAL_MAXIMUM    0x03FF (1023)
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
75 0A          (GLOBAL) REPORT_SIZE        0x0A (10) Number of bits per field
81 02          (MAIN)   INPUT              0x00000002 (1 field x 10 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
padding
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 00          (GLOBAL) LOGICAL_MAXIMUM    0x00 (0)  <-- Info: Consider replacing 25 00 with 24
75 06          (GLOBAL) REPORT_SIZE        0x06 (6) Number of bits per field
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
81 03          (MAIN)   INPUT              0x00000003 (1 field x 6 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap


05 09          (GLOBAL) USAGE_PAGE         0x0009 Button Page
19 01          (LOCAL)  USAGE_MINIMUM      0x00090001 Button 1 Primary/trigger (Selector, On/Off Control, Momentary Control, or One Shot Control)
29 0A          (LOCAL)  USAGE_MAXIMUM      0x0009000A Button 10 (Selector, On/Off Control, Momentary Control, or One Shot Control)
95 0A          (GLOBAL) REPORT_COUNT       0x0A (10) Number of fields
75 01          (GLOBAL) REPORT_SIZE        0x01 (1) Number of bits per field
81 02          (MAIN)   INPUT              0x00000002 (10 fields x 1 bit) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 00          (GLOBAL) LOGICAL_MAXIMUM    0x00 (0) <-- Redundant: LOGICAL_MAXIMUM is already 0 <-- Info: Consider replacing 25 00 with 24
75 06          (GLOBAL) REPORT_SIZE        0x06 (6) Number of bits per field
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields
81 03          (MAIN)   INPUT              0x00000003 (1 field x 6 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
*/
    /*

05 01          (GLOBAL) USAGE_PAGE         0x0001 Generic Desktop Page
09 39          (LOCAL)  USAGE              0x00010039 Hat switch (Dynamic Value)
15 01          (GLOBAL) LOGICAL_MINIMUM    0x01 (1)
25 08          (GLOBAL) LOGICAL_MAXIMUM    0x08 (8)
35 00          (GLOBAL) PHYSICAL_MINIMUM   0x00 (0)  <-- Info: Consider replacing 35 00 with 34
46 3B01        (GLOBAL) PHYSICAL_MAXIMUM   0x013B (315)
66 1400        (GLOBAL) UNIT               0x0014 Rotation in degrees [1° units] (4=System=English Rotation, 1=Rotation=Degrees)  <-- Info: Consider replacing 66 1400 with 65 14
75 04          (GLOBAL) REPORT_SIZE        0x04 (4) Number of bits per field
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
81 42          (MAIN)   INPUT              0x00000042 (1 field x 4 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 1=Null 0=NonVolatile 0=Bitmap

75 04          (GLOBAL) REPORT_SIZE        0x04 (4) Number of bits per field <-- Redundant: REPORT_SIZE is already 4
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0)  <-- Info: Consider replacing 15 00 with 14
25 00          (GLOBAL) LOGICAL_MAXIMUM    0x00 (0)  <-- Info: Consider replacing 25 00 with 24
35 00          (GLOBAL) PHYSICAL_MINIMUM   0x00 (0) <-- Redundant: PHYSICAL_MINIMUM is already 0 <-- Info: Consider replacing 35 00 with 34
45 00          (GLOBAL) PHYSICAL_MAXIMUM   0x00 (0)  <-- Info: Consider replacing 45 00 with 44
65 00          (GLOBAL) UNIT               0x00 No unit (0=None)  <-- Info: Consider replacing 65 00 with 64
81 03          (MAIN)   INPUT              0x00000003 (1 field x 4 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

A1 02          (MAIN)   COLLECTION         0x02 Logical (Usage=0x0: Page=, Usage=, Type=) <-- Error: COLLECTION must be preceded by a USAGE <-- Warning: USAGE type should be CL (Logical Collection)
05 0F            (GLOBAL) USAGE_PAGE         0x000F Physical Interface Device Page
09 97            (LOCAL)  USAGE              0x000F0097 DC Enable Actuators (Selector)
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 01            (GLOBAL) LOGICAL_MAXIMUM    0x01 (1)
75 04            (GLOBAL) REPORT_SIZE        0x04 (4) Number of bits per field <-- Redundant: REPORT_SIZE is already 4
95 01            (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
91 02            (MAIN)   OUTPUT             0x00000002 (1 field x 4 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 00            (GLOBAL) LOGICAL_MAXIMUM    0x00 (0)  <-- Info: Consider replacing 25 00 with 24
91 03            (MAIN)   OUTPUT             0x00000003 (1 field x 4 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

09 70            (LOCAL)  USAGE              0x000F0070 Magnitude (Dynamic Value)
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 64            (GLOBAL) LOGICAL_MAXIMUM    0x64 (100)
75 08            (GLOBAL) REPORT_SIZE        0x08 (8) Number of bits per field
95 04            (GLOBAL) REPORT_COUNT       0x04 (4) Number of fields
91 02            (MAIN)   OUTPUT             0x00000002 (4 fields x 8 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

09 50            (LOCAL)  USAGE              0x000F0050 Duration (Dynamic Value)
66 0110          (GLOBAL) UNIT               0x1001 Time in seconds [1 s units] (1=System=SI Linear, 1=Time=Seconds)
55 0E            (GLOBAL) UNIT_EXPONENT      0x0E (Unit Value x 10⁻²)
26 FF00          (GLOBAL) LOGICAL_MAXIMUM    0x00FF (255)
95 01            (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields
91 02            (MAIN)   OUTPUT             0x00000002 (1 field x 8 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

09 A7            (LOCAL)  USAGE              0x000F00A7 Start Delay (Dynamic Value)
91 02            (MAIN)   OUTPUT             0x00000002 (1 field x 8 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

65 00            (GLOBAL) UNIT               0x00 No unit (0=None)  <-- Info: Consider replacing 65 00 with 64
55 00            (GLOBAL) UNIT_EXPONENT      0x00 (Unit Value x 10⁰)  <-- Info: Consider replacing 55 00 with 54
09 7C            (LOCAL)  USAGE              0x000F007C Loop Count (Dynamic Value)
91 02            (MAIN)   OUTPUT             0x00000002 (1 field x 8 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
C0             (MAIN)   END_COLLECTION     Logical
  */
    /*
05 01          (GLOBAL) USAGE_PAGE         0x0001 Generic Desktop Page
09 80          (LOCAL)  USAGE              0x00010080 System Control (Application Collection)
A1 00          (MAIN)   COLLECTION         0x00 Physical (Usage=0x00010080: Page=Generic Desktop Page, Usage=System Control, Type=Application Collection) <-- Warning: USAGE type should be CP (Physical Collection)
09 85            (LOCAL)  USAGE              0x00010085 System Main Menu (One Shot Control)
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 01            (GLOBAL) LOGICAL_MAXIMUM    0x01 (1)
95 01            (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
75 01            (GLOBAL) REPORT_SIZE        0x01 (1) Number of bits per field
81 02            (MAIN)   INPUT              0x00000002 (1 field x 1 bit) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 00            (GLOBAL) LOGICAL_MAXIMUM    0x00 (0)  <-- Info: Consider replacing 25 00 with 24
75 07            (GLOBAL) REPORT_SIZE        0x07 (7) Number of bits per field
95 01            (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
81 03            (MAIN)   INPUT              0x00000003 (1 field x 7 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
C0             (MAIN)   END_COLLECTION     Physical

05 06          (GLOBAL) USAGE_PAGE         0x0006 Generic Device Controls Page
09 20          (LOCAL)  USAGE              0x00060020 Battery Strength (Dynamic Value)
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
26 FF00        (GLOBAL) LOGICAL_MAXIMUM    0x00FF (255)
75 08          (GLOBAL) REPORT_SIZE        0x08 (8) Number of bits per field
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
81 02          (MAIN)   INPUT              0x00000002 (1 field x 8 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
C0           (MAIN)   END_COLLECTION     Application
*/