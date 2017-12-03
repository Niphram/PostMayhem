package niphram.ld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import niphram.ld.entities.Package;
import niphram.ld.entities.Tape;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Niphram on 02.12.2017.
 */
public class GameScreen implements Screen, InputProcessor, ContactListener {
    
    private Game m_Game;
    
    /* Cameras */
    private OrthographicCamera m_Camera;
    private OrthographicCamera m_GuiCam;
    
    /* Objects */
    private ArrayList<Package> m_Packages = new ArrayList<Package>();
    private ArrayList<Tape> m_Tapes = new ArrayList<Tape>();
    
    /* Box2D */
    private World m_World;
    
    private Body m_GroundBody;
    private Body m_PaletteBody;
    
    /* Drag-and-drop */
    private Body m_HitBody;
    private Vector2 m_HitAnchor = new Vector2();
    private Vector3 m_TestPoint = new Vector3();
    private boolean m_RightDrag;
    private MouseJoint m_MouseJoint;
    
    /* Game variables */
    private float m_PackageTimer = 1f;
    private float m_PaletteTimer = 60f;
    private int m_PaletteState = 0;
    private float m_Difficulty = 1f;
    private float m_TapeAmount = 5f;
    
    private float m_Score = 10.0f;
    private float m_PlayTime = 0;//TimeUtils.millis();
    
    /* Graphics */
    private BitmapFont m_Font;
    private Sprite m_BackgroundSprite;
    private Sprite m_PaletteSprite;
    private TapeProgressBar m_TapeProgress;
    
