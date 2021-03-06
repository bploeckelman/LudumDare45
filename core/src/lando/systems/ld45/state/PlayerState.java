package lando.systems.ld45.state;

import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.objects.Bumper;
import lando.systems.ld45.objects.GameObject;
import lando.systems.ld45.objects.Peg;
import lando.systems.ld45.objects.Spinner;
import lando.systems.ld45.utils.ArtPack;

public class PlayerState {

    // Points used for buying shit
    public long score = 0;

    // For the credits screen, to show how many points you gathered overall
    private long totalScore = 0;
    public long ballsDropped = 0;
    public double totalTime = 0;

    public int pegs = 0;
    public int leftSpinners = 0;
    public int rightSpinners = 0;
    public int bumpers = 0;
    public int balls = 5;
    public ArtPack artPack;
    public int soundPack = 0;

    public boolean hasEffectParticles   = false;
    public boolean hasEffectTrails      = false;
    public boolean hasEffectScreenshake = false;

    public int cashMultiplier = 1;


    public Array<GameObject> gameObjects;

    public PlayerState() {
        gameObjects = new Array<>();
        artPack = ArtPack.a;
    }

    public void addScore(long value) {
        // NOTE: cashMultiplier is added outside here so that the score particle number is updated too
        score += value;
        totalScore += value;
    }

    public void upgradeSound(){
        soundPack = Math.min(soundPack+1, 2);
        // Call play music after this is called
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
