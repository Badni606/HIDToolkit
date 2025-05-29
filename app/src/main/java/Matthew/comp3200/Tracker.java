package Matthew.comp3200;

import android.content.Context;
import android.util.Log;

import Matthew.comp3200.Network.BTUtil;
import Matthew.comp3200.Network.HidUtil;

public class Tracker {
    static String TAG = "Context Manager";
    static Context currentContext;
    static HidUtil hidUtil;
    static BTUtil btUtil;

    public static void changeContext(Context context){
        currentContext = context;
        Log.d(TAG, "Context changed to: "+context.getClass());
    }

    public static Context getCurrentContext(){
        return currentContext;
    }
}
