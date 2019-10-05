package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.ui.typinglabel.TypingLabel;

public class TitleScreen extends BaseScreen {

    public Vector2 pos;
    public Vector2 vel;
    public TypingLabel label;

    public TitleScreen(Game game) {
        super(game);
        pos = new Vector2(worldCamera.viewportWidth/2f, worldCamera.viewportHeight/2f);
        float speed = MathUtils.random(100, 200);
        float dir = MathUtils.random(360);
        vel = new Vector2(MathUtils.cosDeg(dir) * speed, MathUtils.sinDeg(dir) * speed);
        this.label = new TypingLabel(assets.font,
                                     "{JUMP=.2}{RAINBOW} SCORE GOALS {ENDRAINBOW}{ENDJUMP}\n"
                                               + "{WIND=;;;0.7;}{GRADIENT=red;blue} TO GET LETTERS {ENDGRADIENT}{ENDWIND}\n\n"
                                               + "{JUMP=.2}{RAINBOW} SPELL BLAST! {ENDRAINBOW}{ENDJUMP}\n"
                                               + "{WAVE=0.9;1.2;1.75}{GRADIENT=red;blue} TO {SHAKE}WIN {ENDGRADIENT}{ENDSHAKE}{ENDWAVE}",
                                     0f, 0f);
        this.label.setWidth(Config.gameWidth);
        this.label.setFontScale(2f);
        this.label.setY(Config.gameHeight / 2f + 100f);
    }

    @Override
    public void update(float dt) {
        pos.x += vel.x * dt;
        pos.y += vel.y * dt;

        if (pos.x < 20 || pos.x > worldCamera.viewportWidth - 20) vel.x *= -1;
        if (pos.y < 20 || pos.y > worldCamera.viewportHeight - 20) vel.y *= -1;

        label.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(worldCamera.combined);

        batch.draw(assets.debugTexture, worldCamera.viewportWidth/2f - 50, worldCamera.viewportHeight - 100, 100, 100);
        batch.draw(assets.whiteCircle, pos.x - 20, pos.y - 20, 40, 40);

        label.render(batch);

        batch.end();
    }
}
