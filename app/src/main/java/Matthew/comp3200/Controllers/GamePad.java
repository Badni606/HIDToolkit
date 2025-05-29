package Matthew.comp3200.Controllers;

import android.bluetooth.BluetoothHidDevice;
import android.util.Log;

public class GamePad extends Device{
    String TAG = "Gamepad";
    @Override
    public void setDescriptor() {
        hidDescriptor = new byte[]
        {
            (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
            (byte) 0x09, (byte) 0x05, /*        Usage (Gamepad)                     */
            //XYAB BUTTONS
            (byte) 0xa1, (byte) 0x01, /*        Collection (Application)            */
            (byte) 0xa1, (byte) 0x00, /*        Collection (Physical)               */
            (byte) 0x05, (byte) 0x09, /*        Usage (Button)                      */
            (byte) 0x19, (byte) 0x01, /*        Usage Minimum (1)                   */
            (byte) 0x29, (byte) 0x04, /*        Usage Maximum (4)                   */
//            (byte) 0x15, (byte) 0x00, /*        Usage Minimum (1)                   */
            (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
            (byte) 0x25, (byte) 0x01, /*        Logical Maximum (1)                 */
            (byte) 0x75, (byte) 0x01, /*        Report Size (1)                     */
            (byte) 0x95, (byte) 0x04, /*        Report Count (4)                    */
            (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */
            //PADDING
            (byte) 0x75, (byte) 0x01, /*        Report Size (1)                     */
            (byte) 0x95, (byte) 0x04, /*        Report Count (4)                    */
            (byte) 0x81, (byte) 0x03, /*        Input (Constant)                    */

            //JOYSTICK
            (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
            (byte) 0x09, (byte) 0x30, /*        Usage (X)                           */
            (byte) 0x09, (byte) 0x31, /*        Usage (Y)                           */
            (byte) 0x15, (byte) 0x81, /*        Logical Minimum (-127)              */
            (byte) 0x25, (byte) 0x7f, /*        Logical Maximum (127)               */
            (byte) 0x75, (byte) 0x08, /*        Report Size (8)                     */
            (byte) 0x95, (byte) 0x02, /*        Report Count (2)                    */
            (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */

            (byte) 0xC0,              /*       End Collection                       */
            (byte) 0xC0,              /*       End Collection                       */
        };

//        currentReport = new byte[]{(byte)0,(byte)0,(byte)0};
        currentReport = new byte[3];
        subclass = BluetoothHidDevice.SUBCLASS2_GAMEPAD;
    }


    //TEMPORARY INDEXES, there is no standardisation, what did i expect from this world
    public static int X_BUTTON_INDEX = 0;

    public static int Y_BUTTON_INDEX = 1;

    public static int A_BUTTON_INDEX = 2;

    public static int B_BUTTON_INDEX = 3;

    public void toggleButton(int buttonIndex,int value){
        //currentReport[0] |= value << buttonIndex;          //sets bit //cool bitwise operation ;) thanks real-time embedded systems
        currentReport[0] = (byte) ((currentReport[0] & ~(1 << buttonIndex)) | (value << buttonIndex)); //bit clear and then bit set, only way i could think of doin it all in one
        makeReport(true);
    }

    public void moveThumbStick(int x,int y,boolean lt){

        //used to be used for when angle and strength were given
//        x = (int) (Math.cos(Math.toRadians(angle))*boundary*(strength/100.0));
//        y = - (int) (Math.sin(Math.toRadians(angle))*boundary*(strength/100.0));

//        Log.d(TAG, "X = "+x+" | Y = "+y);

        //java automatically assumes negative bit on 8th bit, eliminating the need for adjustment
        currentReport[1] = (byte) x;
        currentReport[2] = (byte) y;
        makeReport(false,lt);
    }


    //Report Structure
    // 0 0 0 0 Button Button Button Button
    // X X X X X X X X
    // Y Y Y Y Y Y Y Y
}
