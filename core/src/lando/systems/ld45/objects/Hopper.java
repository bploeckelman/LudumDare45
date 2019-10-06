package lando.systems.ld45.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.screens.GameScreen;

public class Hopper {

    public Vector2 position;
    public Vector2 velocity;

    private GameScreen screen;

    private float length = 100;
    private int availableBalls;
    private float dropTime = 0;

    public Hopper(GameScreen screen) {
        this.screen = screen;

        position = new Vector2(Config.gameWidth / 2, Config.gameHeight - 20);
        velocity = new Vector2(200, 0);
        if (MathUtils.randomBoolean()) {
            velocity.x = -velocity.x;
        }

        reset();
    }

    public void reset() {
        availableBalls = screen.player.balls;
    }

    public void update(float dt) {
        if (availableBalls == 0) return;

        position.x += (velocity.x * dt);

        float space = 40;
        float halfLength = length / 2;
        if (velocity.x > 0) {
            if (position.x + halfLength > Config.gameWidth - space) {
                position.x = Config.gameWidth - space - halfLength;
                velocity.x = -velocity.x;
            }
        } else {
            if (position.x - halfLength < space) {
                position.x = halfLength + space;
                velocity.x = -velocity.x;
            }
        }

        dropTime -= dt;
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            dropBall(dt);
        }
    }

    public void dropBall(float dt) {
        if (dropTime < 0) {
            dropTime = 0.5f;

            Ball ball = new Ball(screen, 5);
            ball.initialize(position, new Vector2(velocity.x/2, -99));
            screen.balls.add(ball);

            availableBalls--;
        }
    }

    public void render(SpriteBatch batch) {
        if (availableBalls > 0) {
            batch.setColor(Color.BLUE);
            batch.draw(screen.assets.whitePixel, position.x - length / 2, position.y, length, 10);
            batch.setColor(Color.WHITE);
        }
    }
}
