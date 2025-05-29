package Matthew.comp3200.UI;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import Matthew.comp3200.Controllers.DIGamepad;
import Matthew.comp3200.Controllers.GamePad;
import Matthew.comp3200.Controllers.Mouse;
import Matthew.comp3200.Controllers.Wheel;
import Matthew.comp3200.Network.HidUtil;
import Matthew.comp3200.R;
import Matthew.comp3200.Tracker;

public class MainActivity extends AppCompatActivity {
    //controllers to select from
    ImageButton gamepad;
    ImageButton wheel;
    ImageButton mouse;
    Button diGamepad;
    String TAG = "Controller Screen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tracker.changeContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.control_menu);
        setButtons();

    }


    void setButtons(){
        gamepad = findViewById(R.id.gamepad_select);
        wheel = findViewById(R.id.wheel_select);
        mouse = findViewById(R.id.mouse_select);
//        diGamepad = findViewById(R.id.gamepad_di_select);

        gamepad.setOnClickListener(view -> {
            HidUtil.initDevice(new GamePad());
            progress();
        });
        wheel.setOnClickListener(view -> {
            HidUtil.initDevice(new Wheel());
            progress();
        });
        mouse.setOnClickListener(view -> {
            HidUtil.initDevice(new Mouse());
            progress();
        });
//        diGamepad.setOnClickListener(view -> {
//            HidUtil.initDevice(new DIGamepad());
//            progress();
//        });
    }


    void progress(){
        Log.d(TAG, "Controller selected, transitioning screen.");
        Intent intent = new Intent(this, BluetoothMenu.class);
        startActivity(intent);
    }

}