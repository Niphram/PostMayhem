package niphram.ld;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Niphram on 02.12.2017.
 */
public class Game extends com.badlogic.gdx.Game {
    
    public SpriteBatch p_Batch;
    
    @Override
    public void create() {
        TextureManager.init();
        SoundManager.init();
        
        p_Batch = new SpriteBatch();
    
        SoundManager.p_BackgroundMusic.play();
        SoundManager.p_BackgroundMusic.setVolume(0.1f);
        
        this.setScreen(new MainMenu(this));
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
    
    @Override
    public void render() {
        super.render();
    }
    
    @Override
    public void pause() {
        super.pause();
    }
    
    @Override
    public void resume() {
        super.resume();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        SoundManager.p_BackgroundMusic.dispose();
    }
}
