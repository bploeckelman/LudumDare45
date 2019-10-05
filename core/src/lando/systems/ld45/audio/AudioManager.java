package lando.systems.ld45.audio;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Sine;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld45.Assets;
import lando.systems.ld45.Game;

public class AudioManager implements Disposable {

    public static final float MUSIC_VOLUME = .6f;
    public static final boolean shutUpYourFace = false;
    public static final boolean shutUpYourTunes = false;

    public enum Sounds {
        boink
    }

    public enum Musics {
        mainTheme
    }

    public ObjectMap<Sounds, SoundContainer> sounds = new ObjectMap<>();
    public ObjectMap<Musics, Music> musics = new ObjectMap<>();

    public Music currentMusic;
    public MutableFloat musicVolume;
    public Musics eCurrentMusic;
    public Music oldCurrentMusic;

    private Assets assets;
    private TweenManager tween;

    public AudioManager(boolean playMusic, Game game) {
        this.assets = game.assets;
        this.tween = game.tween;

        musics.put(Musics.mainTheme, assets.music);
        musicVolume = new MutableFloat(MUSIC_VOLUME);

        if (playMusic) {
            currentMusic = musics.get(Musics.mainTheme);
            eCurrentMusic = Musics.mainTheme;
            currentMusic.setLooping(true);
            currentMusic.setVolume(0f);
            currentMusic.play();
            setMusicVolume(MUSIC_VOLUME, 2f);
            // currentMusic.setOnCompletionListener(nextSong);
        }
    }

        public void update(float dt){
        if (currentMusic != null) {
            if (musicVolume.floatValue() == 0f) {
                if (oldCurrentMusic != null) oldCurrentMusic.stop();
                setMusicVolume(MUSIC_VOLUME, 1f);
                currentMusic.play();
            }

            currentMusic.setVolume(musicVolume.floatValue());
        }

        if (oldCurrentMusic != null) {
            oldCurrentMusic.setVolume(musicVolume.floatValue());
        }
    }

    @Override
    public void dispose() {
        Sounds[] allSounds = Sounds.values();
        for (Sounds sound : allSounds) {
            if (sounds.get(sound) != null) {
                sounds.get(sound).dispose();
            }
        }
        Musics[] allMusics = Musics.values();
        for (Musics music : allMusics) {
            if (musics.get(music) != null) {
                musics.get(music).dispose();
            }
        }
        currentMusic = null;
    }

    public void putSound(Sounds soundType, Sound sound) {
        SoundContainer soundCont = sounds.get(soundType);
        //Array<Sound> soundArr = sounds.get(soundType);
        if (soundCont == null) {
            soundCont = new SoundContainer();
        }

        soundCont.addSound(sound);
        sounds.put(soundType, soundCont);
    }

    public long playSound(Sounds soundOption) {
        if (shutUpYourFace) return -1;

        SoundContainer soundCont = sounds.get(soundOption);
        Sound s = soundCont.getSound();
        return (s != null) ? s.play(0.1f) : 0;
    }

    public void playMusic(Musics musicOption) {
        // Stop currently running music
        if (currentMusic != null && eCurrentMusic != musicOption) {
            setMusicVolume(0f, 1f);
            oldCurrentMusic = currentMusic;
            //currentMusic.stop();
        }

        boolean currMusicNull = currentMusic == null;
        // Set specified music track as current and play it
        currentMusic = musics.get(musicOption);
        eCurrentMusic = musicOption;
        currentMusic.setLooping(true);
        if (currMusicNull) {
            currentMusic.play();
        }
        //setMusicVolume(MUSIC_VOLUME, 1f);
        //currentMusic.play();
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void stopSound(Sounds soundOption) {
        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont != null) {
            soundCont.stopSound();
        }
    }

    public void stopAllSounds() {
        for (SoundContainer soundCont : sounds.values()) {
            if (soundCont != null) {
                soundCont.stopSound();
            }
        }
    }

    public void setMusicVolume(float level, float duration) {
        Tween.to(musicVolume, 1, duration).target(level).ease(Sine.IN).start(tween);

    }
}
