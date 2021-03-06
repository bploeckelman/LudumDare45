package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.screens.GameScreen;

public class Hopper {

    public Vector2 position;
    public Vector2 velocity;
    public int availableBalls;

    private GameScreen screen;

    private float length = 96;
    private float dropTime = 0;
    private boolean droppingBalls;
    private float inputDelay;

    private float time = 0;
    private boolean isOpen = false;

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
        droppingBalls = false;
        inputDelay = .2f;
    }

    public void dropBalls() {
        droppingBalls = true;
    }

    public void update(float dt) {
        time += dt;
        if (availableBalls == 0) return;
        inputDelay -= dt;

        dropTime -= dt;

        if (droppingBalls) {
            timeDrop();
        }
        move(dt);

    }

    Vector2 tempVector2 = new Vector2();
    public void timeDrop() {
        isOpen = true;

        if (dropTime < 0) {
            dropTime = 0.5f;
            int ballsToDrop = (screen.game.player.balls / 5) + 1;
            ballsToDrop = Math.min(Math.min(ballsToDrop, availableBalls), 10);
            float angle = 170f / (ballsToDrop+1);
            for(int i = 0; i < ballsToDrop; i++) {
                float dir = angle*(i+1)-85 - 90;
                Ball ball = new Ball(screen, 5);
                ball.initialize(tempVector2.set(position).add(MathUtils.cosDeg(dir) * 40 * 2f, MathUtils.sinDeg(dir) * 40 / 2f),
                                new Vector2(velocity.x / 2 + MathUtils.cosDeg(dir)*10, MathUtils.sinDeg(dir) * 10));
                screen.balls.add(ball);
                screen.player.ballsDropped++;
            }

            availableBalls -= ballsToDrop;
            isOpen = availableBalls != 0;
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
        //if (availableBalls > 0) {
//            batch.setColor(Color.BLUE);
//            batch.draw(screen.assets.whitePixel, position.x - length / 2, position.y, length, 10);
//            batch.setColor(Color.WHITE);
            TextureRegion texture = screen.assets.hopper.getKeyFrame(time);
            float y = position.y - 18;
            batch.draw(texture, position.x - 48, y, 48, 18, 96, 36, 1, 1, 0);

            if (isOpen) {
                batch.draw(screen.assets.hopperDoorOpen, position.x - 48, y, 48, 18, 96, 36, 1, 1, 0);
            } else {
                batch.draw(screen.assets.hopperDoorClosed, position.x - 48, y, 48, 18, 96, 36, 1, 1, 0);
            }
        //}
    }
}
