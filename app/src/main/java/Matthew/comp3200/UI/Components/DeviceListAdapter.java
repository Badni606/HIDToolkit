package Matthew.comp3200.UI.Components;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Matthew.comp3200.R;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {

    // stores and recycles views as they are scrolled off screen
    private List<BluetoothDevice> devices;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView deviceLabel;
        ImageView deviceIcon;

        ViewHolder(View itemView) {
            super(itemView);
            deviceLabel = itemView.findViewById(R.id.device_list_item);
            deviceIcon = itemView.findViewById(R.id.device_list_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, devices.get(getAdapterPosition()));
        }
    }

    Drawable[] icons = new Drawable[4];

    public DeviceListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        devices = new ArrayList<BluetoothDevice>(5);
        icons[0] = ContextCompat.getDrawable(context,R.drawable.pc_icon);
        icons[1] = ContextCompat.getDrawable(context,R.drawable.laptop_icon);
        icons[2] = ContextCompat.getDrawable(context,R.drawable.phone_icon);
        icons[3] = ContextCompat.getDrawable(context,R.drawable.question_icon);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.device_list_button, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BluetoothDevice host = devices.get(position);
        String deviceName = host.getName();
        holder.deviceLabel.setText(deviceName);
        //todo add icon depending on
        Drawable dIcon;
        switch(host.getBluetoothClass().getDeviceClass()){
            case BluetoothClass.Device.COMPUTER_DESKTOP:
                dIcon = icons[0];
                break;
            case BluetoothClass.Device.COMPUTER_LAPTOP:
                dIcon = icons[1];
                break;
            case BluetoothClass.Device.PHONE_SMART:
                dIcon = icons[2];
                break;
            default:
                dIcon = icons[3];
                break;
        }
        holder.deviceIcon.setImageDrawable(dIcon);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return devices.size();
    }


    // convenience method for getting data at click position
    BluetoothDevice getItem(int id) {
        return devices.get(id);
    }

    public void addEntry(BluetoothDevice device){
        devices.add(device);
        notifyItemInserted(devices.size());
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, BluetoothDevice selectedDevice);
    }

    public void empty(){
        int size = devices.size();
        devices.clear();
        notifyItemRangeRemoved(0,size);
    }
}
