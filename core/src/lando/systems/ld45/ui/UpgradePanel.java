package lando.systems.ld45.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.screens.BaseScreen;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.utils.UIAssetType;

public class UpgradePanel extends Panel {

    private Button startGameButton;

    // BUTTONS:
    /*
    - Upgrade Art (X/4)
    - Buy Sounds (X/Y)

     */

    public UpgradePanel(BaseScreen screen, UIAssetType uiAssetTypePanel, UIAssetType uiAssetTypePanelInset) {
        super(screen, uiAssetTypePanel, uiAssetTypePanelInset);
        horizontal = false;

        this.startGameButton = new Button(screen, screen.worldCamera, screen.assets.whitePixel, 0f, 0f, 300f, 50f);
        this.startGameButton.setText("Start Game");
        this.startGameButton.addClickHandler(() -> {
            if (isVisible() && !isAnimating()) {
                hide(screen.worldCamera, 0.5f, (params) -> screen.game.setScreen(new GameScreen(screen.game, false)));
            }
        });
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        // Since the panel moves with show/hide/toggle, have to continously reset the button pos... IMGUI ftw
        startGameButton.bounds.setPosition(
                bounds.x + bounds.width  / 2f - startGameButton.bounds.width  / 2f,
                bounds.y + insetMargin + 10f);
        startGameButton.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        startGameButton.render(batch);
    }

}
