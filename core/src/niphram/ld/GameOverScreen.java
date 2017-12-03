package niphram.ld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Niphram on 03.12.2017.
 */
public class GameOverScreen implements Screen, InputProcessor {
    
    private Game m_Game;
    
    private Sprite m_Background;
    
    private OrthographicCamera m_Cam;
    
    private float m_Score;
    private BitmapFont m_Font;
    
    public GameOverScreen(Game game, float score) {
        m_Game = game;
        m_Score = score;
        
        m_Cam = new OrthographicCamera(1280 / 8, 720 / 8);
        
        m_Background = new Sprite(TextureManager.p_GameOver);
        m_Background.setSize(1280 / 8, 720 / 8);
        m_Background.setOriginCenter();
        m_Background.setCenter(0, 0);
        
        m_Font = new BitmapFont();
        m_Font.setColor(Color.WHITE.cpy());
    
        Gdx.input.setInputProcessor(this);
    }
    
    @Override
    public void show() {
        
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);
        
        m_Cam.update();
        m_Game.p_Batch.setProjectionMatrix(m_Cam.combined);
        
        m_Game.p_Batch.begin();
        
        m_Background.draw(m_Game.p_Batch);
    
        double s = (double) Math.round(((double) m_Score * 100)) / 100;
        GlyphLayout layout = new GlyphLayout(m_Font, s + "");
        
        m_Font.draw(m_Game.p_Batch, layout, -(layout.width / 2), -3);
        
        m_Game.p_Batch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        
    }
    
    @Override
    public void pause() {
        
    }
    
    @Override
    public void resume() {
        
    }
    
    @Override
    public void hide() {
        
    }
    
    @Override
    public void dispose() {
        m_Font.dispose();
    }
    
    @Override
    public boolean keyDown(int keycode) {
        m_Game.setScreen(new MainMenu(m_Game));
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
        m_Game.setScreen(new MainMenu(m_Game));
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
