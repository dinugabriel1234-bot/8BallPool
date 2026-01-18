package app.hack.eightballpool;

import android.content.Context;
import android.graphics.Bitmap;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.common.FileUtil;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AIObjectDetector {

    private Interpreter tflite;

    public AIObjectDetector(Context context, String modelPath) {
        try {
            MappedByteBuffer buffer = FileUtil.loadMappedFile(context, modelPath);
            tflite = new Interpreter(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DetectionResult> detectObjects(Bitmap bitmap) {
        List<DetectionResult> results = new ArrayList<>();

        TensorImage image = TensorImage.fromBitmap(bitmap);

        TensorBuffer outputLocations = TensorBuffer.createFixedSize(new int[]{1,10,4}, org.tensorflow.lite.DataType.FLOAT32);
        TensorBuffer outputClasses   = TensorBuffer.createFixedSize(new int[]{1,10}, org.tensorflow.lite.DataType.FLOAT32);
        TensorBuffer outputScores    = TensorBuffer.createFixedSize(new int[]{1,10}, org.tensorflow.lite.DataType.FLOAT32);
        TensorBuffer numDetections   = TensorBuffer.createFixedSize(new int[]{1}, org.tensorflow.lite.DataType.FLOAT32);

        Object[] inputArray = {image.getBuffer()};
        Map<Integer,Object> outputMap = new HashMap<>();
        outputMap.put(0, outputLocations.getBuffer().rewind());
        outputMap.put(1, outputClasses.getBuffer().rewind());
        outputMap.put(2, outputScores.getBuffer().rewind());
        outputMap.put(3, numDetections.getBuffer().rewind());

        tflite.runForMultipleInputsOutputs(inputArray, outputMap);

        int detections = (int) numDetections.getFloatValue(0);
        for (int i=0; i<detections; i++) {
            float score = outputScores.getFloatArray()[i];
            if (score < 0.5f) continue;

            float[] loc = new float[4];
            System.arraycopy(outputLocations.getFloatArray(), i*4, loc, 0, 4);
            int classId = (int) outputClasses.getFloatArray()[i];

            results.add(new DetectionResult(classId, score, loc));
        }

        return results;
    }

    public static class DetectionResult {
        public int classId;
        public float score;
        public float[] location;

        public DetectionResult(int classId, float score, float[] location) {
            this.classId = classId;
            this.score = score;
            this.location = location;
        }
    }
}
