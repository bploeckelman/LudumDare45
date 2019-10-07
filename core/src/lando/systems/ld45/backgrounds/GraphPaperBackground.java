package lando.systems.ld45.backgrounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import lando.systems.ld45.screens.GameScreen;

public class GraphPaperBackground extends Background {

    Array<Vector2> equationPositions;
    FloatArray equationRotations;


    public GraphPaperBackground(GameScreen screen){
        super(screen);

        equationPositions = new Array<>();
        equationRotations = new FloatArray();
        for (int i = 0; i < screen.assets.equations.size; i++){
            equationPositions.add(new Vector2(MathUtils.random(10f, screen.worldCamera.viewportWidth - 100), MathUtils.random(90f, screen.worldCamera.viewportHeight - 100)));
            equationRotations.add(MathUtils.random(-20, 20));
        }
    }

    @Override
    public void addCollision(float x, float y, float size, float ttl, Color color) {
        if (collisions.size < explosionSize) {
            collisions.add(new Collision(x, y, size, ttl, color));
        }
    }


    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(.8f, .8f, .8f, 1f);
        batch.draw(gameScreen.assets.gridPaper, 0, 0, gameScreen.worldCamera.viewportWidth, gameScreen.worldCamera.viewportHeight);
        batch.setColor(1f, 1f, 1f, .2f);
        for (int i = 0; i < equationPositions.size; i++){
            batch.draw(gameScreen.assets.equations.get(i), equationPositions.get(i).x, equationPositions.get(i).y, 0, 0, 150, 150, 1, 1, equationRotations.get(i));
        }
        batch.setColor(1, 1, 1, .3f);
        for (Collision collision : collisions){
            batch.draw(gameScreen.assets.scribble.getKeyFrame(collision.ttl/collision.maxTTL * gameScreen.assets.scribble.getAnimationDuration()), collision.pos.x - 30, collision.pos.y- 30, 60, 60);
        }
        batch.setColor(Color.WHITE);
    }
}
