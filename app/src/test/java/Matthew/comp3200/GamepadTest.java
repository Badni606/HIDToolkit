package Matthew.comp3200;

import org.junit.Test;
import static org.junit.Assert.*;

import Matthew.comp3200.Controllers.GamePad;
import Matthew.comp3200.UI.Components.Joystick;

public class GamepadTest {
    GamePad testerGamepad = new GamePad();

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void moveStickTest(){
        byte[] expected = {0,15,15};
        testerGamepad.moveThumbStick(15,15,false);
        assertArrayEquals(expected,testerGamepad.getCurrentReport());

        expected[1] = 127;
        testerGamepad.moveThumbStick(127,15,false);
        assertArrayEquals(expected,testerGamepad.getCurrentReport());
        testerGamepad.moveThumbStick(0,0,false);
    }

    @Test
    public void buttonTest(){
        byte[] expected = {1,0,0};
        testerGamepad.toggleButton(0,1);
        assertArrayEquals(expected,testerGamepad.getCurrentReport());

        expected[0] = 9;
        testerGamepad.toggleButton(3,1);
        assertArrayEquals(expected,testerGamepad.getCurrentReport());

        expected[0] = 11;
        testerGamepad.toggleButton(1,1);
        assertArrayEquals(expected,testerGamepad.getCurrentReport());

        testerGamepad.toggleButton(2,0);
        assertArrayEquals(expected,testerGamepad.getCurrentReport());
    }

    //making tests for this is pretty hard, my classes have to many dependencies
//    @Test
//    public void joystickTest(){
//        Joystick testerJoystick = new Joystick(null);
//        testerJoystick.setOnMoveListener((x, y, lt) -> {
//            // does nothing, to prevent errors
//        });
//
//        testerJoystick.
//
//    }
}
