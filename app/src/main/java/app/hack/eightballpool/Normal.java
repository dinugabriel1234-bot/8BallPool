package app.hack.eightballpool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class Normal extends GuidelineSurfaceView {

    private float xCircle = 500, yCircle = 500;

    public Normal(Context context) { super(context); init(); }
    public Normal(Context context, AttributeSet attrs) { super(context, attrs); init(); }

    private void init() {
        setLines(new Line[]{
            new Line(xCircle, yCircle, 0, 0),
            new Line(xCircle, yCircle, getWidth(), 0)
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                xCircle = event.getX();
                yCircle = event.getY();

                setLines(new Line[]{
                    new Line(xCircle, yCircle, getWidth() / 2f, 0),
                    new Line(xCircle, yCircle, getWidth() / 2f, getHeight())
                });
                break;
        }
        return true;
    }
}
