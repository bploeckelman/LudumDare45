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

    private static int id = 0;

    private int curId = id++;

    private TextureRegion texture;

    protected HudBox box;

    private String text;

    private ClickHandler clickHandler;

    public boolean isDisabled = false;

    public Color backgroundColor = Color.WHITE;
    public Color backgroundHoverColor = Color.GOLD;
    public Color backgroundDisabledColor = Color.DARK_GRAY;

    public Color textColor = Color.BLACK;
    public Color textHoverColor = Color.BLUE;
    public Color textDisabledColor = Color.GRAY;

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
        bounds.set(x, y, width, height);
    }

    public void addClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (box != null) {
            box.update(dt);
        }
        super.update(dt);

        if (isVisible && isHover && !isDisabled && (Gdx.input.justTouched() || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
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
        Color textColor = getTextColor();
        batch.setColor(getBackgroundColor());

        if (box != null) {
            batch.draw(screen.assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
            box.renderBox(batch, textColor);
        }

        if (texture != null) {
            drawShittyButton(batch, textColor);
        }

        batch.setColor(Color.WHITE);
    }

    private void drawShittyButton(SpriteBatch batch, Color textColor) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);

        if (text != null) {
            BitmapFont font = Game.getCurrentFont();
            font.setColor(textColor);
            float textY = bounds.y + bounds.height - ((bounds.height - font.getCapHeight())/2);
            font.draw(batch, text, bounds.x, textY, bounds.width - 10, Align.center, false);
            font.setColor(Color.WHITE);
        }
    }

    private Color getBackgroundColor() {
        Color background = (isHover) ? backgroundHoverColor : backgroundColor;
        if (isDisabled) {
            background = backgroundDisabledColor;
        }
        return background;
    }

    private Color getTextColor() {
        Color text = (isHover) ? textHoverColor : textColor;
        if (isDisabled) {
            text = textDisabledColor;
        }
        return text;
    }


}
