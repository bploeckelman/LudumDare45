package lando.systems.ld45.ui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Bounce;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld45.accessors.RectangleAccessor;
import lando.systems.ld45.screens.BaseScreen;
import lando.systems.ld45.utils.Callback;
import lando.systems.ld45.utils.UIAssetType;

public class Panel {

    private static final float DEFAULT_SHOW_DURATION = 0.33f;
    private static final float DEFAULT_HIDE_DURATION = 0.2f;

    protected boolean visible;
    protected boolean animating;
    protected Rectangle bounds;
    protected UIAssetType uiAssetTypePanel;
    protected UIAssetType uiAssetTypePanelInset;
    protected NinePatch panel;
    protected NinePatch inset;
    protected BaseScreen screen;

    protected final float insetMargin = 20f;

    public boolean horizontal = true;

    public Panel(BaseScreen screen, UIAssetType uiAssetTypePanel, UIAssetType uiAssetTypePanelInset) {
        this.screen = screen;
        this.visible = false;
        this.animating = false;
        this.bounds = new Rectangle();
        this.uiAssetTypePanel = uiAssetTypePanel;
        this.uiAssetTypePanelInset = uiAssetTypePanelInset;
        this.panel = screen.assets.uiAssetNinepatchMap.get(screen.game.artPack).get(uiAssetTypePanel);
        this.inset = screen.assets.uiAssetNinepatchMap.get(screen.game.artPack).get(uiAssetTypePanelInset);
    }

    public void setInitialBounds(float x, float y, float w, float h) {
        bounds.set(x, y, w, h);
    }

    public void update(float dt) {
        panel = screen.assets.uiAssetNinepatchMap.get(screen.game.artPack).get(uiAssetTypePanel);
        inset = screen.assets.uiAssetNinepatchMap.get(screen.game.artPack).get(uiAssetTypePanelInset);
    }

    public void render(SpriteBatch batch) {
        if (!visible) return;
        panel.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        inset.draw(batch, bounds.x + insetMargin, bounds.y + insetMargin,
                   bounds.width - 2f * insetMargin,
                   bounds.height - 2f * insetMargin);
    }

    public void show(OrthographicCamera camera) {
        show(camera, DEFAULT_SHOW_DURATION);
    }

    public void show(OrthographicCamera camera, float duration) {
        show(camera, duration, null);
    }

    public void show(OrthographicCamera camera, float duration, Callback callback) {
        if (animating) return;
        if (visible) return;

        visible = true;
        animating = true;

        int accessor = (horizontal) ? RectangleAccessor.X : RectangleAccessor.Y;
        float target = (horizontal) ? camera.viewportWidth - bounds.width : 0;

        Tween.to(bounds, accessor, duration)
             .target(target)
             .ease(Bounce.OUT)
             .setCallback((i, baseTween) -> {
                 animating = false;
                 if (callback != null) callback.call();
             })
             .start(screen.game.tween);
    }

    public void hide(OrthographicCamera camera) {
        hide(camera, DEFAULT_HIDE_DURATION);
    }

    public void hide(OrthographicCamera camera, float duration) {
        hide(camera, duration, null);
    }

    public void hide(OrthographicCamera camera, float duration, Callback callback) {
        if (animating) return;
        if (!visible) return;

        animating = true;

        int accessor = (horizontal) ? RectangleAccessor.X : RectangleAccessor.Y;
        float target = (horizontal) ? camera.viewportWidth : -camera.viewportHeight;

        Tween.to(bounds, accessor, duration)
             .target(target)
             .setCallback((i, baseTween) -> {
                 visible = false;
                 animating = false;
                 if (callback != null) callback.call();
             })
             .start(screen.game.tween);
    }

    public void toggle(OrthographicCamera camera) {
        toggle(camera, null);
    }

    public void toggle(OrthographicCamera camera, Callback callback) {
        if (animating) return;
        if (visible) hide(camera, DEFAULT_HIDE_DURATION, callback);
        else         show(camera, DEFAULT_SHOW_DURATION, callback);
    }

    public boolean isVisible() { return visible; }
    public boolean isAnimating() { return animating; }

}
