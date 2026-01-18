package app.hack.eightballpool;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewService extends Service {

    private Normal normalView;
    private GuidelineLogic guidelineLogic;
    private AIObjectDetector detector;
    private ExecutorService aiExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();
        guidelineLogic = new GuidelineLogic();

        normalView = new Normal(this);
        normalView.setGuidelineLogic(guidelineLogic);

        // Adaugă overlay
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(normalView, new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                -3
        ));

        // Încarcă model TFLite
        detector = new AIObjectDetector(this, "pool_detector.tflite");

        // Start detecție continuă (poți înlocui cu Camera2 sau screenshot)
        startDetectionLoop();
    }

    private void startDetectionLoop() {
        aiExecutor.submit(() -> {
            while (true) {
                Bitmap frame = captureFrame(); // aici implementare Camera2 sau screenshot
                runDetection(frame);

                try { Thread.sleep(33); } // ~30 FPS
                catch (InterruptedException e) { break; }
            }
        });
    }

    private void runDetection(Bitmap frame) {
        List<AIObjectDetector.DetectionResult> results = detector.detectObjects(frame);

        float cueX=0, cueY=0;
        List<Float> ballX = new java.util.ArrayList<>();
        List<Float> ballY = new java.util.ArrayList<>();
        List<Float> pocketX = new java.util.ArrayList<>();
        List<Float> pocketY = new java.util.ArrayList<>();

        for (AIObjectDetector.DetectionResult r : results) {
            switch(r.classId) {
                case 0: // bile
                    ballX.add((r.location[0]+r.location[2])/2f);
                    ballY.add((r.location[1]+r.location[3])/2f);
                    break;
                case 1: // găuri
                    pocketX.add((r.location[0]+r.location[2])/2f);
                    pocketY.add((r.location[1]+r.location[3])/2f);
                    break;
                case 2: // tac
                    cueX = (r.location[0]+r.location[2])/2f;
                    cueY = (r.location[1]+r.location[3])/2f;
                    break;
            }
        }

        // Update UI pe UI thread
        normalView.post(() -> {
            guidelineLogic.updatePositions(cueX, cueY, ballX, ballY, pocketX, pocketY);
            normalView.invalidate();
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
