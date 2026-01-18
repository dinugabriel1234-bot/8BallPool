package app.hack.eightballpool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.List;

public class Normal extends View {

    private Paint linePaint, circlePaint;
    private GuidelineLogic logic;

    public Normal(Context context) {
        super(context);
        init();
    }

    public Normal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(6);
        linePaint.setAntiAlias(true);

        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(6);
        circlePaint.setAntiAlias(true);
    }

    public void setGuidelineLogic(GuidelineLogic logic) {
        this.logic = logic;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (logic == null) return;

        float cueX = logic.getCueX();
        float cueY = logic.getCueY();
        List<Float> ballsX = logic.getBallX();
        List<Float> ballsY = logic.getBallY();
        List<Float> pocketsX = logic.getPocketX();
        List<Float> pocketsY = logic.getPocketY();

        // Desenează linii cue → bile → găuri
        if (ballsX != null && ballsY != null && pocketsX != null && pocketsY != null) {
            for (int i=0; i<ballsX.size(); i++) {
                float bx = ballsX.get(i);
                float by = ballsY.get(i);
                canvas.drawLine(cueX, cueY, bx, by, linePaint);

                for (int j=0; j<pocketsX.size(); j++) {
                    canvas.drawLine(bx, by, pocketsX.get(j), pocketsY.get(j), linePaint);
                }
                canvas.drawCircle(bx, by, 20, circlePaint);
            }
        }
    }
}
