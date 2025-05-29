package Matthew.comp3200.Controllers;

import android.bluetooth.BluetoothHidDevice;

import Matthew.comp3200.Network.HidUtil;

public abstract class Device {

    public interface connectionListener{
        void onConnectionChange(boolean c);
    }
    public byte[] hidDescriptor;
    byte[] currentReport;
    /**
     * Overwrite for each device
     */
    public byte subclass = BluetoothHidDevice.SUBCLASS2_UNCATEGORIZED;
    int reportID = 0; //Honestly don't know if i'll need this eventually.Perhaps for when multiple are connected

    public abstract void setDescriptor();

    public void makeReport(boolean button){
        HidUtil.sendReport(currentReport,reportID,button,false);
    };

    public void makeReport(boolean button,boolean last){
        HidUtil.sendReport(currentReport,reportID,button,last);
    };

    byte[] intToBytes(int val){
        byte[] temp = new byte[2];
        temp[0] = (byte) ((val >> 8) & 0xFF);
        temp[1] = (byte) val;
        return temp;
    }

    boolean connected;
    connectionListener connectionListener;
    public void setConnection(boolean c){
        connected = c;
        if(connectionListener != null){
            connectionListener.onConnectionChange(c);
        }
    }

    public void setConnectionListener(Device.connectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public byte[] getCurrentReport() {
        return currentReport;
    }
}
