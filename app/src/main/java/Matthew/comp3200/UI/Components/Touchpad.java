package Matthew.comp3200.UI.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//TODO much to improve!
/*
    - add direction changing
    - add scrolling via two fingers
    - add double tapping = left click
 */
public class Touchpad extends View {

    String TAG = "Touchpad";
    public interface OnMoveListener{
        void onMove(int x,int y,boolean lt); //,boolean lClick,boolean rClick,int wheel
        void onCLick(boolean lClick,boolean rClick);
        void onScroll(int vScroll,int hScroll,boolean lt);
        void onZoom(int zoom,boolean lt);
    }

    boolean lastTransmission = false;
    OnMoveListener listener;

//    class Pointer{
//        int xOrigin;
//        int x;
//        int yOrigin;
//        int y;
//        int xDif;
//        int yDif;
//    }
    Pointer p0 = new Pointer();
    Pointer p1 = new Pointer();

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable resetTouchCounter = () -> {
        touches = 0;
//        goingToDrag = false;
//        dragging = false;
    };

    Runnable resetDrag = () -> {
        goingToDrag = false;
        dragging = false;
    };

    public Touchpad(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public void setOnMoveListener(OnMoveListener onMoveListener){
        listener = onMoveListener;
    }

    int touches =0;
    boolean moved = false;
    boolean rClick = false;
    boolean lClick = false;
    boolean zooming;
    boolean scrolling;
    boolean goingToDrag = false;
    boolean dragging = false;
    int doubleTapDelay = 300;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            //first finger touches screen
            case MotionEvent.ACTION_DOWN:
//                reset(p0,(int)event.getX(),(int) event.getY());
                p0.reset((int)event.getX(),(int) event.getY());
                lClick = true;
                //todo start countdown
                touches ++;
                if(touches == 2){
                    listener.onCLick(true,false);
//                    dragging = goingToDrag;
//                    goingToDrag = true;
                }
                else{
                    executor.schedule(resetTouchCounter,doubleTapDelay, TimeUnit.MILLISECONDS);
                }
                break;
            //second finger touches screen
            case MotionEvent.ACTION_POINTER_DOWN:
//                reset(p1,(int)event.getX(1),(int) event.getY(1));
                p1.reset((int)event.getX(1),(int) event.getY(1));
                rClick = true;
                break;

            case MotionEvent.ACTION_MOVE:
                //when only moving the cursor
                if(event.getPointerCount() == 1){
                    p0.x = (int) event.getX();
                    p0.y = (int) event.getY();
                    moved = true;
                    moveCursor();
                    //should then reset origin to this new coordinate since we are sending values relative to new pos every update
//                    reset(p0, p0.x, p0.y);
                    p0.reset(p0.x,p0.y);
//                    lClick = touches == 2?true:false;
//                    lClick = false;
//                    touches = lClick?2:0;
                }
                //when both fingers move. Could be pinching or scrolling
                else if(event.getPointerCount() == 2){
                    rClick = false;
                    p0.x = (int) event.getX();
                    p0.y = (int) event.getY();
                    p1.x = (int) event.getX(1);
                    p1.y = (int) event.getY(1);
//                    vectorDistance(p0);
//                    vectorDistance(p1);
                    p0.vectorDistance();
                    p1.vectorDistance();

                    //if pointers are going in opposite direction then its a zoom operation
                    if((p0.yDif ^ p1.yDif) < 0){// might need to add a slight threshold to make it less sensitive
                        zooming = true;
                        //identify leftmost pointer, todo: should need to be done only once optimally
                        Pointer left = (p0.xOrigin<p1.xOrigin)?p0:p1;
                        Pointer right = (left == p0)?p1:p0;
                        float x = -((left.xDif + (-right.xDif)) / 2.0f);
                        float y = ((left.yDif + (-right.yDif)) / 2.0f);
                        listener.onZoom((int)((x+y)/2.0f),false);   //take average
//                        reset(p0,p0.x,p0.y);
//                        reset(p1,p1.x,p1.y);
                        p0.reset(p0.x,p0.y);
                        p1.reset(p1.x,p1.y);
                        listener.onZoom(0,false);
                    }
                    //scrolling is occurring
                    else{
                        scrolling = true;
                        //averages
                        int v = (p0.yDif + p1.yDif)/8;//was 2
                        int h = (p0.xDif + p1.xDif)/4;
                        listener.onScroll(v,-h,false);
//
//                        reset(p0,p0.x,p0.y);
//                        reset(p1,p1.x,p1.y);
                        p0.reset(p0.x,p0.y);
                        p1.reset(p1.x,p1.y);
//                        listener.onScroll(0,0,false);
                    }
                    //potential to add 3 finger operations like window swapping
                }
                break;

            //second finger lifted - no use at the moment
//            case MotionEvent.ACTION_POINTER_UP:
//                break;
            //last finger is lifted off screen, we do checking here
            case MotionEvent.ACTION_UP:
                if(moved){
//                    reset(p0,0,0);
                    p0.reset(0,0);
                    lastTransmission = true;
                    moveCursor();
//                    listener.onMove(p0.xDif, p0.yDif,true);
                    lastTransmission = false;
                    listener.onCLick(false,false);  //in case there was a dragging operation going on
                    touches = 0;
//                    lClick = false;
//                    goingToDrag = !dragging;
//                    dragging = false;
                    Log.d(TAG, "Movement Detected");
                }
                else if (scrolling){
                    listener.onScroll(0,0, true);
                    scrolling = false;
                    Log.d(TAG, "Scrolling Detected");
                }
                else if (zooming){
                    listener.onZoom(0, true);
                    zooming = false;
                    Log.d(TAG, "Zooming Detected");
                }
                else {
                    if(lClick){//&& !goingToDrag
                        listener.onCLick(true,false);
                        listener.onCLick(false,false);
                        Log.d(TAG, "Left click Detected");
                    }
                    if(rClick){
                        listener.onCLick(false,true);
                        listener.onCLick(false,false);
                        Log.d(TAG, "Right click Detected");
                    }
                }
                lClick = false;
                rClick = false;
                zooming = false;
                moved = false;
                scrolling = false;
                break;
        }

        return true;
    }

    void moveCursor(){
//        vectorDistance(p0);
        p0.vectorDistance();
        listener.onMove(p0.xDif, p0.yDif,lastTransmission);
    }

//    void reset(Pointer pointer,int newXVal,int newYVal){
//        pointer.xOrigin = newXVal;
//        pointer.x = newXVal;
//        pointer.yOrigin = newYVal;
//        pointer.y = newYVal;
//    }
//
//    void vectorDistance(Pointer p){
//        //1. check if pointer is still in view's bounds
//        p.x = Math.max(0,p.x);
//        p.y = Math.max(0,p.y);
//        //2. calculate distance between origin cords and current cords
//        p.xDif = Math.max(-127,Math.min(p.x - p.xOrigin,127));
//        p.yDif = Math.max(-127,Math.min(p.y - p.yOrigin,127));
//    }

}
