package lando.systems.ld45.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.screens.GameScreen;

public class HexBackground extends Background {


    float[] explosionArray = new float[explosionSize * 4];
    float[] explosionOwnerArray = new float[explosionSize * 3];


    Color gridColor = new Color(.1f, .1f, .1f, .3f);

    public HexBackground(GameScreen gameScreen){
        super(gameScreen);

    }

    @Override
    public void render(SpriteBatch batch) {
        ShaderProgram shader = gameScreen.assets.hexGridShader;
        for (int i = 0; i < explosionSize; i++){
            int index = i * 4;
            int ownerIndex = i * 3;

            if (i < collisions.size) {
                Collision e = collisions.get(i);
                explosionArray[index] = e.pos.x;
                explosionArray[index+1] = e.pos.y;
                explosionArray[index+2] = e.intensity;
                explosionArray[index+3] = e.explosionSize;
                explosionOwnerArray[ownerIndex] = e.color.r;
                explosionOwnerArray[ownerIndex+1] = e.color.g;
                explosionOwnerArray[ownerIndex+2] = e.color.b;

            } else {
                explosionArray[index] = 0;
                explosionArray[index+1] = 0;
                explosionArray[index+2] = 0;
                explosionArray[index+3] = 0;
                explosionOwnerArray[ownerIndex] = 0;
                explosionOwnerArray[ownerIndex+1] = 0;
                explosionOwnerArray[ownerIndex+2] = 0;
            }
        }


        batch.flush();
        batch.setShader(shader);

        Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);
        gameScreen.assets.pixel.bind(0);
        shader.setUniformi("u_texture", 0);
        shader.setUniformf("u_screenResolution", gameScreen.worldCamera.viewportWidth, gameScreen.worldCamera.viewportHeight);
        shader.setUniformf("u_lineColor", Color.CLEAR);
        shader.setUniformf("u_leftColor", gridColor);
        shader.setUniformf("u_rightColor", gridColor);
        shader.setUniformf("u_hexScale", .7f);

        shader.setUniform4fv("explosions", explosionArray, 0, explosionArray.length);
        shader.setUniform3fv("explostionOwners", explosionOwnerArray, 0, explosionOwnerArray.length);

        batch.setColor(.5f, .5f, 1f, 1f);
        batch.draw(gameScreen.assets.pixel,
                0, gameScreen.worldCamera.viewportHeight,
                gameScreen.worldCamera.viewportWidth, -gameScreen.worldCamera.viewportHeight);
        batch.setShader(null);
        batch.setColor(Color.WHITE);
    }
}
