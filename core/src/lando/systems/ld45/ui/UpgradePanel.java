package lando.systems.ld45.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld45.screens.BaseScreen;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.ui.typinglabel.TypingLabel;
import lando.systems.ld45.utils.UIAssetType;

public class UpgradePanel extends Panel {

    private static final float CONTENT_MARGIN = 10f;

    private static final String DEFAULT_DESCRIPTION = ""
        + "{SPEED=5} "
        + "{RAINBOW}Shop for upgrades!{ENDRAINBOW} \n\n"
        + "{GRADIENT=red;blue}  Hover over buttons to see descriptions{ENDGRADIENT} \n"
        + "{GRADIENT=goldenrod;dark_gray}  Click to upgrade (if you can afford it)!{ENDGRADIENT} \n"
        + "{GRADIENT=forest;green}  Press 'Start Game' to go back to the game.{ENDGRADIENT} ";

    private Button startGameButton;

    private Rectangle descriptionBounds;
    private Rectangle buttonGridBounds;

    private UpgradeButton buyEffectsButton;
    private UpgradeButton buyPegGizmosButton;
    private UpgradeButton buyBumperGizmosButton;
    private UpgradeButton buyLeftSpinnerGizmosButton;
    private UpgradeButton buyRightSpinnerGizmosButton;
    private UpgradeButton buyCashMultiplierButton;
    private UpgradeButton buyBallMultiplierButton;
    private UpgradeButton buyAudioButton;
    private UpgradeButton buyArtPackButton;

    private Vector3 mousePos;
    private UpgradeButton hoveredButton;
    private String descriptionText;
    private TypingLabel descriptionLabel;

