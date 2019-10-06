package lando.systems.ld45.ui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Bounce;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld45.accessors.RectangleAccessor;
import lando.systems.ld45.screens.BaseScreen;
import lando.systems.ld45.utils.UIAssetType;

public class Panel {

    private boolean visible;
    private boolean animating;
    private Rectangle bounds;
    private UIAssetType uiAssetTypePanel;
    private UIAssetType uiAssetTypePanelInset;
    private NinePatch panel;
    private NinePatch inset;
    private BaseScreen screen;

    private final float insetMargin = 20f;

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
             .start(screen.game.tween);
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
             .start(screen.game.tween);
    }

    public void toggle(OrthographicCamera camera) {
        if (animating) return;
        if (visible) hide(camera);
        else         show(camera);
    }

    public boolean isVisible() { return visible; }
    public boolean isAnimating() { return animating; }

}
