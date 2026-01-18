package app.hack.eightballpool;

public class GuidelineLogic {

    private float cueX, cueY;
    private float[] targetX, targetY;

    public void updatePositions(float cueX, float cueY, float[] targetX, float[] targetY) {
        this.cueX = cueX;
        this.cueY = cueY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public Line[] calculateLines() {
        if (targetX == null || targetY == null) return new Line[0];
        Line[] lines = new Line[targetX.length];
        for (int i = 0; i < targetX.length; i++) {
            lines[i] = new Line(cueX, cueY, targetX[i], targetY[i]);
        }
        return lines;
    }
}
