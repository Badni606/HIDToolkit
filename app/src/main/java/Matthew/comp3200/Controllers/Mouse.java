package Matthew.comp3200.Controllers;

import android.bluetooth.BluetoothHidDevice;

import Matthew.comp3200.Network.HidUtil;

/**
 * A controller for testing HID functionality
 */
public class Mouse extends Device{

    @Override
    public void setDescriptor() {
        hidDescriptor = new byte[]{
            (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
            (byte) 0x09, (byte) 0x02, /*        Usage (Mouse)                       */

            (byte) 0xa1, (byte) 0x01, /*        Collection (Application)            */
            (byte) 0x85, (byte) 0x01, /*        Report ID                           */
            (byte) 0x09, (byte) 0x01, /*        Usage (Pointer)                     */
            (byte) 0xa1, (byte) 0x00, /*        Collection (Physical)               */
            (byte) 0x05, (byte) 0x09, /*        Usage Page (Button)                 */
            (byte) 0x19, (byte) 0x01, /*        Usage Minimum (1)                   */
            (byte) 0x29, (byte) 0x02, /*        Usage Maximum (2)                   */
            (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
            (byte) 0x25, (byte) 0x01, /*        Logical Maximum (1)                 */
            (byte) 0x95, (byte) 0x02, /*        Report Count (2)                    */
            (byte) 0x75, (byte) 0x01, /*        Report Size (1)                     */
            (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */

            //padding to align byte boundary
            (byte) 0x95, (byte) 0x06, /*        Report Count (6)                    */
            (byte) 0x75, (byte) 0x01, /*        Report Size (1)                     */
            (byte) 0x81, (byte) 0x01, /*        Input (Constant)                    */

            (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
            (byte) 0x09, (byte) 0x30, /*        Usage (X)                           */
            (byte) 0x09, (byte) 0x31, /*        Usage (Y)                           */
            (byte) 0x15, (byte) 0x81, /*        Logical Minimum (-127)              */
            (byte) 0x25, (byte) 0x7f, /*        Logical Maximum (127)               */
            (byte) 0x75, (byte) 0x08, /*        Report Size (8)                     */
            (byte) 0x95, (byte) 0x02, /*        Report Count (2)                    */
            (byte) 0x81, (byte) 0x06, /*        Input (Variable,Relative)           */

            (byte) 0x09, (byte) 0x38, /*        Usage (Scroll)                      */
            (byte) 0x15, (byte) 0x81, /*        Logical Minimum (-127)              */
            (byte) 0x25, (byte) 0x7f, /*        Logical Maximum (127)               */
            (byte) 0x75, (byte) 0x08, /*        Report Size (8)                     */
            (byte) 0x95, (byte) 0x01, /*        Report Count (1)                    */
            (byte) 0x81, (byte) 0x06, /*        Input (Variable,Relative)           */

            //Horizontal scrolling and Zoom
            (byte) 0x05, (byte) 0x0C, /*        Usage Page (Consumer Devices)       */
            (byte) 0x0A, (byte) 0x38, (byte) 0x02, /*        Usage (Panning)                      */
            (byte) 0x0A, (byte) 0x2F, (byte) 0x02, /*        Usage (Zoom)                         */
            (byte) 0x15, (byte) 0x81, /*        Logical Minimum (-127)              */
            (byte) 0x25, (byte) 0x7f, /*        Logical Maximum (127)               */
            (byte) 0x75, (byte) 0x08, /*        Report Size (8)                     */
            (byte) 0x95, (byte) 0x02, /*        Report Count (2)                    */
            (byte) 0x81, (byte) 0x06, /*        Input (Variable,Relative)           */


            (byte) 0xC0,              /*       End Collection                       */
            (byte) 0xC0,              /*       End Collection                       */
        };
        currentReport = new byte[6];
        subclass = BluetoothHidDevice.SUBCLASS1_MOUSE;
        reportID = 1;
    }
    //Report Structure
    // 0 0 0 0 0 0 R L
    // X X X X X X X X
    // Y Y Y Y Y Y Y Y
    public void click(int button_index,int value){
//        currentReport[0] |= value<<button_index;
        currentReport[0] = (byte) ((currentReport[0] & ~(1 << button_index)) | (value << button_index));
        makeReport(true);
    }

    public void move(int x,int y,boolean end){
        currentReport[1] = (byte) x;
        currentReport[2] = (byte) y;
        makeReport(false,end);
    }

    public void scroll(int vScroll,int hScroll,boolean lt){
        currentReport[3] = (byte)vScroll;
        currentReport[4] = (byte)hScroll;
        makeReport(false,lt);//todo FIX, make it use the thread one
    }

    public void zoom(int zoom,boolean lt){
        currentReport[5] = (byte)zoom;
        makeReport(false,lt);
    }
}
