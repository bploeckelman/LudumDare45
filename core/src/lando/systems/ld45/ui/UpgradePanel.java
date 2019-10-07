package lando.systems.ld45.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld45.audio.AudioManager;
import lando.systems.ld45.screens.BaseScreen;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.screens.WinnerScreen;
import lando.systems.ld45.ui.typinglabel.TypingLabel;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.UIAssetType;

public class UpgradePanel extends Panel {

    private static final float CONTENT_MARGIN = 10f;

    private static final String DEFAULT_DESCRIPTION = ""
        + "{SPEED=8} "
        + "{RAINBOW}Shop for upgrades!{ENDRAINBOW} \n"
        + "{GRADIENT=red;blue}    Hover over buttons to see descriptions{ENDGRADIENT} \n"
        + "{GRADIENT=goldenrod;dark_gray}    Click to upgrade, if you can afford it!{ENDGRADIENT} \n"
        + "{GRADIENT=slate;purple}    Press 'Start Editing' to edit the game board{ENDGRADIENT} \n"
        + "{GRADIENT=forest;olive}    Press 'Start Playing' to go back to the game{ENDGRADIENT} ";
    private static final String DEFAULT_COST_TEXT = "{SPEED=8}{GRADIENT=black;dark_gray}Hover an upgrade to see costs...{ENDGRADIENT}";

    private Rectangle descriptionBounds;
    private Rectangle buttonGridBounds;
    private Rectangle costTextBounds;

    private HudBox descriptionBox;
    private HudBox costTextBox;

    private UpgradeButton buyEffectsButton;
    private UpgradeButton buyPegGizmosButton;
    private UpgradeButton buyBumperGizmosButton;
    private UpgradeButton buyLeftSpinnerGizmosButton;
    private UpgradeButton buyRightSpinnerGizmosButton;
    private UpgradeButton buyCashMultiplierButton;
    private UpgradeButton buyBallMultiplierButton;
    private UpgradeButton buyAudioButton;
    private UpgradeButton buyArtPackButton;

    private Button startPlayingButton;
    private Button startEditingButton;
    private UpgradeButton buyWinGameUpgradeButton;

    private Vector3 mousePos;
    private UpgradeButton hoveredButton;

    private String descriptionText;
    private TypingLabel descriptionLabel;

    private String costText;
    private TypingLabel costLabel;

    public UpgradePanel(BaseScreen screen, UIAssetType uiAssetTypePanel, UIAssetType uiAssetTypePanelInset) {
        super(screen, uiAssetTypePanel, uiAssetTypePanelInset);
        this.horizontal = false;

        this.descriptionBounds = new Rectangle();
        this.buttonGridBounds = new Rectangle();
        this.costTextBounds = new Rectangle();

        this.descriptionBox = new HudBox(0f, 0f, 0f, 0f);
        this.costTextBox = new HudBox(0f, 0f, 0f, 0f);

        this.buyEffectsButton            = new UpgradeButton(this, UpgradeProps.special_effects);
        this.buyPegGizmosButton          = new UpgradeButton(this, UpgradeProps.pegs);
        this.buyBumperGizmosButton       = new UpgradeButton(this, UpgradeProps.bumpers);
        this.buyLeftSpinnerGizmosButton  = new UpgradeButton(this, UpgradeProps.spinners_left);
        this.buyRightSpinnerGizmosButton = new UpgradeButton(this, UpgradeProps.spinners_right);
        this.buyCashMultiplierButton     = new UpgradeButton(this, UpgradeProps.cash_multipliers);
        this.buyBallMultiplierButton     = new UpgradeButton(this, UpgradeProps.ball_multipliers);
        this.buyAudioButton              = new UpgradeButton(this, UpgradeProps.audio_packs);
        this.buyArtPackButton            = new UpgradeButton(this, UpgradeProps.art_packs);

        this.startPlayingButton = new Button(screen, screen.worldCamera, 0f, 0f, 300f, 50f);
        this.startPlayingButton.setText("Start Playing");
        this.startPlayingButton.addClickHandler(() -> {
            if (isVisible() && !isAnimating()) {
                hide(screen.worldCamera, 0.5f, (params) -> screen.game.setScreen(new GameScreen(screen.game, false)));
            }
        });

        this.startEditingButton = new Button(screen, screen.worldCamera, 0f, 0f, 300f, 50f);
        this.startEditingButton.setText("Start Editing");
        this.startEditingButton.addClickHandler(() -> {
            if (isVisible() && !isAnimating()) {
                hide(screen.worldCamera, 0.5f, (params) -> screen.game.setScreen(new GameScreen(screen.game, true)));
            }
        });

        this.buyWinGameUpgradeButton = new UpgradeButton(this, UpgradeProps.win_game);

        this.mousePos = new Vector3();
        this.hoveredButton = null;
        this.descriptionText = DEFAULT_DESCRIPTION;
        this.descriptionLabel = new TypingLabel(screen.assets, descriptionText, 0f, 300f);
        this.descriptionLabel.setLineAlign(Align.left);
        this.descriptionLabel.setWidth(screen.worldCamera.viewportWidth - 4f * INSET_MARGIN);
        this.descriptionLabel.setText(descriptionText);

        this.costText = DEFAULT_COST_TEXT;
        this.costLabel = new TypingLabel(screen.assets, costText, 0f, 30f);
        this.costLabel.setLineAlign(Align.center);
        this.costLabel.setWidth(screen.worldCamera.viewportWidth - 4f * INSET_MARGIN);
        this.costLabel.setText(costText);

        initializeButtons();
    }

