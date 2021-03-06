package lando.systems.ld45.objects;

import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Game;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.utils.AssetType;

public class Peg extends GameObject {

    public static final long SCORE_VALUE = 1;


    public static Peg getPeg(GameScreen screen) {
        return new Peg(screen,16);
    }

    public Peg(GameScreen screen, float size) {
        super(screen, Game.getAsset(AssetType.peg,0), size, size);
        setCircleBounds(-10, 10, size/2f);
    }

    @Override
    public void update(float dt, Vector2 mousePosition) {
        super.update(dt, mousePosition);
        setCircleBounds(pos.x, pos.y, size.x/2f);
        image = Game.getAsset(AssetType.peg, artAccum);
    }
}
