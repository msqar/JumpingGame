package com.example.scenes;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.base.BaseScene;
import com.example.extras.LevelCompleteWindow;
import com.example.extras.LevelCompleteWindow.StarsCount;
import com.example.managers.ResourcesManager;
import com.example.managers.SceneManager;
import com.example.managers.SceneManager.SceneType;
import com.example.object.Player;

public class GameScene extends BaseScene implements IOnSceneTouchListener
{
	
	private HUD gameHUD;
	private Text scoreText;
	private int score = 0;
	private PhysicsWorld physicsWorld;
	
	// Variables for game
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	    
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelComplete";
    
	private Player player;
	
	private boolean firstTouch = false;
	
	// Handle GAME OVER
	private Text gameOverText;
	private boolean gameOverDisplayed = false;
	
	// Handle Complete Game
	private LevelCompleteWindow levelCompleteWindow;
	
	@Override
	public void createScene()
	{
	    createBackground();
	    createHUD();
	    createControls();
	    createPhysics();
	    loadLevel(1);
	    createGameOverText();
	    setOnSceneTouchListener(this);
	    levelCompleteWindow = new LevelCompleteWindow(vbom);
	}

	@Override
	public void onBackKeyPressed()
	{
	    SceneManager.getInstance().loadMenuScene(engine);
	}

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
        camera.setHUD(null);
        camera.setCenter(400, 240);
//        camera.setCenter(GameActivity.getScreenWidth() / 2, GameActivity.getScreenHeight() / 2);
        // TODO code responsible for disposing scene
        // removing all game scene objects.
        camera.setChaseEntity(null);
    }
    
    private void createBackground()
    {
        setBackground(new Background(240.0f, 240.0f, 240.0f));
    }
    
    private void createHUD()
    {
        gameHUD = new HUD();
        
        // CREATE SCORE TEXT
        scoreText = new Text(20, 400, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0, 0);
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);
        
        camera.setHUD(gameHUD);        
    }
    
    private void addToScore(int i)
    {
        score += i;
        scoreText.setText("Score: " + score);
    }
    
    private void createPhysics()
    {
    	physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false); 
        physicsWorld.setContactListener(contactListener());
        registerUpdateHandler(physicsWorld);
    }
    
    private void loadLevel(int levelID)
    {
        final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
        
        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
        
        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException 
            {
                final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
                
                camera.setBounds(0, 0, width, height); // here we set camera bounds
                camera.setBoundsEnabled(true);

                return GameScene.this;
            }
        });
        
        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
                
                final Sprite levelObject;
                
                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform1_region, vbom);
                    PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
                } 
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform2");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform3");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
                {
                	
//                	playerBody = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.DynamicBody, FIXTURE_DEF);
                	levelObject = new Sprite(x, y, resourcesManager.coin_region, vbom)
                	{
                	    @Override
                	    protected void onManagedUpdate(float pSecondsElapsed) 
                	    {
                	        super.onManagedUpdate(pSecondsElapsed);

                	        if (player.collidesWith(this))
                	        {
                	            addToScore(10);
                	            this.setVisible(false);
                	            this.setIgnoreUpdate(true);
                	        }
                	    }
                	};
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
                {
                    player = new Player(x, y, vbom, camera, physicsWorld)
                    {
                    	@Override
                    	public void onDie()
                    	{
                    	    if (!gameOverDisplayed)
                    	    {
                    	        displayGameOverText();
                    	    }
                    	}

                    };
                    levelObject = player;
                } 
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
                {
                    levelObject = new Sprite(x, y, resourcesManager.complete_stars_region, vbom)
                    {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) 
                        {
                            super.onManagedUpdate(pSecondsElapsed);

                            if (player.collidesWith(this))
                            {
                                levelCompleteWindow.display(StarsCount.TWO, GameScene.this, camera);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else
                {
                    throw new IllegalArgumentException();
                }

                levelObject.setCullingEnabled(true);

                return levelObject;
            }
        });

        levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
    }
    
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
    {
    	if (pSceneTouchEvent.isActionDown())
        {
            if (!firstTouch)
            {
                player.setRunning();
                firstTouch = true;
            }
            else
            {
                player.jump();
            }
        }
        return false;
    }
    
    private void createGameOverText()
    {
        gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
    }

    private void displayGameOverText()
    {
        camera.setChaseEntity(null);
        gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
        attachChild(gameOverText);
        gameOverDisplayed = true;
    }
    
    private ContactListener contactListener()
    {
        ContactListener contactListener = new ContactListener()
        {
            public void beginContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
                {
                    if (x2.getBody().getUserData().equals("player"))
                    {
                        player.increaseFootContacts();
                    }
                }
                if (x1.getBody().getUserData().equals("platform3") && x2.getBody().getUserData().equals("player"))
                {
                    x1.getBody().setType(BodyType.DynamicBody);
                }
                if (x1.getBody().getUserData().equals("platform2") && x2.getBody().getUserData().equals("player"))
                {
                    engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback()
                    {                                    
                        public void onTimePassed(final TimerHandler pTimerHandler)
                        {
                            pTimerHandler.reset();
                            engine.unregisterUpdateHandler(pTimerHandler);
                            x1.getBody().setType(BodyType.DynamicBody);
                        }
                    }));
                }
            }

            public void endContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
                {
                    if (x2.getBody().getUserData().equals("player"))
                    {
                        player.decreaseFootContacts();
                    }
                }
            }

            public void preSolve(Contact contact, Manifold oldManifold)
            {

            }

            public void postSolve(Contact contact, ContactImpulse impulse)
            {

            }
        };
        return contactListener;
    } 
    
    private void createControls() {
    	//final DigitalOnScreenControl control = new DigitalOnScreenControl(20, camera.getHeight() - ResourcesManager.getInstance().control_base_region.getHeight(), 
    	final DigitalOnScreenControl control = new DigitalOnScreenControl(300, 300,
    			camera, ResourcesManager.getInstance().control_base_region, ResourcesManager.getInstance().control_knob_region, 
    			0.1f, vbom, new IOnScreenControlListener() {					
					public void onControlChange(BaseOnScreenControl pBaseOnScreenControl, float x, float y) {
						if(player.getPlayerBody().getLinearVelocity().x > -8 && player.getPlayerBody().getLinearVelocity().x < 8) {
							if(x > 0) { // right
								player.getPlayerBody().setLinearVelocity(8.0f, player.getPlayerBody().getLinearVelocity().y);
							}else if (x < 0) { // left
								player.getPlayerBody().setLinearVelocity(-8.0f, player.getPlayerBody().getLinearVelocity().y);
							}else { // stop moving
								player.getPlayerBody().setLinearVelocity(0.0f, player.getPlayerBody().getLinearVelocity().y);
							}
						}
						if(y > 0) {
							player.jump();
						}
					}
				});
    	
    	control.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    	control.getControlBase().setAlpha(0.5f);
    	control.getControlBase().setScaleCenter(0, 128);
    	control.getControlBase().setScale(1.25f);
    	control.getControlKnob().setScale(1.25f);
    	control.setAnchorCenter(0, 0);
    	control.refreshControlKnobPosition();
    	setChildScene(control);
    }

//	@Override
//	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
//		// TODO Auto-generated method stub
//		return false;
//	}
    
}

