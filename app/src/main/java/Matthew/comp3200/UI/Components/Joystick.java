package Matthew.comp3200.UI.Components;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import Matthew.comp3200.R;

public class Joystick extends View{

    Paint bkgrdBorderPaint = new Paint();
    Paint stickPaint = new Paint();

    public Joystick(Context context, @Nullable AttributeSet attrs,int defStyle){
        this(context,attrs);
    }
    public Joystick(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bkgrdBorderPaint.setColor(ContextCompat.getColor(getContext(),R.color.joy_grey));
        bkgrdBorderPaint.setStyle(Paint.Style.STROKE);
        bkgrdBorderPaint.setAntiAlias(true);
        bkgrdBorderPaint.setStrokeWidth(5);

        stickPaint.setColor(ContextCompat.getColor(getContext(),R.color.joy_grey));
        stickPaint.setStyle(Paint.Style.FILL);
        stickPaint.setAntiAlias(true);
    }
    public Joystick(Context context){
        this(context,null);
    }

    public interface OnMoveListener{
        void onMove(int x,int y,boolean lt);
    }

    OnMoveListener listener;
    //used instead of Pointer because vector calculation method differs, based on radius of joystick
    int xOrigin;
    int yOrigin;
    int currentX;
    int currentY;


    public void setOnMoveListener(OnMoveListener onMoveListener){
        listener = onMoveListener;
    }


    boolean lastTouch = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                reset((int)event.getX(),(int)event.getY());
                drawPoint();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (int) event.getX();
                currentY = (int) event.getY();
                drawPoint();
                calculateVector();
                break;
            case MotionEvent.ACTION_UP:
                reset(centre,centre);
                drawPoint();
                lastTouch = true;
                calculateVector();
                lastTouch = false;
                break;
        }

        return true;
    }

    float sensitivity = 2.0f;
    void calculateVector(){
        int xDif;
        int yDif;
        currentX = Math.max(0,currentX);
        currentY = Math.max(0,currentY);
//        xDif = (int) Math.max(-127,Math.min((currentX - xOrigin)/sensitivity,127));
        xDif = (int) Math.max(-bkgrdRad,Math.min((currentX - xOrigin),bkgrdRad));//sensitivity
        xDif = (int) (xDif/bkgrdRad * 127);
//        yDif = (int) Math.max(-127,Math.min((currentY - yOrigin)/sensitivity,127));
        yDif = (int) Math.max(-bkgrdRad,Math.min((currentY - yOrigin),bkgrdRad));//sensitivity
        yDif = (int) (yDif/bkgrdRad * 127);
        listener.onMove(xDif,yDif,lastTouch);
    }

    void reset(int newXVal,int newYVal){
        xOrigin= newXVal;
        currentX= newXVal;
        yOrigin= newYVal;
        currentY= newYVal;
    }

    void drawPoint(){
        //makes view call onPaint again :D
        invalidate();
    }
    float viewWidth;
    float viewHeight;
    int centre;
    float bkgrdRad;
    float btnRad;

    //this is called during layout so we can get measurements here
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = getWidth();
        viewHeight = getHeight();
        //the joystick view is 3cmx3cm but pixel density varies by phone so I have to make it relative to the number of pixels
        centre = (int)(viewWidth/2.0);
        bkgrdRad = viewHeight * 0.4f;
        xOrigin = centre;
        yOrigin = centre;
        currentX = xOrigin;
        currentY = yOrigin;
        btnRad = viewHeight * 0.1f;
    }



    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        //Makes the whole joystick originate where first touched
        //bkgrd
        canvas.drawCircle(xOrigin,yOrigin,bkgrdRad, bkgrdBorderPaint);

        //button
        double vectorLength = Math.sqrt(Math.pow(currentX-xOrigin,2) + Math.pow(currentY-yOrigin,2));
        float bX=currentX;
        float bY=currentY;
        //for calculating out of bounds drags
        if(vectorLength > bkgrdRad){
            bX -= xOrigin;
            bY -= yOrigin;
            bX *= bkgrdRad/vectorLength;
            bY *= bkgrdRad/vectorLength;
            bX += xOrigin;
            bY += yOrigin;
        }
        canvas.drawCircle(bX,bY,btnRad, stickPaint);
    }


    public int getCurrentX() {
        return currentX;
    }
}
