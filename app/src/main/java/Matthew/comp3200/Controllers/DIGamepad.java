package Matthew.comp3200.Controllers;

import android.bluetooth.BluetoothHidDevice;
import android.util.Log;

public class DIGamepad extends Device{

    @Override
    public void setDescriptor() {
        hidDescriptor = new byte[]{
                (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
                (byte) 0x09, (byte) 0x05, /*        Usage (Gamepad)                     */
                //Left Joystick
                (byte) 0xA1, (byte) 0x01, /*        Collection (Application)            */
                (byte) 0xA1, (byte) 0x00, /*        Collection (Physical)               */
                (byte) 0x09, (byte) 0x30, /*        Usage (X)                           */
                (byte) 0x09, (byte) 0x31, /*        Usage (Y)                           */
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x27, (byte) 0xFF, (byte) 0xFF,                                  /*        Logical Maximum (65535)               */
                (byte) 0x95, (byte) 0x02, /*        Report Count (2)                    */
                (byte) 0x75, (byte) 0x10, /*        Report Size (16)                    */
                (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */
                (byte) 0xC0,              /*       End Collection                       */

                //Right Joystick
                (byte) 0xA1, (byte) 0x00, /*        Collection (Physical)               */
                (byte) 0x09, (byte) 0x33, /*        Usage (RX)                          */
                (byte) 0x09, (byte) 0x34, /*        Usage (RY)                          */
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x27, (byte) 0xFF, (byte) 0xFF,                                  /*        Logical Maximum (65535)               */
                (byte) 0x95, (byte) 0x02, /*        Report Count (2)                    */
                (byte) 0x75, (byte) 0x10, /*        Report Size (16)                    */
                (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */
                (byte) 0xC0,              /*       End Collection                       */

                //Left Trigger?
                (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
                (byte) 0x09, (byte) 0x32, /*        Usage (Z)                           */
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x26, (byte) 0x03, (byte) 0xFF,                                   /*        Logical Maximum (1023)               */
                (byte) 0x95, (byte) 0x01, /*        Report Count (1)                    */
                (byte) 0x75, (byte) 0x0A, /*        Report Size (10)                    */
                (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */
                //6-bit padding
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x25, (byte) 0x00,
                (byte) 0x75, (byte) 0x06, /*        Report Size (1)                     */
                (byte) 0x95, (byte) 0x01, /*        Report Count (6)                    */
                (byte) 0x81, (byte) 0x03, /*        Input (Constant)                    */

                //Right Trigger?
                (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
                (byte) 0x09, (byte) 0x35, /*        Usage (RZ)                          */
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x26, (byte) 0x03, (byte) 0xFF,                                   /*        Logical Maximum (1023)               */
                (byte) 0x95, (byte) 0x01, /*        Report Count (1)                    */
                (byte) 0x75, (byte) 0x0A, /*        Report Size (10)                    */
                (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */
                //6-bit padding
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x25, (byte) 0x00,
                (byte) 0x75, (byte) 0x06, /*        Report Size (1)                     */
                (byte) 0x95, (byte) 0x01, /*        Report Count (6)                    */
                (byte) 0x81, (byte) 0x03, /*        Input (Constant)                      */

                //Buttons
                (byte) 0x05, (byte) 0x09, /*        Usage (Button)                      */
                (byte) 0x19, (byte) 0x01, /*        Usage Maximum (1)                   */
                (byte) 0x29, (byte) 0x0A, /*        Usage Maximum (10)                  */
                (byte) 0x95, (byte) 0x0A, /*        Report Count (10)                   */
                (byte) 0x75, (byte) 0x01, /*        Report Size (1)                     */
                (byte) 0x81, (byte) 0x02, /*        Input (Variable)                    */
                //6-bit padding
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x25, (byte) 0x00,
                (byte) 0x75, (byte) 0x06, /*        Report Size (1)                     */
                (byte) 0x95, (byte) 0x01, /*        Report Count (6)                    */
                (byte) 0x81, (byte) 0x03, /*        Input (Constant)                    */
                //fine until here -------------------------------------------------------------------------------------------------------------------
                //Hat switch? Not sure what this is
                (byte) 0x05, (byte) 0x01, /*        Usage Page (Generic Desktop)        */
                (byte) 0x09, (byte) 0x39, /*        Usage (Hat Switch)                  */  //Not sure what this is
                (byte) 0x15, (byte) 0x01, /*        Logical Minimum (1)                 */
                (byte) 0x25, (byte) 0x08, /*        Logical Maximum (8)                 */
                (byte) 0x35, (byte) 0x00, /*        Physical Minimum (0)                */
                (byte) 0x46, (byte) 0x01, (byte) 0x3B,                                   /*        Physical Maximum (315)                 */
                (byte) 0x66, (byte) 0x14, /*        Unit (8)                            */
                (byte) 0x75, (byte) 0x04, /*        Report Size (4)                     */
                (byte) 0x95, (byte) 0x01, /*        Report Count (1)                    */
                (byte) 0x81, (byte) 0x42, /*        Input (Var)                         */
                //4-bit padding
                (byte) 0x75, (byte) 0x07, /*        Report Size (1)                     */
                (byte) 0x95, (byte) 0x01, /*        Report Count (4)                    */
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x25, (byte) 0x00,
                (byte) 0x35, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x45, (byte) 0x00,
                (byte) 0x65, (byte) 0x00,
                (byte) 0x81, (byte) 0x03, /*        Input (Constant)                    */

                (byte) 0xA1, (byte) 0x02,
                //Outputs
                (byte) 0x05, (byte) 0x0F, /*    Usage Page (Physical Interface Device)  */
                (byte) 0x09, (byte) 0x97, /*        Usage Page (Dc Actuators)           */
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x25, (byte) 0x01, /*        Logical Maximum (1)                 */
                (byte) 0x75, (byte) 0x04, /*        Report Size (4)                     */
                (byte) 0x95, (byte) 0x01, /*        Report Count (1)                    */
                (byte) 0x91, (byte) 0x02, /*        Output                              */ //I think this is for rumbling and stuff like that
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x25, (byte) 0x00, /*        Logical Maximum (100)  */
                (byte) 0x91, (byte) 0x03,

                (byte) 0x09, (byte) 0x70, /*        Usage Page (Magnitude)              */
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x25, (byte) 0x64, /*        Logical Maximum (100)               */
                (byte) 0x75, (byte) 0x08, /*        Report Size (8)                     */
                (byte) 0x95, (byte) 0x04, /*        Report Count (4)                    */
                (byte) 0x91, (byte) 0x02, /*        Output                              */

                (byte) 0x09, (byte) 0x50, /*        Usage Page (Duration)              */
                (byte) 0x66, (byte) 0x10, (byte) 0x01,                                  /*        Unit (time)                               */
                (byte) 0x55, (byte) 0x0E, /*        Unit Exponent (10^-2)               */
                (byte) 0x26, (byte) 0xFF, /*        Logical Maximum (255)               */
                (byte) 0x95, (byte) 0x01, /*        Report Count (1)                    */  //no size here apparently? Might default to 1 bit?
                (byte) 0x91, (byte) 0x02, /*        Output                              */

                (byte) 0x09, (byte) 0xA7, /*        Usage Page (Start Delay)            */
                (byte) 0x91, (byte) 0x02, /*        Output                              */  //its just left like this, not sure why atm. wtf

                (byte) 0x65, (byte) 0x00, /*        Unit (time)                         */
                (byte) 0x55, (byte) 0x00, /*        Unit Exponent (10^-2)               */
                (byte) 0x09, (byte) 0x7C, /*        Usage Page (loop Count )            */
                (byte) 0x91, (byte) 0x02, /*        Output                              */
                (byte) 0xC0,              /*       End Collection                       */

                (byte) 0x05, (byte) 0x01, /*    Usage Page (Physical Interface Device)  */
                (byte) 0x09, (byte) 0x80, /*        Usage Page (Dc Actuators)           */
                (byte) 0xA1, (byte) 0x00, /*        Collection (Physical)               */
                (byte) 0x09, (byte) 0x85, /*        Usage (One Shot Control)            */
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x25, (byte) 0x01, /*        Logical Maximum (1)                 */
                (byte) 0x75, (byte) 0x01, /*        Report Size (1)                     */
                (byte) 0x95, (byte) 0x01, /*        Report Count (1)                    */
                (byte) 0x81, (byte) 0x02, /*        Input (Var)                         */
                //7-bit padding
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x25, (byte) 0x00, /*        Logical Maximum (1)                 */
                (byte) 0x75, (byte) 0x07, /*        Report Size (7)                     */
                (byte) 0x95, (byte) 0x01, /*        Report Count (1)                    */
                (byte) 0x81, (byte) 0x03, /*        Input (Const)                       */
                (byte) 0xC0,              /*       End Collection                       */

                (byte) 0x05, (byte) 0x06, /*    Usage Page (Physical Interface Device)  */
                (byte) 0x09, (byte) 0x20, /*    Usage Page (battery Strength)           */
                (byte) 0x15, (byte) 0x00, /*        Logical Minimum (0)                 */
                (byte) 0x26, (byte) 0xFF, /*        Logical Maximum (255)               */
                (byte) 0x75, (byte) 0x08, /*        Report Size (8)                     */
                (byte) 0x95, (byte) 0x01, /*        Report Count (1)                    */
                (byte) 0x81, (byte) 0x02, /*        Input (Var)                         */
                (byte) 0xC0,              /*       End Collection                       */
        };

        currentReport = new byte[16];
        subclass = BluetoothHidDevice.SUBCLASS2_GAMEPAD;
        currentReport[15] = (byte) 255; //full battery for now
    }


     /*
05 01        (GLOBAL) USAGE_PAGE         0x0001 Generic Desktop Page
09 05        (LOCAL)  USAGE              0x00010005 Game Pad (Application Collection)

A1 01        (MAIN)   COLLECTION         0x01 Application (Usage=0x00010005: Page=Generic Desktop Page, Usage=Game Pad, Type=Application Collection)
A1 00          (MAIN)   COLLECTION         0x00 Physical (Usage=0x0: Page=, Usage=, Type=) <-- Error: COLLECTION must be preceded by a USAGE <-- Warning: USAGE type should be CP (Physical Collection)
09 30            (LOCAL)  USAGE              0x00010030 X (Dynamic Value)
09 31            (LOCAL)  USAGE              0x00010031 Y (Dynamic Value)
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0)  <-- Info: Consider replacing 15 00 with 14
27 FFFF0000      (GLOBAL) LOGICAL_MAXIMUM    0x0000FFFF (65535)
95 02            (GLOBAL) REPORT_COUNT       0x02 (2) Number of fields
75 10            (GLOBAL) REPORT_SIZE        0x10 (16) Number of bits per field
81 02            (MAIN)   INPUT              0x00000002 (2 fields x 16 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
C0             (MAIN)   END_COLLECTION     Physical

A1 00          (MAIN)   COLLECTION         0x00 Physical (Usage=0x0: Page=, Usage=, Type=) <-- Error: COLLECTION must be preceded by a USAGE <-- Warning: USAGE type should be CP (Physical Collection)
09 33            (LOCAL)  USAGE              0x00010033 Rx (Dynamic Value)
09 34            (LOCAL)  USAGE              0x00010034 Ry (Dynamic Value)
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
27 FFFF0000      (GLOBAL) LOGICAL_MAXIMUM    0x0000FFFF (65535) <-- Redundant: LOGICAL_MAXIMUM is already 65535
95 02            (GLOBAL) REPORT_COUNT       0x02 (2) Number of fields <-- Redundant: REPORT_COUNT is already 2
75 10            (GLOBAL) REPORT_SIZE        0x10 (16) Number of bits per field <-- Redundant: REPORT_SIZE is already 16
81 02            (MAIN)   INPUT              0x00000002 (2 fields x 16 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
C0             (MAIN)   END_COLLECTION     Physical


05 01          (GLOBAL) USAGE_PAGE         0x0001 Generic Desktop Page <-- Redundant: USAGE_PAGE is already 0x0001
09 32          (LOCAL)  USAGE              0x00010032 Z (Dynamic Value)
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
26 FF03        (GLOBAL) LOGICAL_MAXIMUM    0x03FF (1023)
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields
75 0A          (GLOBAL) REPORT_SIZE        0x0A (10) Number of bits per field
81 02          (MAIN)   INPUT              0x00000002 (1 field x 10 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
padding
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 00          (GLOBAL) LOGICAL_MAXIMUM    0x00 (0)  <-- Info: Consider replacing 25 00 with 24
75 06          (GLOBAL) REPORT_SIZE        0x06 (6) Number of bits per field
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
81 03          (MAIN)   INPUT              0x00000003 (1 field x 6 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap


05 01          (GLOBAL) USAGE_PAGE         0x0001 Generic Desktop Page <-- Redundant: USAGE_PAGE is already 0x0001
09 35          (LOCAL)  USAGE              0x00010035 Rz (Dynamic Value)
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
26 FF03        (GLOBAL) LOGICAL_MAXIMUM    0x03FF (1023)
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
75 0A          (GLOBAL) REPORT_SIZE        0x0A (10) Number of bits per field
81 02          (MAIN)   INPUT              0x00000002 (1 field x 10 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
padding
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 00          (GLOBAL) LOGICAL_MAXIMUM    0x00 (0)  <-- Info: Consider replacing 25 00 with 24
75 06          (GLOBAL) REPORT_SIZE        0x06 (6) Number of bits per field
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
81 03          (MAIN)   INPUT              0x00000003 (1 field x 6 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap


05 09          (GLOBAL) USAGE_PAGE         0x0009 Button Page
19 01          (LOCAL)  USAGE_MINIMUM      0x00090001 Button 1 Primary/trigger (Selector, On/Off Control, Momentary Control, or One Shot Control)
29 0A          (LOCAL)  USAGE_MAXIMUM      0x0009000A Button 10 (Selector, On/Off Control, Momentary Control, or One Shot Control)
95 0A          (GLOBAL) REPORT_COUNT       0x0A (10) Number of fields
75 01          (GLOBAL) REPORT_SIZE        0x01 (1) Number of bits per field
81 02          (MAIN)   INPUT              0x00000002 (10 fields x 1 bit) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 00          (GLOBAL) LOGICAL_MAXIMUM    0x00 (0) <-- Redundant: LOGICAL_MAXIMUM is already 0 <-- Info: Consider replacing 25 00 with 24
75 06          (GLOBAL) REPORT_SIZE        0x06 (6) Number of bits per field
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields
81 03          (MAIN)   INPUT              0x00000003 (1 field x 6 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
*/
    /*

05 01          (GLOBAL) USAGE_PAGE         0x0001 Generic Desktop Page
09 39          (LOCAL)  USAGE              0x00010039 Hat switch (Dynamic Value)
15 01          (GLOBAL) LOGICAL_MINIMUM    0x01 (1)
25 08          (GLOBAL) LOGICAL_MAXIMUM    0x08 (8)
35 00          (GLOBAL) PHYSICAL_MINIMUM   0x00 (0)  <-- Info: Consider replacing 35 00 with 34
46 3B01        (GLOBAL) PHYSICAL_MAXIMUM   0x013B (315)
66 1400        (GLOBAL) UNIT               0x0014 Rotation in degrees [1° units] (4=System=English Rotation, 1=Rotation=Degrees)  <-- Info: Consider replacing 66 1400 with 65 14
75 04          (GLOBAL) REPORT_SIZE        0x04 (4) Number of bits per field
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
81 42          (MAIN)   INPUT              0x00000042 (1 field x 4 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 1=Null 0=NonVolatile 0=Bitmap

75 04          (GLOBAL) REPORT_SIZE        0x04 (4) Number of bits per field <-- Redundant: REPORT_SIZE is already 4
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0)  <-- Info: Consider replacing 15 00 with 14
25 00          (GLOBAL) LOGICAL_MAXIMUM    0x00 (0)  <-- Info: Consider replacing 25 00 with 24
35 00          (GLOBAL) PHYSICAL_MINIMUM   0x00 (0) <-- Redundant: PHYSICAL_MINIMUM is already 0 <-- Info: Consider replacing 35 00 with 34
45 00          (GLOBAL) PHYSICAL_MAXIMUM   0x00 (0)  <-- Info: Consider replacing 45 00 with 44
65 00          (GLOBAL) UNIT               0x00 No unit (0=None)  <-- Info: Consider replacing 65 00 with 64
81 03          (MAIN)   INPUT              0x00000003 (1 field x 4 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
A1 02          (MAIN)   COLLECTION         0x02 Logical (Usage=0x0: Page=, Usage=, Type=) <-- Error: COLLECTION must be preceded by a USAGE <-- Warning: USAGE type should be CL (Logical Collection)


05 0F            (GLOBAL) USAGE_PAGE         0x000F Physical Interface Device Page
09 97            (LOCAL)  USAGE              0x000F0097 DC Enable Actuators (Selector)
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 01            (GLOBAL) LOGICAL_MAXIMUM    0x01 (1)
75 04            (GLOBAL) REPORT_SIZE        0x04 (4) Number of bits per field <-- Redundant: REPORT_SIZE is already 4
95 01            (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
91 02            (MAIN)   OUTPUT             0x00000002 (1 field x 4 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 00            (GLOBAL) LOGICAL_MAXIMUM    0x00 (0)  <-- Info: Consider replacing 25 00 with 24
91 03            (MAIN)   OUTPUT             0x00000003 (1 field x 4 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

09 70            (LOCAL)  USAGE              0x000F0070 Magnitude (Dynamic Value)
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 64            (GLOBAL) LOGICAL_MAXIMUM    0x64 (100)
75 08            (GLOBAL) REPORT_SIZE        0x08 (8) Number of bits per field
95 04            (GLOBAL) REPORT_COUNT       0x04 (4) Number of fields
91 02            (MAIN)   OUTPUT             0x00000002 (4 fields x 8 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

09 50            (LOCAL)  USAGE              0x000F0050 Duration (Dynamic Value)
66 0110          (GLOBAL) UNIT               0x1001 Time in seconds [1 s units] (1=System=SI Linear, 1=Time=Seconds)
55 0E            (GLOBAL) UNIT_EXPONENT      0x0E (Unit Value x 10⁻²)
26 FF00          (GLOBAL) LOGICAL_MAXIMUM    0x00FF (255)
95 01            (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields
91 02            (MAIN)   OUTPUT             0x00000002 (1 field x 8 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

09 A7            (LOCAL)  USAGE              0x000F00A7 Start Delay (Dynamic Value)
91 02            (MAIN)   OUTPUT             0x00000002 (1 field x 8 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

65 00            (GLOBAL) UNIT               0x00 No unit (0=None)  <-- Info: Consider replacing 65 00 with 64
55 00            (GLOBAL) UNIT_EXPONENT      0x00 (Unit Value x 10⁰)  <-- Info: Consider replacing 55 00 with 54
09 7C            (LOCAL)  USAGE              0x000F007C Loop Count (Dynamic Value)
91 02            (MAIN)   OUTPUT             0x00000002 (1 field x 8 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
C0             (MAIN)   END_COLLECTION     Logical
  */
    /*
05 01          (GLOBAL) USAGE_PAGE         0x0001 Generic Desktop Page
09 80          (LOCAL)  USAGE              0x00010080 System Control (Application Collection)
A1 00          (MAIN)   COLLECTION         0x00 Physical (Usage=0x00010080: Page=Generic Desktop Page, Usage=System Control, Type=Application Collection) <-- Warning: USAGE type should be CP (Physical Collection)
09 85            (LOCAL)  USAGE              0x00010085 System Main Menu (One Shot Control)
15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 01            (GLOBAL) LOGICAL_MAXIMUM    0x01 (1)
95 01            (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
75 01            (GLOBAL) REPORT_SIZE        0x01 (1) Number of bits per field
81 02            (MAIN)   INPUT              0x00000002 (1 field x 1 bit) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap

15 00            (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
25 00            (GLOBAL) LOGICAL_MAXIMUM    0x00 (0)  <-- Info: Consider replacing 25 00 with 24
75 07            (GLOBAL) REPORT_SIZE        0x07 (7) Number of bits per field
95 01            (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
81 03            (MAIN)   INPUT              0x00000003 (1 field x 7 bits) 1=Constant 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
C0             (MAIN)   END_COLLECTION     Physical

05 06          (GLOBAL) USAGE_PAGE         0x0006 Generic Device Controls Page
09 20          (LOCAL)  USAGE              0x00060020 Battery Strength (Dynamic Value)
15 00          (GLOBAL) LOGICAL_MINIMUM    0x00 (0) <-- Redundant: LOGICAL_MINIMUM is already 0 <-- Info: Consider replacing 15 00 with 14
26 FF00        (GLOBAL) LOGICAL_MAXIMUM    0x00FF (255)
75 08          (GLOBAL) REPORT_SIZE        0x08 (8) Number of bits per field
95 01          (GLOBAL) REPORT_COUNT       0x01 (1) Number of fields <-- Redundant: REPORT_COUNT is already 1
81 02          (MAIN)   INPUT              0x00000002 (1 field x 8 bits) 0=Data 1=Variable 0=Absolute 0=NoWrap 0=Linear 0=PrefState 0=NoNull 0=NonVolatile 0=Bitmap
C0           (MAIN)   END_COLLECTION     Application
*/

    String TAG = "DIGamepad";

    //TEMPORARY INDEXES, there is no standardisation, what did i expect from this world
    public static int X_BUTTON_INDEX = 0;

    public static int Y_BUTTON_INDEX = 1;

    public static int A_BUTTON_INDEX = 2;

    public static int B_BUTTON_INDEX = 3;

    public void toggleButton(int buttonIndex,int value){
        //currentReport[0] |= value << buttonIndex;          //sets bit //cool bitwise operation ;) thanks real-time embedded systems
        currentReport[8] = (byte) ((currentReport[8] & ~(1 << buttonIndex)) | (value << buttonIndex)); //bit clear and then bit set, only way i could think of doin it all in one
        makeReport(true);


    }

    int midPoint = 665535/2;
    public void moveThumbStick(int x,int y,boolean lt){

        x = x*10 + midPoint;
        y = y*10 + midPoint;

//        x = (int) (Math.cos(Math.toRadians(angle))*boundary*(strength/100.0));
//        y = - (int) (Math.sin(Math.toRadians(angle))*boundary*(strength/100.0));

        Log.d(TAG, "X = "+x+" | Y = "+y);

        byte[] xB = intToBytes(x);
        currentReport[0] = xB[0];
        currentReport[1] = xB[1];
        byte[] yB = intToBytes(y);
        currentReport[2] = yB[0];
        currentReport[3] = yB[1];
        makeReport(false,lt);
    }

}
