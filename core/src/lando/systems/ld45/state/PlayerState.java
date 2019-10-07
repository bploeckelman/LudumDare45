package lando.systems.ld45.state;

import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.objects.GameObject;
import lando.systems.ld45.utils.ArtPack;

import java.math.BigInteger;

public class PlayerState {

    // Points used for buying shit
    public long score = 0;

    // For the credits screen, to show how many points you gathered overall
    private long totalScore = 0;

    public int pegs = 0;
    public int leftSpinners = 0;
    public int rightSpinners;
    public int bumpers = 0;
    public int balls = 5;
    public ArtPack artPack;

    public Array<GameObject> gameObjects;

    public PlayerState(){
        gameObjects = new Array<>();
        artPack = ArtPack.a;
    }

    public void addScore(long value) {
        score += value;
        totalScore += value;
    }

    public long getTotalScore() {
        return totalScore;
    }
}
