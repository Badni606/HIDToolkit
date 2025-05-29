package Matthew.comp3200.UI.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import Matthew.comp3200.Network.HidUtil;
import Matthew.comp3200.R;

public class ConnectionView extends LinearLayout {

    public ConnectionView(Context context){
        this(context,null);
    }
    public ConnectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ConnectionView(Context context, @Nullable AttributeSet attrs,int defStyle) {
        this(context, attrs);
    }

    Paint circlePaint;
    TextView textView;
    ImageButton reconnectBtn;

    int dcColour = ContextCompat.getColor(getContext(), R.color.connection_false);
    int cColour = ContextCompat.getColor(getContext(), R.color.connection_true);
    int rcColor = ContextCompat.getColor(getContext(), R.color.connection_reconnecting);
    int circleColor = rcColor; // Default color

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        setWillNotDraw(false); //needed for view groups else it won't draw at all

        textView = new TextView(getContext());
        textView.setText("Connecting");
        addView(textView);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        reconnectBtn = (ImageButton) inflater.inflate(R.layout.reconnect_button,this,false);
        reconnectBtn.setOnClickListener(v -> {
            reconnecting();
            Log.d("Connection UI", "Refresh button pressed");
        });
        addView(reconnectBtn);//,buttonParams
        reconnectBtn.setVisibility(View.GONE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        textView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//        reconnectBtn.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        // Update the measured dimensions of this view
        setMeasuredDimension(
                textView.getMeasuredWidth() + 100 + 60, // Width of text view + size of circle
                Math.max(textView.getMeasuredHeight(), 100) // Height of text view or minimum size
        );


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // Position the text view
        textView.layout(
                100, // Left offset for the circle
                (b - t - textView.getMeasuredHeight()) / 2, // Vertical centering
                100 + textView.getMeasuredWidth(), // Right offset for the circle
                (b - t + textView.getMeasuredHeight()) / 2 // Vertical centering
        );
        reconnectBtn.layout(
                100+ textView.getMeasuredWidth()+10, // Left offset for the circle
                (b - t - 50) / 2, // Vertical centering
                100 + textView.getMeasuredWidth()+60, // Right offset for the circle
                (b - t + 50) / 2 // Vertical centering
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(50, getHeight() / 2, 25, circlePaint); // Adjust circle parameters as needed
    }

    void setCircleColor(int color) {
        circleColor = color;
        circlePaint.setColor(circleColor);
        invalidate(); // Redraw the view
    }
    void setText(String text) {
        textView.setText(text);
    }

    boolean reconnecting = false;

    public void connectionChanged(boolean change){
        reconnecting = false;
        if(change){
            setCircleColor(cColour);
            setText("Connected");
            reconnectBtn.setVisibility(View.GONE);
        }
        else{
            setCircleColor(dcColour);
            setText("Disconnected");
            reconnectPrompt();
        }
        invalidate();
    }

    void reconnectPrompt(){
        reconnectBtn.setVisibility(View.VISIBLE);
    }

    void reconnecting(){
        if (reconnecting){
            return;
        }
        setText("Reconnecting");
        setCircleColor(rcColor);
        //try reconnect
        HidUtil.connect();
        reconnecting = true;
        reconnectBtn.setVisibility(View.GONE);
    }




}
