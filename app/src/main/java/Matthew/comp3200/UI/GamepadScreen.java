package Matthew.comp3200.UI;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import Matthew.comp3200.Controllers.GamePad;
import Matthew.comp3200.Network.HidUtil;
import Matthew.comp3200.R;
import Matthew.comp3200.UI.Components.ConnectionView;
import Matthew.comp3200.UI.Components.Joystick;

public class GamepadScreen extends AppCompatActivity implements controllerScreen{
    Joystick thumbStick;
    GamePad gamepad= (GamePad) HidUtil.controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.game_pad);
        setUpUI();
    }

    @Override
    public void setUpUI() {
        setUpThumbStick();
        setUpButtons();
        setUpConnectionUI();
    }

    private void setUpButtons() {
        Button x = (Button)findViewById(R.id.x_button);
        setButtonListener(x,GamePad.X_BUTTON_INDEX);
        Button y = (Button)findViewById(R.id.y_button);
        setButtonListener(y,GamePad.Y_BUTTON_INDEX);
        Button a = (Button)findViewById(R.id.a_button);
        setButtonListener(a,GamePad.A_BUTTON_INDEX);
        Button b = (Button)findViewById(R.id.b_button);
        setButtonListener(b,GamePad.B_BUTTON_INDEX);

    }

    @SuppressLint("ClickableViewAccessibility")
    void setButtonListener(Button button, int button_index){
        button.setOnTouchListener((view, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){ //might need to add ACTION_CANCEL
                gamepad.toggleButton(button_index,1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                gamepad.toggleButton(button_index,0);
            }
            return false;
        });
    }


    void setUpThumbStick(){
        thumbStick = findViewById(R.id.thumb_stick);
        thumbStick.setOnMoveListener((x,y,lt)-> {
            gamepad.moveThumbStick(x,y,lt);
        });
    }

    ConnectionView connectionView;
    void setUpConnectionUI(){
        connectionView = findViewById(R.id.gamepad_connection);
        gamepad.setConnectionListener(c -> {
            connectionView.connectionChanged(c);
        });
    }


}
