package lando.systems.ld45.state;

import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.objects.Bumper;
import lando.systems.ld45.objects.GameObject;
import lando.systems.ld45.objects.Peg;
import lando.systems.ld45.objects.Spinner;
import lando.systems.ld45.utils.ArtPack;

import java.math.BigInteger;

public class PlayerState {

    // Points used for buying shit
    public long score = 0;

    // For the credits screen, to show how many points you gathered overall
    private long totalScore = 0;
    public long ballsDropped = 0;
    public long totalTime = 0;

    public int pegs = 0;
    public int leftSpinners = 10;
    public int rightSpinners = 10;
    public int bumpers = 0;
    public int balls = 5;
    public boolean screenShakeUnlocked = true;
    public ArtPack artPack;

    public Array<GameObject> gameObjects;

    public PlayerState() {
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

    public int getCurrentPegs() {
        int count = 0;
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Peg) count++;
        }
        return count;
    }


    public int getCurrentBumpers() {
        int count = 0;
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Bumper) count++;
        }
        return count;
    }

    public int getCurrentLeftSpinner() {
        int count = 0;
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Spinner) {
                Spinner spinner = (Spinner) gameObject;
                if (spinner.left) count++;
            }
        }
        return count;
    }

    public int getCurrentRightSpinner() {
        int count = 0;
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Spinner) {
                Spinner spinner = (Spinner) gameObject;
                if (!spinner.left) count++;
            }
        }
        return count;
    }

    public boolean canBuildPeg() {
        return getCurrentPegs() < pegs;
    }

    public boolean canBuildBumper() {
        return getCurrentBumpers() < bumpers;
    }

    public boolean canBuildLeftSpinner() {
        return getCurrentLeftSpinner() < leftSpinners;
    }

    public boolean canBuildRightSpinner() {
        return getCurrentRightSpinner() < rightSpinners;
    }

    public boolean canBuildSomething() {
        return canBuildPeg() || canBuildBumper() || canBuildRightSpinner() || canBuildLeftSpinner();
    }

    public boolean canUpgradeSomething() {
        return true;
    }
}
