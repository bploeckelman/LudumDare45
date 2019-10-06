package lando.systems.ld45.state;

import java.math.BigInteger;

public class PlayerState {

    public BigInteger score = BigInteger.ZERO;

    public int pegLevel = 0;
    public int pegs = 0;
    public int spinnerLevel = 0;
    public int spinners = 0;
    public int bumperLevel = 0;
    public int bumpers;
    public int graphicsPack = 0;
    public int balls = 5;

    public void addScore(long value) {
        score.add(BigInteger.valueOf(value));
    }
}
