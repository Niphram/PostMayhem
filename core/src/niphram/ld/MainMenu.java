package niphram.ld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Niphram on 03.12.2017.
 */
public class MainMenu implements Screen, InputProcessor {
    
    private Game m_Game;
    
    private OrthographicCamera m_Cam;
    private Sprite m_Sprite;
    
    public MainMenu(Game game) {
        m_Game = game;
        
        m_Cam = new OrthographicCamera(16, 9);
        
        m_Sprite = new Sprite(TextureManager.p_MainMenu);
        m_Sprite.setSize(16, 9);
        m_Sprite.setOriginCenter();
        m_Sprite.setCenter(0, 0);
        
        Gdx.input.setInputProcessor(this);
    
    }
    
    @Override
    public void show() { }
    
    @Override
    public void render(float delta) {
    
        SoundManager.p_BackgroundMusic.setVolume(MathUtils.lerp(SoundManager.p_BackgroundMusic.getVolume(), 0.1f, 0.01f));
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);
        
        m_Cam.update();
        m_Game.p_Batch.setProjectionMatrix(m_Cam.combined);
        
        m_Game.p_Batch.begin();
        
        m_Sprite.draw(m_Game.p_Batch);
        
        m_Game.p_Batch.end();
    }
    
    @Override
    public void resize(int width, int height) { }
    
    @Override
    public void pause() { }
    
    @Override
    public void resume() { }
    
    @Override
    public void hide() { }
    
    @Override
    public void dispose() { }
    
    @Override
    public boolean keyDown(int keycode) {
        m_Game.setScreen(new GameScreen(m_Game));
        return false;
    }
    
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        m_Game.setScreen(new GameScreen(m_Game));
        return false;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