    /* Callback */
    private QueryCallback m_Callback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            if (fixture.testPoint(m_TestPoint.x, m_TestPoint.y)) {
                m_HitBody = fixture.getBody();
                return false;
            }
            return true;
        }
    };
    
    public GameScreen(Game game) {
        m_Game = game;
        
        initBox2D();
        initGraphics();
        
        Gdx.input.setInputProcessor(this);
    }
    
    private void initGraphics() {
        m_Camera = new OrthographicCamera(8, 4.5f);
        m_GuiCam = new OrthographicCamera(1280 / 4, 720 / 4);
        
        m_TapeProgress = new TapeProgressBar(-4, 2, 7.5f, .25f, 1);
        
        m_Font = new BitmapFont();
        m_Font.setColor(Color.BLACK.cpy());
        
        initPaletteSprite();
        initBackgroundSprite();
    }
    
    private void initPaletteSprite() {
        m_PaletteSprite = new Sprite(TextureManager.p_Palette);
        m_PaletteSprite.setSize(1, 1);
        m_PaletteSprite.setOriginCenter();
        m_PaletteSprite.setCenter(-2, -2.5f);
    }
    
    private void initBackgroundSprite() {
        m_BackgroundSprite = new Sprite(TextureManager.p_Background);
        m_BackgroundSprite.setSize(8, 4.5f);
        m_BackgroundSprite.setOriginCenter();
        m_BackgroundSprite.setCenter(0, 0);
        m_BackgroundSprite.setColor(Color.LIGHT_GRAY.cpy());
    }
    
    private void initBox2D() {
        Box2D.init();
        
        m_World = new World(new Vector2(0, -10), true);
    
        m_World.setContactListener(this);
        
        // Empty Body
        BodyDef bodyDef = new BodyDef();
        m_GroundBody = m_World.createBody(bodyDef);
        
        initPalette();
        initTable();
    }
    
    private void initPalette() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(-2, -2.5f);
        
        m_PaletteBody = m_World.createBody(bodyDef);
        
        Shape shape = ShapeUtils.createRectangle(.5f, .5f);
        
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.friction = 1f;
        
        m_PaletteBody.createFixture(fixDef);
        
        shape.dispose();
    }
    
    private void initTable() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(6.25f, -1.65f);
        
        Body b = m_World.createBody(bodyDef);
        
        Shape shape = ShapeUtils.createRectangle(5, 1);
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.friction = 1f;
        
        b.createFixture(fixDef);
        
        shape.dispose();
    }
    
    private void spawnPackage() {
        float width = MathUtils.random(0.15f, 0.4f);
        float height = MathUtils.random(0.15f, 0.4f);
    
        switch (MathUtils.random(3)) {
            default:
            case 0:
                m_Packages.add(new Package(5, 0, width, height, m_World));
                break;
            case 1:
                m_Packages.add(new Package(5, 0, width, m_World));
                break;
        }
    }
    
    private Package getPackageByBody(Body b) {
        for (Package p : m_Packages) {
            if (p.getBody().equals(b)) {
                return p;
            }
        }
        return null;
    }
    
    // Really bad
    private void movePalette() {
        switch (m_PaletteState) {
            case 0:
                SoundManager.p_PaletteMove.play(0.75f);
    
                for (Package p : m_Packages) {
                    if (p.getBody().getPosition().x < 0) {
                        WeldJointDef def = new WeldJointDef();
                        def.initialize(p.getBody(), m_PaletteBody, m_PaletteBody.getPosition());
                        m_World.createJoint(def);
                    }
                }
                
                m_PaletteBody.setLinearVelocity(-2, 0);
                m_PaletteState = 1;
                break;
            case 1:
                if (m_PaletteBody.getPosition().x < -5) {
                    m_PaletteState = 2;
                    m_PaletteBody.setLinearVelocity(0, 1);
                    m_PaletteBody.setTransform(-2, -3, 0);
    
                    ArrayList<Package> packages = new ArrayList<Package>();
    
                    for (JointEdge je : m_PaletteBody.getJointList()) {
                        packages.add(getPackageByBody(je.other));
                    }
    
                    for (Iterator<Package> pack = packages.iterator(); pack.hasNext(); ) {
                        Package p = pack.next();
                        Body b = p.getBody();
        
                        Array<JointEdge> jointEdges = b.getJointList();
                        for (Iterator<Tape> tape = m_Tapes.iterator(); tape.hasNext() && jointEdges.size > 0; ) {
                            Tape t = tape.next();
            
                            if (t.getJoint().equals(jointEdges.get(0).joint)) {
                                tape.remove();
                                m_World.destroyJoint(jointEdges.get(0).joint);
                            }
                        }
        
                        m_Score += b.getMass() * 5;
                        m_World.destroyBody(b);
                        m_Packages.remove(p);
                        pack.remove();
                    }
                    m_TapeAmount = 5;
                    m_TapeProgress.setProgress(m_TapeAmount / 5f);
                }
                break;
            case 2:
                if (m_PaletteBody.getPosition().y > -2.5f) {
                    m_PaletteBody.setLinearVelocity(0, 0);
                    m_PaletteBody.setTransform(-2, -2.5f, 0);
                    m_PaletteTimer = 60;
                    m_PaletteState = 0;
                    m_Difficulty *= 0.8f;
                }
                break;
        }
    }
    
    @Override
    public void show() {
        
    }
    
    @Override
    public void render(float delta) {
        SoundManager.p_BackgroundMusic.setVolume(MathUtils.lerp(SoundManager.p_BackgroundMusic.getVolume(), 1.0f, 0.01f));
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        m_PackageTimer -= delta;
        m_PaletteTimer -= delta;
        
        if (m_PackageTimer < 0) {
            m_PackageTimer = MathUtils.random(5, 10) * m_Difficulty;
            spawnPackage();
        }
        
        if (m_PaletteTimer < 0) {
            movePalette();
        }
        
        /* Drawing */
        SpriteBatch batch = m_Game.p_Batch;
        
        m_Camera.update();
        batch.setProjectionMatrix(m_Camera.combined);
    
        batch.begin();
        
        m_BackgroundSprite.draw(batch);
        m_PaletteSprite.setCenter(m_PaletteBody.getPosition().x, m_PaletteBody.getPosition().y);
        m_PaletteSprite.draw(batch);
    
        for (Package p : m_Packages) {
            p.draw(batch);
        }
        for (Tape t : m_Tapes) {
            t.draw(batch);
        }
        
        m_TapeProgress.draw(batch);
        batch.setProjectionMatrix(m_GuiCam.combined);
        
        double s = (double) Math.round(((double) m_Score * 100)) / 100;
        
        m_Font.draw(batch, s + "$", 50, 50);
        batch.end();
        
        /* Update Bodies */
        for (Iterator<Package> pack = m_Packages.iterator(); pack.hasNext(); ) {
            Package p = pack.next();
            Body b = p.getBody();
        
            if (b.getPosition().x > 3.5) {
                b.setLinearVelocity(-0.5f, b.getLinearVelocity().y);
            }
            if (b.getPosition().y < -3) {
                Array<JointEdge> jointEdges = b.getJointList();
                for (Iterator<Tape> tape = m_Tapes.iterator(); tape.hasNext() && jointEdges.size > 0; ) {
                    Tape t = tape.next();
                
                    if (t.getJoint().equals(jointEdges.get(0).joint)) {
                        tape.remove();
                        m_World.destroyJoint(jointEdges.get(0).joint);
                    } else if (t.getJoint().equals(m_MouseJoint)) {
                        m_World.destroyJoint(m_MouseJoint);
                        m_MouseJoint = null;
                    }
                }
            
                m_Score -= b.getMass() * 20;
                SoundManager.p_BoxDestroy.play(1.0f);
                m_World.destroyBody(b);
                pack.remove();
            }
        }
        
        m_PlayTime += delta;
        
        if (m_Score < 0) {
            m_Game.setScreen(new GameOverScreen(m_Game, m_PlayTime / 60f));
        }
        
        m_World.step(delta, 6, 2);
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
        m_World.dispose();
        m_Font.dispose();
    }
    
    @Override
    public boolean keyDown(int keycode) {
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
        m_Camera.unproject(m_TestPoint.set(screenX, screenY, 0));
        
        m_HitBody = null;
        m_World.QueryAABB(m_Callback, m_TestPoint.x - 0.001f, m_TestPoint.y - 0.001f, m_TestPoint.x + 0.001f, m_TestPoint.y + 0.001f);
        
        if (m_HitBody == null) return false;
        if (m_HitBody == m_GroundBody) return false;
        if (m_HitBody.getType() != BodyDef.BodyType.DynamicBody) return false;
        
        switch (button) {
            case Input.Buttons.LEFT:
                MouseJointDef jointDef = new MouseJointDef();
                jointDef.bodyA = m_GroundBody;
                jointDef.bodyB = m_HitBody;
                jointDef.collideConnected = true;
                jointDef.target.set(m_TestPoint.x, m_TestPoint.y);
                jointDef.maxForce = 1000f * m_HitBody.getMass();
                m_MouseJoint = (MouseJoint) m_World.createJoint(jointDef);
                m_HitBody.setAwake(true);
                break;
            case Input.Buttons.RIGHT:
                m_HitAnchor = m_HitBody.getLocalPoint(new Vector2(m_TestPoint.x, m_TestPoint.y));
                m_RightDrag = true;
                break;
            default:
                break;
        }
        
        return false;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Input.Buttons.LEFT:
                if (m_MouseJoint != null) {
                    m_World.destroyJoint(m_MouseJoint);
                    m_MouseJoint = null;
                }
                break;
            case Input.Buttons.RIGHT:
                if (m_RightDrag) {
                    m_RightDrag = false;
                    m_Camera.unproject(m_TestPoint.set(screenX, screenY, 0));
                    
                    Body a = m_HitBody;
                    
                    m_HitBody = null;
                    m_World.QueryAABB(m_Callback, m_TestPoint.x - 0.001f, m_TestPoint.y - 0.001f, m_TestPoint.x + 0.001f, m_TestPoint.y + 0.001f);
    
                    if (m_HitBody == null) return false;
                    if (m_HitBody.getType() != BodyDef.BodyType.DynamicBody) return false;
                    if (m_HitBody.equals(a)) return false;
    
                    Vector2 anchorB = m_HitBody.getLocalPoint(new Vector2(m_TestPoint.x, m_TestPoint.y));
                    float len = Math.max(0.5f, Math.abs(a.getWorldPoint(m_HitAnchor).cpy().sub(m_HitBody.getWorldPoint(anchorB)).len()));
                    
                    if (len < m_TapeAmount) {
                        m_Tapes.add(new Tape(a, m_HitBody, m_HitAnchor, anchorB, m_World));
                        m_TapeAmount -= len;
                        m_TapeProgress.setProgress(m_TapeAmount / 5f);
                    } else {
                        m_TapeProgress.blink();
                    }
                }
                break;
            default:
                break;
        }
        
        return false;
    }
    
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (m_MouseJoint != null) {
            m_Camera.unproject(m_TestPoint.set(screenX, screenY, 0));
            m_MouseJoint.setTarget(new Vector2(m_TestPoint.x, m_TestPoint.y));
        }
        
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
    
    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
    
        float intensity = a.getLinearVelocity().sub(b.getLinearVelocity()).len() / 5f;
        intensity = MathUtils.clamp(intensity, 0f, 1f) * 2f;
    
        SoundManager.p_BoxSound.play(intensity);
    }
    
    @Override
    public void endContact(Contact contact) {
        
    }
    
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        
    }
    
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        
    }
}
