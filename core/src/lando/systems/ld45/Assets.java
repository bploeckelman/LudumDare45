package lando.systems.ld45;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.AssetType;
import lando.systems.ld45.utils.UIAssetType;

import java.util.Arrays;
import java.util.stream.Stream;

public class Assets {

    private final AssetDescriptor<Texture> pixelTextureAsset = new AssetDescriptor<>("images/pixel.png", Texture.class);
    private final AssetDescriptor<Texture> gridpaperTextureAsset = new AssetDescriptor<>("images/gridpaper.png", Texture.class);
    private final AssetDescriptor<Texture> pathGradientTextureAsset = new AssetDescriptor<>("images/path-gradient.png", Texture.class);
    private final AssetDescriptor<Texture> crosshatchGradientTextureAsset = new AssetDescriptor<>("images/crosshatch-gradient.png", Texture.class);
    private final AssetDescriptor<Texture> laserTextureAsset = new AssetDescriptor<>("images/laser.png", Texture.class);
    private final AssetDescriptor<TextureAtlas> atlasAsset = new AssetDescriptor<>("images/sprites.atlas", TextureAtlas.class);
    private final AssetDescriptor<BitmapFont> pixelFont16Asset = new AssetDescriptor<>("fonts/chevyray-column-16.fnt", BitmapFont.class);
    private final AssetDescriptor<BitmapFont> fontArtPackAssetA = new AssetDescriptor<>("fonts/blahblahblah.fnt", BitmapFont.class);
    private final AssetDescriptor<BitmapFont> fontArtPackAssetB = new AssetDescriptor<>("fonts/chevyray-bubble-time.fnt", BitmapFont.class);
    private final AssetDescriptor<BitmapFont> fontArtPackAssetC = new AssetDescriptor<>("fonts/dignity-of-labor.fnt", BitmapFont.class);
    private final AssetDescriptor<BitmapFont> fontArtPackAssetD = new AssetDescriptor<>("fonts/destructobeam.fnt", BitmapFont.class);

    public Music music;

    public SpriteBatch batch;
    public GlyphLayout layout;
    public BitmapFont font;
    public AssetManager mgr;
    public TextureAtlas atlas;

    public Texture debugTexture;
    public Texture pixel;
    public Texture ballTrailTexture;
    public Texture pathGradientTexture;
    public Texture crossHatchGradientTexture;
    public Texture gridPaper;

    public TextureRegion whitePixel;
    public TextureRegion whiteCircle;

    public TextureRegion uiCursorHand;
    public Array<TextureRegion> equations;


    public NinePatch buildArea;

    public ObjectMap<Integer, Animation<TextureRegion>> fontPoints;

    public ObjectMap<ArtPack, ObjectMap<AssetType, Animation<TextureRegion>>> assetMap;
    public ObjectMap<ArtPack, ObjectMap<UIAssetType, NinePatch>> uiAssetNinepatchMap;
    public ObjectMap<ArtPack, BitmapFont> fontMap;

    public Animation<TextureRegion> scribble;

    public ShaderProgram ballTrailShader;
    public ShaderProgram hexGridShader;


    public boolean initialized;

    public Assets() {
        initialized = false;

        batch = new SpriteBatch();
        layout = new GlyphLayout();


        mgr = new AssetManager();
        mgr.load(atlasAsset);
        mgr.load(pixelTextureAsset);
        mgr.load(pathGradientTextureAsset);
        mgr.load(crosshatchGradientTextureAsset);
        mgr.load(laserTextureAsset);
        mgr.load(pixelFont16Asset);
        mgr.load(fontArtPackAssetA);
        mgr.load(fontArtPackAssetB);
        mgr.load(fontArtPackAssetC);
        mgr.load(fontArtPackAssetD);
        mgr.load(gridpaperTextureAsset);

        mgr.load("audio/music.mp3", Music.class);

        mgr.load("images/badlogic.jpg", Texture.class);

        mgr.finishLoading();
        load();
    }

    public float load() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1;

        initialized = true;

        music = mgr.get("audio/music.mp3", Music.class);

