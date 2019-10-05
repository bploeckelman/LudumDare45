package lando.systems.ld45.objects;

import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.screens.GameScreen;

public class Ball extends GameObject {
    public Vector2 vel = new Vector2();

    public Ball(GameScreen screen) {
        super(screen, screen.assets.whiteCircle);

        pos.x = pos.y = 100;
        vel.x = 20;
        vel.y = 20;
    }

    @Override
    public void update(float dt) {
        pos.x += vel.x * dt;
        pos.y += vel.y * dt;

        if (pos.x < 20 || pos.x > screen.worldCamera.viewportWidth - 20) vel.x *= -1;
        if (pos.y < 20 || pos.y > screen.worldCamera.viewportHeight - 20) vel.y *= -1;
    }
}
