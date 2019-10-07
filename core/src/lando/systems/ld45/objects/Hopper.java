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
    public int availableBalls;

    private GameScreen screen;

    private float length = 100;
    private float dropTime = 0;

    public Hopper(GameScreen screen) {
        this.screen = screen;

        velocity = new Vector2(400, 0);
        if (MathUtils.randomBoolean()) {
            velocity.x = -velocity.x;
        }

        reset();
    }

    public void reset() {
        position = new Vector2(Config.gameWidth / 2, Config.gameHeight - 20);
        availableBalls = screen.game.player.balls;
    }

    public void update(float dt) {
        if (availableBalls == 0) return;

        dropTime -= dt;
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            dropBall(dt);
        }
        move(dt);

    }

    public void dropBall(float dt) {
        if (dropTime < 0) {
            dropTime = 0.5f;

            Ball ball = new Ball(screen, 5);
            screen.shaker.addDamage(1f);
            ball.initialize(position, new Vector2(velocity.x / 2, -99));
            screen.balls.add(ball);

            availableBalls--;
        }
    }

    private void move(float dt) {
        position.x += (velocity.x * dt);

        float space = 60;
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
    }

    public void render(SpriteBatch batch) {
        if (availableBalls > 0) {
            batch.setColor(Color.BLUE);
            batch.draw(screen.assets.whitePixel, position.x - length / 2, position.y, length, 10);
            batch.setColor(Color.WHITE);
        }
    }
}
