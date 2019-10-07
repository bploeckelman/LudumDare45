package lando.systems.ld45.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld45.screens.BaseScreen;

public abstract class UIElement {

    public BaseScreen screen;
    public OrthographicCamera camera;
    public Rectangle bounds;

    private Vector3 projection = new Vector3();

    public boolean isHover = false;
    public boolean isVisible = true;

    public UIElement(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
    }

    public void set(BaseScreen screen) {
        this.set(screen, screen.hudCamera);
    }

    public void set(BaseScreen screen, OrthographicCamera camera) {
        this.screen = screen;
        this.camera = camera;

        screen.addUIElement(this);
    }

    // handle rollover/highlight/tooltips here
    public void update(float dt) {
        if (!isVisible) return;

        projection.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        projection = camera.unproject(projection);
        isHover = bounds.contains(projection.x, projection.y);
    }

    public void render(SpriteBatch batch) {
        if (isVisible) {
            renderElement(batch);
        }
    }

    protected abstract void renderElement(SpriteBatch batch);
}
