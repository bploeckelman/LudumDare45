package lando.systems.ld45.state;

import lando.systems.ld45.utils.ArtPack;

import java.math.BigInteger;

public class PlayerState {

    // Points used for buying shit
    public long score = 0;

    // For the credits screen, to show how many points you gathered overall
    private long totalScore = 0;

    public int pegLevel = 0;
    public int pegs = 0;
    public int spinnerLevel = 0;
    public int spinners = 0;
    public int bumperLevel = 0;
    public int bumpers;
    public int graphicsPack = 0;
    public int balls = 5;

    public void addScore(long value) {
        score += value;
        totalScore += value;
    }

    public long getTotalScore() {
        return totalScore;
    }
}
