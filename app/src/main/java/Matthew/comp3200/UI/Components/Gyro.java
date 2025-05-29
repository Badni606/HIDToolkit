package Matthew.comp3200.UI.Components;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import Matthew.comp3200.Tracker;

public class Gyro implements SensorEventListener {
    //TODO find what sensor the phone has (accelerometer and magnometer OR a gyroscope)
    String TAG = "Gyro";
    Sensor rotationSensor;
    SensorManager sensorManager ;
    float[] matrix;
    onRotationChange listener;

    public interface onRotationChange{
        public void rotationListener(float z);
    }

    public Gyro(){
        //grab gyroscope
        sensorManager = (SensorManager) Tracker.getCurrentContext().getSystemService(Context.SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        matrix = new float[5];
    }

    public void setListener(onRotationChange listener){
        this.listener = listener;
        activateSensor();
    }
    //called when needed
    void activateSensor(){
        sensorManager.registerListener(this, rotationSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    //turn off sensor listener when not needed since its a waste of resources
    public void deactivateSensor(){
        sensorManager.unregisterListener(this);
    }

    int rotationBounding = 70;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            float[] r = new float[9];
            float[] o = new float[3];
            SensorManager.getRotationMatrixFromVector(r,sensorEvent.values);
            SensorManager.getOrientation(r, o);
            float z = angleFormatter(o[1]);
//            System.out.println(z);
            listener.rotationListener(z);

        }

    }

    public float angleFormatter(float rads){
        float z = rads;
        z = (float)Math.toDegrees(z);
        z = -z;
        int sensitivity = 3;
        if(-sensitivity < z && z < sensitivity){
            z = 0;
        }
        z = Math.max(Math.min(rotationBounding,z),-rotationBounding);   //bounding
        z = z/rotationBounding *127;
        return z;
    }

    //no use, i think...
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
