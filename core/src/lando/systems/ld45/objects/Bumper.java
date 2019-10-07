package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.AssetType;

public class Bumper extends GameObject {

    public static final long SCORE_VALUE = 2;

    private float bumperSize;

    private float currentBumperSize = 0;

    public static Bumper getBumper(GameScreen screen) {
        return new Bumper(screen,32);
    }

    public Bumper(GameScreen screen, float size) {
        super(screen, Game.getAsset(AssetType.bumper,0), size, size);

        this.bumperSize = this.currentBumperSize = size;

        setPosition(MathUtils.random(10, Config.gameWidth - 20), MathUtils.random(10, Config.gameHeight - 200));

        setCircleBounds(pos.x, pos.y, bumperSize/2f);
    }

    @Override
    public void update(float dt, Vector2 mousePosition) {
        super.update(dt, mousePosition);

        float adjust = 1f;
        if (screen.game.player.artPack == ArtPack.b) {
            adjust = 0.2f;
        }
        image = Game.getAsset(AssetType.bumper, artAccum * adjust);

        if (currentHitTime > 0) {
            if (currentBumperSize > (hitTime / 2)) {
                currentBumperSize = bumperSize + (bumperSize * Interpolation.bounceIn.apply(currentHitTime / hitTime / 2));
            } else {
                currentBumperSize = bumperSize * (1 + Interpolation.bounceOut.apply(currentHitTime / hitTime));
            }
        } else {
            currentBumperSize = bumperSize;
        }
        size.x = size. y = currentBumperSize;
        setCircleBounds(pos.x, pos.y, bumperSize/2f);

    }

    @Override
    public void render(SpriteBatch batch) {
        float half = currentBumperSize / 2;
        batch.draw(image, pos.x - half, pos.y - half, currentBumperSize, currentBumperSize);
    }
}
