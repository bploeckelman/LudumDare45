package lando.systems.ld45;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Assets {

    private final AssetDescriptor<Texture> pixelTextureAsset = new AssetDescriptor<>("images/pixel.png", Texture.class);
    private final AssetDescriptor<Texture> pathGradientTextureAsset = new AssetDescriptor<>("images/path-gradient.png", Texture.class);
    private final AssetDescriptor<Texture> laserTextureAsset = new AssetDescriptor<>("images/laser.png", Texture.class);
    private final AssetDescriptor<TextureAtlas> atlasAsset = new AssetDescriptor<>("images/sprites.atlas", TextureAtlas.class);
    private final AssetDescriptor<BitmapFont> pixelFont16Asset = new AssetDescriptor<>("fonts/chevyray-column-16.fnt", BitmapFont.class);


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

    public TextureRegion whitePixel;
    public TextureRegion whiteCircle;

    public TextureRegion[] bumpers;

    public TextureRegion[][] pegs;
    public TextureRegion[][] spinners;

    public ShaderProgram ballTrailShader;

    public boolean initialized;

    public Assets() {
        initialized = false;

        batch = new SpriteBatch();
        layout = new GlyphLayout();


        mgr = new AssetManager();
        mgr.load(atlasAsset);
        mgr.load(pixelTextureAsset);
        mgr.load(pathGradientTextureAsset);
        mgr.load(laserTextureAsset);
        mgr.load(pixelFont16Asset);

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
        pathGradientTexture = mgr.get(pathGradientTextureAsset);
        ballTrailTexture = mgr.get(laserTextureAsset);
        debugTexture = mgr.get("images/badlogic.jpg", Texture.class);

        font = mgr.get(pixelFont16Asset);

        atlas = mgr.get(atlasAsset);
        whitePixel = atlas.findRegion("white-pixel");
        whiteCircle = atlas.findRegion("white-circle");

        bumpers = new TextureRegion[] {
                atlas.findRegion("bumper-a"),
                atlas.findRegion("bumper-b"),
                atlas.findRegion("bumper-c")
        };

        pegs = new TextureRegion[][] {
            new TextureRegion[]{
                atlas.findRegion("peg-a-1"),
                atlas.findRegion("peg-b-1"),
                atlas.findRegion("peg-c-1"),
                atlas.findRegion("peg-d-1")
            },
            new TextureRegion[] {
                atlas.findRegion("peg-a-2"),
                atlas.findRegion("peg-b-2"),
                atlas.findRegion("peg-c-2"),
                atlas.findRegion("peg-d-2")
            },
             new TextureRegion[] {
                atlas.findRegion("peg-a-3"),
                atlas.findRegion("peg-b-3"),
                atlas.findRegion("peg-c-3"),
                atlas.findRegion("peg-d-3")
            }
        };

        spinners = new TextureRegion[][] {
                new TextureRegion[]{
                        atlas.findRegion("spinner-a-1"),
                        atlas.findRegion("spinner-b-1"),
                        atlas.findRegion("spinner-c-1"),
                        atlas.findRegion("spinner-d-1")
                },
                new TextureRegion[] {
                        atlas.findRegion("spinner-a-2"),
                        atlas.findRegion("spinner-b-2"),
                        atlas.findRegion("spinner-c-2"),
                        atlas.findRegion("spinner-d-2")
                },
                new TextureRegion[] {
                        atlas.findRegion("spinner-a-3"),
                        atlas.findRegion("spinner-b-3"),
                        atlas.findRegion("spinner-c-3"),
                        atlas.findRegion("spinner-d-3")
                }
        };

        ballTrailShader = loadShader("shaders/standardMesh.vert", "shaders/ballTrailMesh.frag");

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
