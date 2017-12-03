package niphram.ld.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import niphram.ld.ShapeUtils;
import niphram.ld.TextureManager;

/**
 * Created by Niphram on 02.12.2017.
 */
public class Package {

    private Sprite m_Sprite;
    private Body m_Body;

    private float m_Width;
    private float m_Height;
    
    public Package(float x, float y, float width, float height, World world) {
        
        m_Width = width;
        m_Height = height;
    
        Shape shape = ShapeUtils.createRectangle(width, height);
        createBody(x, y, world, shape);
        shape.dispose();
        
        createSprite(TextureManager.p_RectPackage);
    }
    
    public Package(float x, float y, float radius, World world) {
        m_Width = radius;
        m_Height = radius;
        
        Shape shape = ShapeUtils.createCircle(radius);
        createBody(x, y, world, shape);
        shape.dispose();
    
        createSprite(TextureManager.p_RoundPackage);
    }
    
    public Body getBody() {
        return m_Body;
    }
    
    private void createBody(float x, float y, World world, Shape shape) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.angularDamping = 1f;
        bodyDef.angularDamping = 1f;
    
        m_Body = world.createBody(bodyDef);
        
        FixtureDef fixDef = new FixtureDef();
        fixDef.density = 1;
        fixDef.friction = 0.5f;
        fixDef.restitution = 0;
        fixDef.shape = shape;
    
        m_Body.createFixture(fixDef);
    }
    
    private void createSprite(Texture tex) {
        m_Sprite = new Sprite(tex);
        m_Sprite.setOriginCenter();
        m_Sprite.setSize(m_Width * 2, m_Height * 2);
        m_Sprite.setOriginCenter();
    }
    
    public void draw(SpriteBatch batch) {
        m_Sprite.setCenter(m_Body.getPosition().x, m_Body.getPosition().y);
        m_Sprite.setRotation(m_Body.getAngle() / MathUtils.degreesToRadians);
        m_Sprite.draw(batch);
    }
}
