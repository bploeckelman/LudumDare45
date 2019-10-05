package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld45.Config;
import lando.systems.ld45.screens.GameScreen;

public class Peg extends GameObject {

    public static Peg getPeg(GameScreen screen, int level, int gfxPack) {
        return new Peg(screen, level, gfxPack, 16);
    }

    public Peg(GameScreen screen, int level, int gfxPack, float size) {
        super(screen, screen.assets.pegs[gfxPack][level], size, size);

        pos.x = MathUtils.random(10, Config.gameWidth - 20);
        pos.y = MathUtils.random(10, Config.gameHeight - 20);
    }
}
