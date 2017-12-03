package niphram.ld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Niphram on 02.12.2017.
 */
public class TextureManager {
    
    public static Texture p_RectPackage;
    public static Texture p_RoundPackage;
    public static Texture p_Tape;
    public static Texture p_TapeEnd;
    public static Texture p_Palette;
    public static Texture p_Pixel;
    public static Texture p_Background;
    public static Texture p_GameOver;
    public static Texture p_MainMenu;
    
    public static void init() {
        p_RectPackage = new Texture(Gdx.files.internal("package.png"));
        p_RoundPackage = new Texture(Gdx.files.internal("packageRound.png"));
        p_Tape = new Texture(Gdx.files.internal("tape.png"));
        p_TapeEnd = new Texture(Gdx.files.internal("tapeEnd.png"));
        p_Palette = new Texture(Gdx.files.internal("palette.png"));
        p_Pixel = new Texture(Gdx.files.internal("pixel.png"));
        p_Background = new Texture(Gdx.files.internal("background.png"));
        p_GameOver = new Texture(Gdx.files.internal("gameOver.png"));
        p_MainMenu = new Texture(Gdx.files.internal("mainMenu.png"));
        p_Tape.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }
    
}
