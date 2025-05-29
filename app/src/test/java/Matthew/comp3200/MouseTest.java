package Matthew.comp3200;

import org.junit.Test;
import static org.junit.Assert.*;

import Matthew.comp3200.Controllers.Mouse;

public class MouseTest {
    Mouse testerMouse = new Mouse();

    @Test
    public void buttonTest(){
        byte[] expected = {1,0,0,0,0,0};
        testerMouse.click(0,1);
        assertArrayEquals(expected,testerMouse.getCurrentReport());

        expected[0] = 3;
        testerMouse.click(1,1);
        assertArrayEquals(expected,testerMouse.getCurrentReport());

        expected[0] = 2;
        testerMouse.click(0,0);
        assertArrayEquals(expected,testerMouse.getCurrentReport());

        expected[0] = 0;
        testerMouse.click(1,0);
        assertArrayEquals(expected,testerMouse.getCurrentReport());
    }

    @Test
    public void moveTest(){
        byte[] expected = {0,15,15,0,0,0};
        testerMouse.move(15,15,false);
        assertArrayEquals(expected,testerMouse.getCurrentReport());

        expected[1] = 127;
        testerMouse.move(127,15,false);
        assertArrayEquals(expected,testerMouse.getCurrentReport());
        //reset
        testerMouse.move(0,0,false);
    }

    @Test
    public void scrollTest(){
        byte[] expected = {0,0,0,15,15,0};
        testerMouse.scroll(15,15,false);
        assertArrayEquals(expected,testerMouse.getCurrentReport());

        expected[3] = 127;
        testerMouse.scroll(127,15,false);
        assertArrayEquals(expected,testerMouse.getCurrentReport());
        //reset
        testerMouse.scroll(0,0,false);
    }
    @Test
    public void zoomTest(){
        byte[] expected = {0,0,0,0,0,15};
        testerMouse.zoom(15,false);
        assertArrayEquals(expected,testerMouse.getCurrentReport());

        expected[5] = 127;
        testerMouse.zoom(127,false);
        assertArrayEquals(expected,testerMouse.getCurrentReport());
        //reset
        testerMouse.zoom(0,false);
    }
}
