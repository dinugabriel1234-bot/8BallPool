package app.hack.eightballpool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GuidelineSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private DrawThread drawThread;
    private Line[] currentLines;

    public GuidelineSurfaceView(Context context) {
        super(context);
        init();
    }

    public GuidelineSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
    }

    public void setLines(Line[] lines) {
        this.currentLines = lines;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(holder, this);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) { }
        }
    }

    private static class DrawThread extends Thread {
        private boolean running = false;
        private SurfaceHolder holder;
        private GuidelineSurfaceView view;
        private Paint paint = new Paint();

        public DrawThread(SurfaceHolder holder, GuidelineSurfaceView view) {
            this.holder = holder;
            this.view = view;
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(6);
            paint.setAntiAlias(true);
        }

        public void setRunning(boolean running) { this.running = running; }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    if (view.currentLines != null) {
                        for (Line line : view.currentLines) {
                            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint);
                        }
                    }
                    holder.unlockCanvasAndPost(canvas);
                }

                try { Thread.sleep(1000 / 120); } catch (InterruptedException e) { }
            }
        }
    }
}