    @Override
    public void update(float dt) {
        super.update(dt);


        boolean edits = screen.player.canBuildSomething();
        if (!startEditingButton.isDisabled) {
            startEditingButton.setHighlight(edits);
        }

        // Since the panel moves with show/hide/toggle, have to continuously reset positions... immediate-mode gui style
        float contentHeight = (bounds.height - 2f * INSET_MARGIN - 2f * CONTENT_MARGIN);
        float descriptionHeight = (1f / 3f) * contentHeight;
        descriptionBounds.set(
                bounds.x + INSET_MARGIN + CONTENT_MARGIN,
                bounds.y + bounds.height - INSET_MARGIN - CONTENT_MARGIN - descriptionHeight + 20f,
                bounds.width - 2f * INSET_MARGIN - 2f * CONTENT_MARGIN,
                descriptionHeight);
        descriptionBox.reset(descriptionBounds.x, descriptionBounds.y, descriptionBounds.width, descriptionBounds.height);

        float costTextBoundsHeight = 40f;
        costTextBounds.set(
                bounds.x + INSET_MARGIN + CONTENT_MARGIN,
                descriptionBounds.y - costTextBoundsHeight - INSET_MARGIN * (2f / 4f),
                bounds.width - 2f * INSET_MARGIN - 2f * CONTENT_MARGIN,
                costTextBoundsHeight);
        costTextBox.reset(costTextBounds.x, costTextBounds.y, costTextBounds.width, costTextBounds.height);

        buttonGridBounds.set(
                bounds.x + INSET_MARGIN + CONTENT_MARGIN,
                bounds.y + INSET_MARGIN + CONTENT_MARGIN - INSET_MARGIN,
                bounds.width - 2f * INSET_MARGIN - 2f * CONTENT_MARGIN,
                (2f / 3f) * contentHeight);

        float rowWidth = buttonGridBounds.width;
        float rowHeight = buttonGridBounds.height / 4f;
        float buttonWidth = rowWidth / 3f;
        float x = buttonGridBounds.x;
        float y = buttonGridBounds.y + buttonGridBounds.height - rowHeight;

        // first (top) row
        buyEffectsButton              .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyBallMultiplierButton       .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyCashMultiplierButton       .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;

        // second (middle) row
        y -= rowHeight;
        x = buttonGridBounds.x;
        buyArtPackButton              .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyPegGizmosButton            .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyAudioButton                .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;

        // third (bottom-ish) row
        y -= rowHeight;
        x = buttonGridBounds.x;
        buyLeftSpinnerGizmosButton    .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyBumperGizmosButton         .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;
        buyRightSpinnerGizmosButton   .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;

        // fourth (bottom) row
        y -= rowHeight;
        x = buttonGridBounds.x;
        buyWinGameUpgradeButton       .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;
        startEditingButton            .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;
        startPlayingButton            .setHudBox(x, y, buttonWidth, rowHeight); x += buttonWidth;

        descriptionBox.update(dt);
        costTextBox.update(dt);

        buyWinGameUpgradeButton.isDisabled = buyWinGameUpgradeButton.props.getCurrentCost() > screen.player.score;
        buyEffectsButton.isDisabled = buyEffectsButton.props.getCurrentCost() > screen.player.score;
        buyCashMultiplierButton.isDisabled = buyCashMultiplierButton.props.getCurrentCost() > screen.player.score;
        buyBallMultiplierButton.isDisabled = buyBallMultiplierButton.props.getCurrentCost() > screen.player.score;
        buyArtPackButton.isDisabled = buyArtPackButton.props.getCurrentCost() > screen.player.score;
        buyPegGizmosButton.isDisabled = buyPegGizmosButton.props.getCurrentCost() > screen.player.score;
        buyAudioButton.isDisabled = buyAudioButton.props.getCurrentCost() > screen.player.score;
        buyLeftSpinnerGizmosButton.isDisabled = buyLeftSpinnerGizmosButton.props.getCurrentCost() > screen.player.score;
        buyBumperGizmosButton.isDisabled = buyBumperGizmosButton.props.getCurrentCost() > screen.player.score;
        buyRightSpinnerGizmosButton.isDisabled = buyRightSpinnerGizmosButton.props.getCurrentCost() > screen.player.score;

        buyEffectsButton             .update(dt);
        buyCashMultiplierButton      .update(dt);
        buyBallMultiplierButton      .update(dt);
        buyArtPackButton             .update(dt);
        buyPegGizmosButton           .update(dt);
        buyAudioButton               .update(dt);
        buyLeftSpinnerGizmosButton   .update(dt);
        buyBumperGizmosButton        .update(dt);
        buyRightSpinnerGizmosButton  .update(dt);

        buyWinGameUpgradeButton      .update(dt);
        startEditingButton           .update(dt);
        startPlayingButton           .update(dt);

        // Figure out which button is hovered (if any)
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        screen.worldCamera.unproject(mousePos.set(mouseX, mouseY, 0f));
        hoveredButton = null;
        if      (buyEffectsButton            .collisionBounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyEffectsButton;
        else if (buyCashMultiplierButton     .collisionBounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyCashMultiplierButton;
        else if (buyBallMultiplierButton     .collisionBounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyBallMultiplierButton;
        else if (buyArtPackButton            .collisionBounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyArtPackButton;
        else if (buyPegGizmosButton          .collisionBounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyPegGizmosButton;
        else if (buyAudioButton              .collisionBounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyAudioButton;
        else if (buyLeftSpinnerGizmosButton  .collisionBounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyLeftSpinnerGizmosButton;
        else if (buyBumperGizmosButton       .collisionBounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyBumperGizmosButton;
        else if (buyRightSpinnerGizmosButton .collisionBounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyRightSpinnerGizmosButton;
        else if (buyWinGameUpgradeButton     .collisionBounds.contains(mousePos.x, mousePos.y)) hoveredButton = buyWinGameUpgradeButton;

        descriptionLabel.setX(descriptionBounds.x + INSET_MARGIN);
        descriptionLabel.setY(descriptionBounds.y + descriptionBounds.height - INSET_MARGIN);
        String prevDescriptionText = descriptionText;
        descriptionText = (hoveredButton != null) ? hoveredButton.getDescription() : DEFAULT_DESCRIPTION;
        if (!prevDescriptionText.equals(descriptionText)) {
            descriptionLabel.restart(descriptionText);
        }
        descriptionLabel.update(dt);

        costLabel.setX(costTextBounds.x + INSET_MARGIN);
        costLabel.setY(costTextBounds.y + costTextBounds.height - INSET_MARGIN / 2f);
        String prevCostText = costText;
        costText = (hoveredButton != null) ? getCostTextForButton(hoveredButton) : DEFAULT_COST_TEXT;
        if (!prevCostText.equals(costText)) {
            costLabel.restart(costText);
        }
        costLabel.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isVisible()) return;

        descriptionBox.render(batch);
        descriptionLabel.render(batch);

        costTextBox.render(batch);
        costLabel.render(batch);

        buyEffectsButton             .render(batch);
        buyCashMultiplierButton      .render(batch);
        buyBallMultiplierButton      .render(batch);
        buyArtPackButton             .render(batch);
        buyPegGizmosButton           .render(batch);
        buyAudioButton               .render(batch);
        buyLeftSpinnerGizmosButton   .render(batch);
        buyBumperGizmosButton        .render(batch);
        buyRightSpinnerGizmosButton  .render(batch);

        buyWinGameUpgradeButton.render(batch);
        startEditingButton.render(batch);
        startPlayingButton.render(batch);
    }

    private void initializeButtons() {
        buyEffectsButton.addClickHandler(() -> {
            UpgradeButton button = buyEffectsButton;
            screen.player.score -= button.props.getCurrentCost();
            if (!screen.player.hasEffectParticles) {
                screen.player.hasEffectParticles = true;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (!screen.player.hasEffectTrails) {
                screen.player.hasEffectTrails = true;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (!screen.player.hasEffectScreenshake) {
                screen.player.hasEffectScreenshake = true;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
                button.isDisabled = true;
            }
        });

        buyPegGizmosButton.addClickHandler(() -> {
            UpgradeButton button = buyPegGizmosButton;
            screen.player.score -= button.props.getCurrentCost();
            if (screen.player.pegs == 0) {
                screen.player.pegs = 1;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.pegs == 1) {
                screen.player.pegs = 4;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.pegs == 4) {
                screen.player.pegs = 8;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.pegs == 8) {
                screen.player.pegs = 10;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
                button.isDisabled = true;
            }
        });

        buyBumperGizmosButton.addClickHandler(() -> {
            UpgradeButton button = buyBumperGizmosButton;
            screen.player.score -= button.props.getCurrentCost();
            if (screen.player.bumpers == 0) {
                screen.player.bumpers = 1;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.bumpers == 1) {
                screen.player.bumpers = 4;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.bumpers == 4) {
                screen.player.bumpers = 8;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.bumpers == 8) {
                screen.player.bumpers = 10;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
                button.isDisabled = true;
            }
        });

        buyLeftSpinnerGizmosButton.addClickHandler(() -> {
            UpgradeButton button = buyLeftSpinnerGizmosButton;
            screen.player.score -= button.props.getCurrentCost();
            if (screen.player.leftSpinners == 0) {
                screen.player.leftSpinners = 1;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.leftSpinners == 1) {
                screen.player.leftSpinners = 4;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.leftSpinners == 4) {
                screen.player.leftSpinners = 8;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.leftSpinners == 8) {
                screen.player.leftSpinners = 10;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
                button.isDisabled = true;
            }
        });

        buyRightSpinnerGizmosButton.addClickHandler(() -> {
            UpgradeButton button = buyRightSpinnerGizmosButton;
            screen.player.score -= button.props.getCurrentCost();
            if (screen.player.rightSpinners == 0) {
                screen.player.rightSpinners = 1;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.rightSpinners == 1) {
                screen.player.rightSpinners = 4;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.rightSpinners == 4) {
                screen.player.rightSpinners = 8;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.rightSpinners == 8) {
                screen.player.rightSpinners = 10;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
                button.isDisabled = true;
            }
        });

        buyCashMultiplierButton.addClickHandler(() -> {
            UpgradeButton button = buyCashMultiplierButton;
            screen.player.score -= button.props.getCurrentCost();
            if (screen.player.cashMultiplier == 1) {
                screen.player.cashMultiplier = 2;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.cashMultiplier == 2) {
                screen.player.cashMultiplier = 4;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.cashMultiplier == 4) {
                screen.player.cashMultiplier = 8;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.cashMultiplier == 8) {
                screen.player.cashMultiplier = 16;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
                button.isDisabled = true;
            }
        });

        buyBallMultiplierButton.addClickHandler(() -> {
            UpgradeButton button = buyBallMultiplierButton;
            screen.player.score -= button.props.getCurrentCost();
            if (screen.player.balls == 5) {
                screen.player.balls += 10;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.balls == 15) {
                screen.player.balls += 25;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.balls == 40) {
                screen.player.balls += 50;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.balls == 90) {
                screen.player.balls += 100;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.balls == 190) {
                screen.player.balls += 200;
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
                button.isDisabled = true;
            }
        });

        buyAudioButton.addClickHandler(() -> {
            UpgradeButton button = buyAudioButton;
            screen.player.score -= button.props.getCurrentCost();
            if (screen.player.soundPack == 0) {
                screen.player.upgradeSound();
                screen.game.audio.playMusic(AudioManager.Musics.mainTheme);
                button.props.nextDescription();
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.soundPack == 1) {
                screen.player.upgradeSound();
                button.props.nextDescription();
                screen.game.audio.playMusic(AudioManager.Musics.mainTheme);
                button.addClickParticles();
                // TODO: subtract cost
                button.isDisabled = true;
            }
        });

        buyArtPackButton.addClickHandler(() -> {
            UpgradeButton button = buyArtPackButton;
            screen.player.score -= button.props.getCurrentCost();
            if (screen.player.artPack == ArtPack.a) {
                screen.player.artPack= ArtPack.b;
                descriptionText = button.props.nextDescription();
                descriptionLabel = new TypingLabel(screen.assets, descriptionText, 0f, 300f);
                descriptionLabel.setLineAlign(Align.left);
                descriptionLabel.setWidth(screen.worldCamera.viewportWidth - 4f * INSET_MARGIN);
                descriptionLabel.setText(descriptionText);
                costLabel = new TypingLabel(screen.assets, costText, 0f, 300f);
                costLabel.setLineAlign(Align.center);
                costLabel.setWidth(screen.worldCamera.viewportWidth - 4f * INSET_MARGIN);
                costLabel.setText(costText);
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.artPack == ArtPack.b) {
                screen.player.artPack = ArtPack.c;
                descriptionText = button.props.nextDescription();
                descriptionLabel = new TypingLabel(screen.assets, descriptionText, 0f, 300f);
                descriptionLabel.setLineAlign(Align.left);
                descriptionLabel.setWidth(screen.worldCamera.viewportWidth - 4f * INSET_MARGIN);
                descriptionLabel.setText(descriptionText);
                costLabel = new TypingLabel(screen.assets, costText, 0f, 300f);
                costLabel.setLineAlign(Align.center);
                costLabel.setWidth(screen.worldCamera.viewportWidth - 4f * INSET_MARGIN);
                costLabel.setText(costText);
                button.addClickParticles();
                // TODO: subtract cost
            } else if (screen.player.artPack == ArtPack.c) {
                screen.player.artPack = ArtPack.d;
                descriptionText = button.props.nextDescription();
                descriptionLabel = new TypingLabel(screen.assets, descriptionText, 0f, 300f);
                descriptionLabel.setLineAlign(Align.left);
                descriptionLabel.setWidth(screen.worldCamera.viewportWidth - 4f * INSET_MARGIN);
                descriptionLabel.setText(descriptionText);
                costLabel = new TypingLabel(screen.assets, costText, 0f, 300f);
                costLabel.setLineAlign(Align.center);
                costLabel.setWidth(screen.worldCamera.viewportWidth - 4f * INSET_MARGIN);
                costLabel.setText(costText);
                button.addClickParticles();
                // TODO: subtract cost
                button.isDisabled = true;
            }
        });

        buyWinGameUpgradeButton.addClickHandler(() -> {
            UpgradeButton button = buyWinGameUpgradeButton;
            button.addClickParticles();
            screen.game.setScreen(new WinnerScreen(screen.game));
        });
    }

    private String getCostTextForButton(UpgradeButton button) {
        return "{GRADIENT=red;blue}Upgrade Cost: $" + button.props.getCurrentCost() + " {ENDGRADIENT}   "
             + "{GRADIENT=forest;olive}Available Cash: $ " + screen.player.score + " {ENDGRADIENT} ";
    }

}
