package Matthew.comp3200.Controllers;

import android.bluetooth.BluetoothHidDevice;
import android.util.Log;

public class Wheel extends Device{

    @Override
    public void setDescriptor() {
        hidDescriptor = new byte[]
            {
                (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
                (byte) 0x09, (byte) 0x04, /*        Usage (Joystick)                    */
                (byte) 0xa1, (byte) 0x01, /*        Collection (Application)            */

                (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
                (byte) 0x09, (byte) 0x30, /*        Usage (X)                           */
                (byte) 0x15, (byte) 0x81, /*        Logical Minimum (-127)              */
                (byte) 0x25, (byte) 0x7F, /*        Logical Maximum (127)               */
                (byte) 0x75, (byte) 0x08, /*        Report Size (8)                     */
                (byte) 0x95, (byte) 0x01, /*        Report Count (1)                    */
                (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */

//                (byte) 0x09, (byte) 0x32, /*        Usage (Z)                           */
//                (byte) 0x09, (byte) 0x35, /*        Usage (Rz)                          */
//                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
//                (byte) 0x26, (byte) 0xFF, (byte) 0x00, /*        Logical Maximum (255)               */
//                (byte) 0x75, (byte) 0x08, /*        Report Size (8)                     */
//                (byte) 0x95, (byte) 0x02, /*        Report Count (2)                    */
//                (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */


                (byte) 0x05, (byte) 0x02, /*        Usage Page (Simulation Controls)    */
                (byte) 0x09, (byte) 0xC4, /*        Usage (Accelerator)                 */
                (byte) 0x09, (byte) 0xC5, /*        Usage (Brake)                       */
                //(byte) 0x09, (byte) 0xC6, /*        Usage (Clutch)                      *//use to be assessed most likely not since then i would need a shift changer control
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x26, (byte) 0xFF, (byte) 0x00, /*        Logical Maximum (255)               */
                (byte) 0x75, (byte) 0x08, /*        Report Size (8)                     */
                (byte) 0x95, (byte) 0x02, /*        Report Count (2)                    */ // change to 3 if clutch is to be used
                (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */

                //shifters
                (byte) 0x05, (byte) 0x09, /*        Usage (Button)                      */
                (byte) 0x19, (byte) 0x01, /*        Usage Minimum (1)                   */
                (byte) 0x29, (byte) 0x02, /*        Usage Maximum (2)                   */
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x25, (byte) 0x01, /*        Logical Maximum (1)                 */
                (byte) 0x75, (byte) 0x01, /*        Report Size (1)                     */
                (byte) 0x95, (byte) 0x02, /*        Report Count (2)                    */
                (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */
                //padding 6-bits
                (byte) 0x75, (byte) 0x01, /*        Report Size (1)                     */
                (byte) 0x95, (byte) 0x06, /*        Report Count (6)                    */
                (byte) 0x81, (byte) 0x03, /*        Input (Const)                       */

                (byte) 0xC0,              /*        End Collection                      */

//                (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
//                (byte) 0x09, (byte) 0xBA, /*        Usage (Accelerator)                 */
//                (byte) 0x09, (byte) 0xBB, /*        Usage (Brake)                       */
//                (byte) 0x15, (byte) 0x81, /*        Logical Minimum (-127)              */
//                (byte) 0x25, (byte) 0x7F, /*        Logical Maximum (127)               */
//                (byte) 0x75, (byte) 0x08, /*        Report Size (8)                     */
//                (byte) 0x95, (byte) 0x02, /*        Report Count (2)                    */
//                (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */
//                (byte) 0xa1, (byte) 0x01, /*        Collection (Application)            */
            };
        currentReport = new byte[4];
        subclass = BluetoothHidDevice.SUBCLASS2_JOYSTICK;
    }

    public void move(float value,boolean lt){
        int converted = (int)(value);

        currentReport[0] = (byte)converted;
//        System.out.println(converted);
        makeReport(false,lt);
    }

    public static int ACCEL_INDEX = 1;
    public static int BRAKE_INDEX = 2;

    public void shift(int button_index,int value){
        currentReport[3] = (byte) ((currentReport[3] & ~(1 << button_index)) | (value << button_index));
        makeReport(true);
    }

    public void pressPedal(int button_index,int value){
        currentReport[button_index] = (byte)value;
        makeReport(true);
    }


}
