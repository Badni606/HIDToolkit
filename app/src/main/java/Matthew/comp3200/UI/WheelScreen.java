package Matthew.comp3200.UI;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import Matthew.comp3200.Controllers.Wheel;
import Matthew.comp3200.Network.HidUtil;
import Matthew.comp3200.UI.Components.ConnectionView;
import Matthew.comp3200.UI.Components.Gyro;
import Matthew.comp3200.R;

public class WheelScreen extends AppCompatActivity implements controllerScreen {
    Gyro gyroSensor;
    Wheel wheel = (Wheel) HidUtil.controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.steering_wheel);
        setUpUI();
    }

    @Override
    public void setUpUI() {
        setUpConnectionUI();
        setUpButtons();
        setRotationListener();
    }

    ConnectionView connectionView;
    void setUpConnectionUI(){
        connectionView = findViewById(R.id.wheel_connection);
        wheel.setConnectionListener(c -> {
            connectionView.connectionChanged(c);
        });
    }
    Button accel;
    Button brake;
    Button shiftUp;
    Button shiftDown;
    @SuppressLint("ClickableViewAccessibility")
    void setUpButtons(){
        accel = findViewById(R.id.accelerate_button);
        brake = findViewById(R.id.brake_button);
        setPedalListener(accel,Wheel.ACCEL_INDEX);
        setPedalListener(brake,Wheel.BRAKE_INDEX);

        shiftUp = findViewById(R.id.shift_up);
        shiftDown = findViewById(R.id.shift_down);
        setShiftListener(shiftUp,0);
        setShiftListener(shiftDown,1);

        wheelIcon = findViewById(R.id.wheel_icon);
    }

    @SuppressLint("ClickableViewAccessibility")
    void setShiftListener(Button button, int index){
        button.setOnTouchListener((v,event)-> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){ //might need to add ACTION_CANCEL
                wheel.shift(index,1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                wheel.shift(index,0);
            }
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    void setPedalListener(Button button, int index){
        button.setOnTouchListener((v,event)-> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){ //might need to add ACTION_CANCEL
                wheel.pressPedal(index,255);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                wheel.pressPedal(index,0);
            }
            return true;
        });
    }


    ImageView wheelIcon;
    float lastZ= 0.0f;
    boolean lastSend = false;
    void setRotationListener() {
        gyroSensor = new Gyro();
        gyroSensor.setListener((z)->{
            if(z!=lastZ){
                wheel.move(z,false);
                lastZ=z;
                lastSend = false;
            }
            else if (z==0 && lastZ == z && !lastSend){
                wheel.move(z,true);
                lastSend = true;
            }
            wheelIcon.setRotation(-z);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gyroSensor.deactivateSensor();
    }

}