    public UpgradePanel(BaseScreen screen, UIAssetType uiAssetTypePanel, UIAssetType uiAssetTypePanelInset) {
        super(screen, uiAssetTypePanel, uiAssetTypePanelInset);
        this.horizontal = false;

        this.descriptionBounds = new Rectangle();
        this.buttonGridBounds = new Rectangle();

        this.startGameButton = new Button(screen, screen.worldCamera, screen.assets.whitePixel, 0f, 0f, 300f, 50f);
        this.startGameButton.setText("Start Game");
        this.startGameButton.addClickHandler(() -> {
            if (isVisible() && !isAnimating()) {
                hide(screen.worldCamera, 0.5f, (params) -> screen.game.setScreen(new GameScreen(screen.game, false)));
            }
        });

        this.buyEffectsButton            = new UpgradeButton(this, "Buy Special Effects", "{SPEED=5}Purchase special effects:\n\nParticles, Ball trails, Screenshake");
        this.buyPegGizmosButton          = new UpgradeButton(this, "Buy Pegs",            "{SPEED=5}Purchase pegs:\n\nUnlock first, then purchase additional pegs up to X");
        this.buyBumperGizmosButton       = new UpgradeButton(this, "Buy Bumpers",         "{SPEED=5}Purchase bumpers:\n\nUnlock first, then purchase additional bumpers up to X");
        this.buyLeftSpinnerGizmosButton  = new UpgradeButton(this, "Buy Left Spinner",    "{SPEED=5}Purchase spinners (rotating left):\n\nUnlock first, then purchase additional spinners up to X");
        this.buyRightSpinnerGizmosButton = new UpgradeButton(this, "Buy Right Spinner",   "{SPEED=5}Purchase spinners (rotating right):\n\nUnlock first, then purchase additional spinners up to X");
        this.buyCashMultiplierButton     = new UpgradeButton(this, "Buy Cash Multiplier", "{SPEED=5}Purchase cash multiplier:\n\nIncrease the amount of money you get for everything;\n2x, 4x, 8x, and 16x");
        this.buyBallMultiplierButton     = new UpgradeButton(this, "Buy Ball Multiplier", "{SPEED=5}Purchase ball multiplier:\n\nDrop more balls each playthrough;\n3x, 10x, 50x, 100x");
        this.buyAudioButton              = new UpgradeButton(this, "Buy Audio",           "{SPEED=5}Purchase audio:\n\nIt's like graphics, but for your ears!\nUnlock placeholder pack, then final version");
        this.buyArtPackButton            = new UpgradeButton(this, "Buy Art Packs",       "{SPEED=5}Purchase art:\n\nIt's like audio, but for your eyes!\nHire and artist and move through four distinct art packs!");

        this.mousePos = new Vector3();
        this.hoveredButton = null;
        this.descriptionText = DEFAULT_DESCRIPTION;
        this.descriptionLabel = new TypingLabel(screen.assets, descriptionText, 0f, 300f);
        this.descriptionLabel.setLineAlign(Align.left);
        this.descriptionLabel.setWidth(screen.worldCamera.viewportWidth - 4f * INSET_MARGIN);
        this.descriptionLabel.setText(descriptionText);

        initializeButtons();
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        // Since the panel moves with show/hide/toggle, have to continuously reset positions... immediate-mode gui style
        float contentHeight = (bounds.height - 2f * INSET_MARGIN - 2f * CONTENT_MARGIN);
        float descriptionHeight = (1f / 3f) * contentHeight;
        descriptionBounds.set(
                bounds.x + INSET_MARGIN + CONTENT_MARGIN,
                bounds.y + bounds.height - INSET_MARGIN - CONTENT_MARGIN - descriptionHeight,
                bounds.width - 2f * INSET_MARGIN - 2f * CONTENT_MARGIN,
                descriptionHeight);

        buttonGridBounds.set(
                bounds.x + INSET_MARGIN + CONTENT_MARGIN,
                bounds.y + INSET_MARGIN + CONTENT_MARGIN,
                bounds.width - 2f * INSET_MARGIN - 2f * CONTENT_MARGIN,
                (2f / 3f) * contentHeight);

        float rowWidth = buttonGridBounds.width;
        float rowHeight = buttonGridBounds.height / 3f;
        float buttonWidth = rowWidth / 3f;
        float x = buttonGridBounds.x;
        float y = buttonGridBounds.y + buttonGridBounds.height - rowHeight;

        // first (top) row
        buyEffectsButton              .bounds.set(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyCashMultiplierButton       .bounds.set(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyBallMultiplierButton       .bounds.set(x, y, buttonWidth, rowHeight); x += buttonWidth;

        // second (middle) row
        y -= rowHeight;
        x = buttonGridBounds.x;
        buyArtPackButton              .bounds.set(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyPegGizmosButton            .bounds.set(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyAudioButton                .bounds.set(x, y, buttonWidth, rowHeight); x += buttonWidth;

        // third (bottom) row
        y -= rowHeight;
        x = buttonGridBounds.x;
        buyLeftSpinnerGizmosButton    .bounds.set(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyBumperGizmosButton         .bounds.set(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyRightSpinnerGizmosButton   .bounds.set(x, y, buttonWidth, rowHeight); x += buttonWidth;

        buyEffectsButton             .update(dt);
        buyCashMultiplierButton      .update(dt);
        buyBallMultiplierButton      .update(dt);
        buyArtPackButton             .update(dt);
        buyPegGizmosButton           .update(dt);
        buyAudioButton               .update(dt);
        buyLeftSpinnerGizmosButton   .update(dt);
        buyBumperGizmosButton        .update(dt);
        buyRightSpinnerGizmosButton  .update(dt);

        // Figure out which button is hovered (if any)
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        screen.worldCamera.unproject(mousePos.set(mouseX, mouseY, 0f));
        hoveredButton = null;
        if      (buyEffectsButton            .bounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyEffectsButton;
        else if (buyCashMultiplierButton     .bounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyCashMultiplierButton;
        else if (buyBallMultiplierButton     .bounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyBallMultiplierButton;
        else if (buyArtPackButton            .bounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyArtPackButton;
        else if (buyPegGizmosButton          .bounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyPegGizmosButton;
        else if (buyAudioButton              .bounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyAudioButton;
        else if (buyLeftSpinnerGizmosButton  .bounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyLeftSpinnerGizmosButton;
        else if (buyBumperGizmosButton       .bounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyBumperGizmosButton;
        else if (buyRightSpinnerGizmosButton .bounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyRightSpinnerGizmosButton;

        descriptionLabel.setX(descriptionBounds.x + INSET_MARGIN);
        descriptionLabel.setY(descriptionBounds.y + descriptionBounds.height - INSET_MARGIN);

        String prevDescriptionText = descriptionText;
        descriptionText = (hoveredButton != null) ? hoveredButton.description : DEFAULT_DESCRIPTION;
        if (!prevDescriptionText.equals(descriptionText)) {
            descriptionLabel.restart(descriptionText);
        }
        descriptionLabel.update(dt);

        startGameButton.bounds.setPosition(
                bounds.x + bounds.width  / 2f - startGameButton.bounds.width  / 2f,
                bounds.y);
        startGameButton.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isVisible()) return;
        panel.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        inset.draw(batch, descriptionBounds.x, descriptionBounds.y, descriptionBounds.width, descriptionBounds.height);
        descriptionLabel.render(batch);

        inset.draw(batch, buttonGridBounds.x, buttonGridBounds.y, buttonGridBounds.width, buttonGridBounds.height);
        buyEffectsButton             .render(batch);
        buyCashMultiplierButton      .render(batch);
        buyBallMultiplierButton      .render(batch);
        buyArtPackButton             .render(batch);
        buyPegGizmosButton           .render(batch);
        buyAudioButton               .render(batch);
        buyLeftSpinnerGizmosButton   .render(batch);
        buyBumperGizmosButton        .render(batch);
        buyRightSpinnerGizmosButton  .render(batch);

        startGameButton.render(batch);
    }

    private void initializeButtons() {
//        screen.game.player.
//        buyEffectsButton            .set(screen.game.player);
//        buyPegGizmosButton          ;
//        buyBumperGizmosButton       ;
//        buyLeftSpinnerGizmosButton  ;
//        buyRightSpinnerGizmosButton ;
//        buyCashMultiplierButton     ;
//        buyBallMultiplierButton     ;
//        buyAudioButton              ;
//        buyArtPackButton            ;
    }

}
