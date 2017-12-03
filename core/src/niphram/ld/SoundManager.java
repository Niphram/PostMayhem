package niphram.ld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Niphram on 03.12.2017.
 */
public class SoundManager {
    
    public static Sound p_BoxSound;
    public static Sound p_TapeSound;
    public static Sound p_BoxDestroy;
    public static Sound p_PaletteMove;
    
    public static Music p_BackgroundMusic;
    
    public static void init() {
        p_BoxSound = Gdx.audio.newSound(Gdx.files.internal("boxSound.ogg"));
        p_TapeSound = Gdx.audio.newSound(Gdx.files.internal("tapeSound.ogg"));
        p_BoxDestroy = Gdx.audio.newSound(Gdx.files.internal("boxDestroy.wav"));
        p_PaletteMove = Gdx.audio.newSound(Gdx.files.internal("paletteMove.wav"));
        
        p_BackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.wav"));
        p_BackgroundMusic.setLooping(true);
    }
    
}
