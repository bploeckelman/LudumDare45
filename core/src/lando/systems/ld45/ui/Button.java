package lando.systems.ld45.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld45.Game;
import lando.systems.ld45.screens.BaseScreen;

public class Button extends UIElement {

    private TextureRegion texture;

    protected HudBox box;

    private String text;

    private ClickHandler clickHandler;

    public Button(BaseScreen screen, OrthographicCamera camera, TextureRegion texture, float x, float y) {
        this(screen, camera, texture, x, y, texture.getRegionWidth(), texture.getRegionHeight());
    }

    public Button(BaseScreen screen, OrthographicCamera camera, TextureRegion texture, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.texture = texture;
        this.screen = screen;
        this.camera = camera;
    }

    public Button(BaseScreen screen, OrthographicCamera camera, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.texture = null;
        this.screen = screen;
        this.camera = camera;

        box = new HudBox(x, y, width, height);
        box.align = Align.center;
        box.wrap = true;
    }

    public void setText(String text) {
        if (box != null) {
            box.setText(text.toUpperCase());
        } else {
            this.text = text;
        }
    }

    public void setHudBox(float x, float y, float width, float height) {
        box.reset(x, y, width, height);
    }

    public void addClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public void update(float dt) {
        if (!isVisible) return;

        super.update(dt);
        if (box != null) {
            box.update(dt);
        }

        if (isHover && (Gdx.input.justTouched() || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
            onClick();
        }
    }

    private void onClick() {
        if (clickHandler != null) {
            clickHandler.click();
        }
    }

    @Override
    protected void renderElement(SpriteBatch batch) {
        if (box != null) {
            box.render(batch);
        }

        if (texture != null) {
            drawShittyButton(batch);
        }
    }

    private void drawShittyButton(SpriteBatch batch) {
        batch.setColor((isHover) ? Color.RED : Color.BLUE);
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);

        if (text != null) {
            BitmapFont font = Game.getCurrentFont();
            float textY = bounds.y + bounds.height - ((bounds.height - font.getCapHeight())/2);
            font.draw(batch, text, bounds.x, textY, bounds.width - 10, Align.center, false);
        }

        batch.setColor(Color.WHITE);
    }
}
