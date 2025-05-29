package Matthew.comp3200.UI;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import Matthew.comp3200.Controllers.Mouse;
import Matthew.comp3200.Network.HidUtil;
import Matthew.comp3200.R;
import Matthew.comp3200.Tracker;
import Matthew.comp3200.UI.Components.ConnectionView;
import Matthew.comp3200.UI.Components.Touchpad;

public class MouseScreen extends AppCompatActivity implements controllerScreen{
    String TAG = "MouseScreen";
    Mouse mouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tracker.changeContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mouse = (Mouse) HidUtil.controller;
        setContentView(R.layout.mouse);
        setUpUI();
        setUpConnectionUI();
    }

    ConnectionView connectionView;
    void setUpConnectionUI(){
        connectionView = findViewById(R.id.mouse_connection);
        mouse.setConnectionListener(c -> {
            connectionView.connectionChanged(c);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setUpUI() {
        Button leftClick = findViewById(R.id.left_mouse_button);
        leftClick.setOnTouchListener((view, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){ //might need to add ACTION_CANCEL
                mouse.click(0,1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                mouse.click(0,0);
            }
            return false;
        });

        Button rightClick = findViewById(R.id.right_mouse_button);
        rightClick.setOnTouchListener((view, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                mouse.click(1,1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                mouse.click(1,0);
            }
            return false;
        });

        Touchpad touchpad = findViewById(R.id.touchpad);
        touchpad.setOnMoveListener(new Touchpad.OnMoveListener() {
            @Override
            public void onMove(int x, int y, boolean lt) {
                mouse.move(x,y,lt);
            }

            @Override
            public void onCLick(boolean lClick, boolean rClick) {
                mouse.click(0,lClick?1:0);
                mouse.click(1,rClick?1:0);
            }

            @Override
            public void onScroll(int vScroll, int hScroll,boolean lt) {
                mouse.scroll(vScroll,hScroll,lt);
            }

            @Override
            public void onZoom(int zoom,boolean lt) {
                mouse.zoom(zoom,lt);
            }
        });

    }


}
