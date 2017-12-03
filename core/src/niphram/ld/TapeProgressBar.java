package niphram.ld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Niphram on 02.12.2017.
 */
public class TapeProgressBar {
    
    private Sprite m_TapeSprite;
    private Sprite m_EndSprite;
    
    private float m_Width = 1f;
    private float m_Height = 0.5f;
    private float m_XPos = 0f;
    private float m_YPos = 0f;
    private float m_Progress = 1f;
    private float m_ProgressDisplay = 1f;
    private Color m_Color = Color.WHITE.cpy();
    
    public TapeProgressBar(float x, float y, float width, float height, float progress) {
        m_XPos = x;
        m_YPos = y;
        m_Width = width;
        m_Height = height;
        m_Progress = progress;
        
        createSprite(TextureManager.p_Tape, TextureManager.p_TapeEnd);
    }
    
    public void setProgress(float value) {
        m_Progress = value;
    }
    
    public float getProgress() {
        return m_Progress;
    }
    
    private void createSprite(Texture tape, Texture end) {
        m_TapeSprite = new Sprite(tape);
        m_TapeSprite.setPosition(m_XPos, m_YPos);
        m_TapeSprite.setSize(m_Width, m_Height);
        
        m_EndSprite = new Sprite(end);
        m_EndSprite.setPosition(m_XPos + m_Width, m_YPos);
        m_EndSprite.setOrigin(0, 4);
        m_EndSprite.setSize(m_Height, m_Height);
        
        updateSprite();
    }
    
    private void updateSprite() {
        m_TapeSprite.setRegion(-(m_Width * m_ProgressDisplay) / m_Height, 0, 1, 1);
        m_TapeSprite.setSize(m_Width * m_ProgressDisplay, m_Height);
        m_TapeSprite.setPosition(m_XPos, m_YPos);
        m_TapeSprite.setCenterY(m_YPos);
        
        m_EndSprite.setX(m_XPos + m_Width * m_ProgressDisplay);
        m_EndSprite.setCenterY(m_YPos);
        
        m_TapeSprite.setColor(m_Color);
        m_EndSprite.setColor(m_Color);
    }
        
    public void draw(SpriteBatch batch) {
        m_Color.lerp(Color.WHITE, 0.1f);
        m_ProgressDisplay = (MathUtils.lerp(m_ProgressDisplay, m_Progress, 0.25f));
        
        updateSprite();
        m_TapeSprite.draw(batch);
        m_EndSprite.draw(batch);
    }
    
    public void blink() {
        m_Color.set(Color.RED.cpy());
    }
}
