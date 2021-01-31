package database.DTO;

public class Validate {
    private int score;
    private int totalQuestions;
    private float percentageAccuracy;

    @Override
    public String toString() {
        return "Validate{" +
                "score=" + score +
                ", totalQuestions=" + totalQuestions +
                ", percentageAccuracy=" + percentageAccuracy +
                '}';
    }

    public Validate(){
        this.score=0;
        this.totalQuestions=0;
        this.percentageAccuracy= (float) 0.0;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public float getPercentageAccuracy() {
        return percentageAccuracy;
    }

    public void setPercentageAccuracy(float percentageAccuracy) {
        this.percentageAccuracy = percentageAccuracy;
    }
}
