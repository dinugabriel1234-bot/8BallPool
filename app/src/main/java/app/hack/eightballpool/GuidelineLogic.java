package app.hack.eightballpool;

import java.util.List;

public class GuidelineLogic {

    private float cueX, cueY;
    private List<Float> ballX, ballY;
    private List<Float> pocketX, pocketY;

    public void updatePositions(float cueX, float cueY,
                                List<Float> ballX, List<Float> ballY,
                                List<Float> pocketX, List<Float> pocketY) {
        this.cueX = cueX;
        this.cueY = cueY;
        this.ballX = ballX;
        this.ballY = ballY;
        this.pocketX = pocketX;
        this.pocketY = pocketY;
    }

    public float getCueX() { return cueX; }
    public float getCueY() { return cueY; }
    public List<Float> getBallX() { return ballX; }
    public List<Float> getBallY() { return ballY; }
    public List<Float> getPocketX() { return pocketX; }
    public List<Float> getPocketY() { return pocketY; }
}
