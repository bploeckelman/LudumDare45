package lando.systems.ld45.ui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld45.Assets;
import lando.systems.ld45.accessors.RectangleAccessor;

public class Panel {

    private boolean visible;
    private boolean animating;
    private Rectangle bounds;
    private NinePatch panel;
    private NinePatch inset;
    private TweenManager tween;

    private final float insetMargin = 20f;

    public boolean horizontal = true;

    public Panel(Assets assets, TweenManager tween) {
        this.tween = tween;
        this.visible = false;
        this.animating = false;
        this.bounds = new Rectangle();
        this.panel = assets.uiPanelNinepatch;
        this.inset = assets.uiPanelInsetNinepatch;
    }

    public void setInitialBounds(float x, float y, float w, float h) {
        bounds.set(x, y, w, h);
    }

    public void update(float dt) {

    }

    public void render(SpriteBatch batch) {
        if (!visible) return;
        panel.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        inset.draw(batch, bounds.x + insetMargin, bounds.y + insetMargin,
                   bounds.width - 2f * insetMargin,
                   bounds.height - 2f * insetMargin);
    }

    public void show(OrthographicCamera camera) {
        if (animating) return;
        if (visible) return;

        visible = true;
        animating = true;

        int accessor = (horizontal) ? RectangleAccessor.X : RectangleAccessor.Y;
        float target = (horizontal) ? camera.viewportWidth - bounds.width : 0;

        Tween.to(bounds, accessor, 0.33f)
             .target(target)
             .ease(Bounce.OUT)
             .setCallback((i, baseTween) -> animating = false)
             .start(tween);
    }

    public void hide(OrthographicCamera camera) {
        if (animating) return;
        if (!visible) return;

        animating = true;

        int accessor = (horizontal) ? RectangleAccessor.X : RectangleAccessor.Y;
        float target = (horizontal) ? camera.viewportWidth : -camera.viewportHeight;

        Tween.to(bounds, accessor, 0.05f)
             .target(target)
             .setCallback((i, baseTween) -> {
                 visible = false;
                 animating = false;
             })
             .start(tween);
    }

    public void toggle(OrthographicCamera camera) {
        if (animating) return;
        if (visible) hide(camera);
        else         show(camera);
    }

    public boolean isVisible() { return visible; }
    public boolean isAnimating() { return animating; }

}
