package lando.systems.ld45;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Assets {

    private final AssetDescriptor<Texture> pixelTextureAsset = new AssetDescriptor<>("images/pixel.png", Texture.class);
    private final AssetDescriptor<TextureAtlas> atlasAsset = new AssetDescriptor<>("images/sprites.atlas", TextureAtlas.class);


    public Music music;

    public SpriteBatch batch;
    public GlyphLayout layout;
    public AssetManager mgr;
    public TextureAtlas atlas;

    public Texture debugTexture;
    public Texture pixel;

    public TextureRegion whitePixel;
    public TextureRegion whiteCircle;

    public boolean initialized;

    public Assets() {
        initialized = false;

        batch = new SpriteBatch();
        layout = new GlyphLayout();


        mgr = new AssetManager();
        mgr.load(atlasAsset);
        mgr.load(pixelTextureAsset);

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
        debugTexture = mgr.get("images/badlogic.jpg", Texture.class);

        atlas = mgr.get(atlasAsset);
        whitePixel = atlas.findRegion("white-pixel");
        whiteCircle = atlas.findRegion("white-circle");

        return 1;
    }
}
