package lando.systems.ld45.objects;

import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld45.Config;
import lando.systems.ld45.screens.GameScreen;

public class Bumper extends GameObject {
    public Bumper(GameScreen screen) {
        this(screen, 10);
    }

    public Bumper(GameScreen screen, float size) {
        super(screen, screen.assets.bumper, size, size);

        pos.x = MathUtils.random(10, Config.gameWidth - 20);
        pos.y = MathUtils.random(10, Config.gameHeight - 20);
    }
}
