package Matthew.comp3200;

import org.junit.Test;
import static org.junit.Assert.*;

import Matthew.comp3200.Controllers.Wheel;
import Matthew.comp3200.UI.Components.Gyro;

public class SteeringWheelTest {
    Wheel testerWheel = new Wheel();


    @Test
    public void pedalTest(){
        byte[] expected = {0,(byte)0xFF,0,0};
        testerWheel.pressPedal(Wheel.ACCEL_INDEX,255);
        assertArrayEquals(expected,testerWheel.getCurrentReport());

        expected[2] = (byte)0xFF;
        testerWheel.pressPedal(Wheel.BRAKE_INDEX,255);
        assertArrayEquals(expected,testerWheel.getCurrentReport());

        expected[1] = 0;
        testerWheel.pressPedal(Wheel.ACCEL_INDEX,0);
        assertArrayEquals(expected,testerWheel.getCurrentReport());

        expected[2] = 0;
        testerWheel.pressPedal(Wheel.BRAKE_INDEX,0);
        assertArrayEquals(expected,testerWheel.getCurrentReport());
    }

    @Test
    public void shifterTest(){
        byte[] expected = {0,(byte)0xFF,0,1};
        testerWheel.pressPedal(0,1);
        assertArrayEquals(expected,testerWheel.getCurrentReport());

        expected[3] = 3;
        testerWheel.pressPedal(1,1);
        assertArrayEquals(expected,testerWheel.getCurrentReport());

        expected[3] = 1;
        testerWheel.pressPedal(1,0);
        assertArrayEquals(expected,testerWheel.getCurrentReport());

        expected[3] = 0;
        testerWheel.pressPedal(0,0);
        assertArrayEquals(expected,testerWheel.getCurrentReport());

    }
    @Test
    public void steerTest(){
        byte[] expected = {25,0,0,0};
        testerWheel.move(25,false);
        assertArrayEquals(expected,testerWheel.getCurrentReport());

        expected[0] = -127;
        testerWheel.move(-127,false);
        assertArrayEquals(expected,testerWheel.getCurrentReport());

        expected[0] = 127;
        testerWheel.move(127,false);
        assertArrayEquals(expected,testerWheel.getCurrentReport());

        expected[0] = 0;
        testerWheel.move(0,false);
        assertArrayEquals(expected,testerWheel.getCurrentReport());
    }

    Gyro testerGyro = new Gyro();

    @Test
    public void rotationTest(){
        float expected = ((float)Math.toDegrees(0.6f) / 70) * 127;
        assertEquals(expected,testerGyro.angleFormatter(0.6f),0.1);

        //should cap at 127
        expected = 127;
        assertEquals(expected,testerGyro.angleFormatter(3),0.1);

        expected = -127;
        assertEquals(expected,testerGyro.angleFormatter(-3),0.1);
    }
}