        pixel = mgr.get(pixelTextureAsset);
        gridPaper = mgr.get(gridpaperTextureAsset);
        gridPaper.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pathGradientTexture = mgr.get(pathGradientTextureAsset);
        crossHatchGradientTexture = mgr.get(crosshatchGradientTextureAsset);
        ballTrailTexture = mgr.get(laserTextureAsset);
        debugTexture = mgr.get("images/badlogic.jpg", Texture.class);

        font = mgr.get(pixelFont16Asset);

        atlas = mgr.get(atlasAsset);
        whitePixel = atlas.findRegion("white-pixel");
        whiteCircle = atlas.findRegion("white-circle");
        equations = new Array<>();
        for (int i = 0; i < 7; i++){
            equations.add(atlas.findRegion("equation"+i));
        }

        assetMap = new ObjectMap<>();
        uiAssetNinepatchMap = new ObjectMap<>();
        for (ArtPack artPack : ArtPack.values()) {
            assetMap.put(artPack, new ObjectMap<>());
            for (AssetType assetType : AssetType.values()) {
                String assetName = assetType.fileName + "-" + artPack.name();
                assetMap.get(artPack).put(assetType, new Animation<>(0.1f, atlas.findRegions(assetName), Animation.PlayMode.LOOP));
            }

            String fileName;
            uiAssetNinepatchMap.put(artPack, new ObjectMap<>());
            fileName = UIAssetType.toychest_panel.fileName + "-" + artPack.name();
            uiAssetNinepatchMap.get(artPack).put(UIAssetType.toychest_panel, new NinePatch(atlas.findRegion(fileName), 10, 10, 10, 10));
            fileName = UIAssetType.toychest_panel_inset.fileName + "-" + artPack.name();
            uiAssetNinepatchMap.get(artPack).put(UIAssetType.toychest_panel_inset, new NinePatch(atlas.findRegion(fileName), 6, 6, 6, 6));
            fileName = UIAssetType.upgrade_panel.fileName + "-" + artPack.name();
            uiAssetNinepatchMap.get(artPack).put(UIAssetType.upgrade_panel, new NinePatch(atlas.findRegion(fileName), 10, 10, 10, 10));
            fileName = UIAssetType.upgrade_panel_inset.fileName + "-" + artPack.name();
            uiAssetNinepatchMap.get(artPack).put(UIAssetType.upgrade_panel_inset, new NinePatch(atlas.findRegion(fileName), 6, 6, 6, 6));
        }

        fontMap = new ObjectMap<>();
        fontMap.put(ArtPack.a, mgr.get(fontArtPackAssetA));
        fontMap.put(ArtPack.b, mgr.get(fontArtPackAssetB));
        fontMap.put(ArtPack.c, mgr.get(fontArtPackAssetC));
        fontMap.put(ArtPack.d, mgr.get(fontArtPackAssetD));

        scribble = new Animation<>(0.1f, atlas.findRegions("scribble-a"));

        uiCursorHand = atlas.findRegion("ui-cursor-hand");
        buildArea = new NinePatch(atlas.findRegion("redbox"), 4, 4, 4, 4);

        fontPoints = new ObjectMap<>();
        for (int i = 0; i <= 9; ++i) {
            fontPoints.put(i, new Animation<>(0.1f, atlas.findRegions("font-points-" + i)));
        }

        ballTrailShader = loadShader("shaders/standardMesh.vert", "shaders/ballTrailMesh.frag");
        hexGridShader = loadShader("shaders/standard.vert", "shaders/hexGrid.frag");

        return 1;
    }

    private static ShaderProgram loadShader(String vertSourcePath, String fragSourcePath) {
        ShaderProgram.pedantic = false;
        ShaderProgram shaderProgram = new ShaderProgram(
                Gdx.files.internal(vertSourcePath),
                Gdx.files.internal(fragSourcePath));

        if (!shaderProgram.isCompiled()) {
            Gdx.app.error("LoadShader", "compilation failed:\n" + shaderProgram.getLog());
            throw new GdxRuntimeException("LoadShader: compilation failed:\n" + shaderProgram.getLog());
        } else if (Config.shaderDebug){
            Gdx.app.debug("LoadShader", "ShaderProgram compilation log: " + shaderProgram.getLog());
        }

        return shaderProgram;
    }

}
