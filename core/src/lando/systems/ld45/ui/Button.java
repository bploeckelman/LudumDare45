package lando.systems.ld45.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.screens.BaseScreen;
import lando.systems.ld45.ui.typinglabel.TypingLabel;

public class Button extends UIElement {

    private TextureRegion texture;
    private String text;

    private TypingLabel label;

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

    public void setText(String text) {
        this.text = text;
    }

    public void addClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public void update(float dt) {
        if (!isVisible) return;

        super.update(dt);
        updateLabel(dt);

        if (isHover && (Gdx.input.justTouched() || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
            onClick();
        }
    }

    private void updateLabel(float dt) {
        if (text == null) return;

        if (label == null) {
            label = createLabel();
        }
        label.setX(bounds.x);
        label.setY(bounds.y + bounds.height - (bounds.height - label.getLineHeight())/2);
        label.update(dt);
    }

    private void onClick() {
        if (clickHandler != null) {
            clickHandler.click();
        }
    }

    protected TypingLabel createLabel() {
        TypingLabel label = new TypingLabel(Game.getCurrentFont(), text,0f, 0f);

        label.setWidth(bounds.width);
        label.setFontScale(2f);

        label.setX(bounds.x);
        label.setY(bounds.y + bounds.height - (bounds.height - label.getLineHeight())/2);

        return label;
    }

    @Override
    protected void renderElement(SpriteBatch batch) {
        batch.setColor((isHover) ? Color.RED : Color.BLUE);
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        if (label != null) {
            label.render(batch);
        }
        batch.setColor(Color.WHITE);
    }
}
