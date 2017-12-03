package niphram.ld.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import niphram.ld.SoundManager;
import niphram.ld.TextureManager;

/**
 * Created by Niphram on 02.12.2017.
 */
public class Tape {
    
    private RopeJoint m_Joint;
    private Sprite m_Sprite;
    
    private float m_Scale = .1f;
    
    private Vector2 m_AnchorA = new Vector2();
    private Vector2 m_AnchorB = new Vector2();
    
    public Tape(Body a, Body b, Vector2 anchorA, Vector2 anchorB, World world) {
        SoundManager.p_TapeSound.play(1.0f);
        createJoint(a, b, anchorA, anchorB, world);
        createSprite(TextureManager.p_Tape);
    }
    
    public Joint getJoint() {
        return m_Joint;
    }
    
    private void createJoint(Body a, Body b, Vector2 anchorA, Vector2 anchorB, World world) {
        RopeJointDef jointDef = new RopeJointDef();
        jointDef.bodyA = a;
        jointDef.bodyB = b;
        jointDef.localAnchorA.set(anchorA);
        jointDef.localAnchorB.set(anchorB);
    
        m_AnchorA = a.getWorldPoint(anchorA);
        m_AnchorB = b.getWorldPoint(anchorB);
        
        jointDef.maxLength = Math.abs(m_AnchorA.cpy().sub(m_AnchorB).len());
        jointDef.collideConnected = true;
    
        m_Joint = (RopeJoint) world.createJoint(jointDef);
    }
    
    private float getLength() {
        return Math.abs(m_Joint.getAnchorA().sub(m_Joint.getAnchorB()).len());
    }
    
    private void createSprite(Texture tex) {
        m_Sprite = new Sprite(tex);
        m_Sprite.setOriginCenter();
    
        m_Sprite.setRegion(0, 0, getLength() / m_Scale, 1);
        updateSprite();
    }
    
    private void updateSprite() {
        m_Sprite.setSize(getLength(), m_Scale);
        m_Sprite.setOriginCenter();
    
        m_AnchorA = m_Joint.getAnchorA();
        m_AnchorB = m_Joint.getAnchorB();
        
        Vector2 jointCenter = m_AnchorA.cpy().add(m_AnchorB).scl(0.5f);
        float jointAngle = m_AnchorA.sub(m_AnchorB).angle();
        
        m_Sprite.setCenter(jointCenter.x, jointCenter.y);
        m_Sprite.setRotation(jointAngle);
    }
    
    public void draw(SpriteBatch batch) {
        updateSprite();
        m_Sprite.draw(batch);
    }
}
