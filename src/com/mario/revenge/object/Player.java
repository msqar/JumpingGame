package com.mario.revenge.object;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mario.revenge.managers.ResourcesManager;
import com.mario.revenge.managers.SceneManager;

public abstract class Player extends AnimatedSprite
{
	
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	    
	private Body body;
	private boolean isJumping = false;
	private int footContacts = 0;
	private boolean isMoving = false;
	public enum PlayerDirection { 
		LEFT, 
		RIGHT, 
		UP, 
		DOWN, 
		NONE; 
	}
	
	public long start;
	public long end = 0;
	
	public PlayerDirection lastDirection = PlayerDirection.UP;
	

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------
    
	public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().mario_region, vbo);
        createPhysics(camera, physicsWorld);
        camera.setChaseEntity(this);
    }
    
    public abstract void onDie();
    
    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
    {        
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        body.setUserData("mario");
        body.setFixedRotation(true);
        
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
        {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);
                
//                System.out.println(getX());
                if(getX() == 3.0f) {
//                	body.set
                }
                
                if (getY() <= 0)
                {                    
                    onDie();
                }
            }
        });
    }
    
    public void setRunningRight()
    {
    	lastDirection = PlayerDirection.RIGHT;    	
		setFlippedHorizontal(false); 
    	if(isJumping) {
    		stopAnimation(5);
    		body.setLinearVelocity(new Vector2(2, body.getLinearVelocity().y)); 
    	}else {
    		System.out.println("Siempre entro aca?");
    		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };            
    		animate(PLAYER_ANIMATE, 1, 3, true);
    		body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y));    		   		
    	}
    	
    }
    
    public void setRunningLeft()
    {
    	lastDirection = PlayerDirection.LEFT;  
    	setFlippedHorizontal(true); 
    	if(isJumping) {
    		stopAnimation(5);
    		body.setLinearVelocity(new Vector2(-2, body.getLinearVelocity().y));
    	}else {
    		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };            
    		animate(PLAYER_ANIMATE, 1, 3, true);    		
    		body.setLinearVelocity(new Vector2(-5, body.getLinearVelocity().y));    		   		
    	}
    }
    
    public void jump()
    {      	
   	
        if (footContacts < 1) 
        {
            return; 
        }

    	final long[] PLAYER_ANIMATE = new long[] { 100, 100 };
    	
    	animate(PLAYER_ANIMATE, 4, 5, false);
    	float xVelocity = 0f;

    	if(body.getLinearVelocity().x > 0.0f) {
    		xVelocity = body.getLinearVelocity().x - 1;
    	}else if(body.getLinearVelocity().x < 0.0f){
    		xVelocity = body.getLinearVelocity().x + 1;
    	}else if(body.getLinearVelocity().x == 0.0f){
    		xVelocity = body.getLinearVelocity().x;
    	}
  	
//    	final Vector2 velocity = Vector2Pool.obtain(xVelocity, calculteYVelocity());
//    	body.setLinearVelocity(xVelocity, calculteYVelocity());
//    	Vector2Pool.recycle(velocity);
    	stopAnimation(5);
    	body.applyLinearImpulse(new Vector2(0, 19), body.getPosition());
    	ResourcesManager.getInstance().mario_jump_sound.play();    
        
    }
    
    private float calculteYVelocity() {
		float yVelocity = 0;
		System.out.println("Start pressed: " + start);
		long totalTime = (start - System.currentTimeMillis()) / 1000;
    	if(totalTime <= 0.5) {
    		yVelocity = 10;
		}else{
			if(totalTime > 1) {
				yVelocity = 19;
			}
		}
		return yVelocity;
	}

	public void jumpingStart() {    	
        this.isJumping = true;
    }

	public void jumpingEnd() {
        this.isJumping = false;
        if (isMoving()) {
            if (lastDirection == PlayerDirection.LEFT) {
            	if (!isAnimationRunning())
            		animate(new long[] { 100, 100, 100 }, 1, 3, true);

            	body.setLinearVelocity(new Vector2(-5, body.getLinearVelocity().y));
            	setFlippedHorizontal(true);
                
            } else if (lastDirection == PlayerDirection.RIGHT) {
            	if (!isAnimationRunning())
                    animate(new long[] { 100, 100, 100 }, 1, 3, true);

            	body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y));
            	setFlippedHorizontal(false);
            }
        } else {
            if (lastDirection == PlayerDirection.LEFT) {
            	stopAnimation(0); 
            	setFlippedHorizontal(true);
            	body.setLinearVelocity(0, 0);
            } else if (lastDirection == PlayerDirection.RIGHT) {
            	stopAnimation(0);
            	body.setLinearVelocity(0, 0);
            }
        }
	}
    
    public boolean isMoving() {
		return isMoving;
	}

	public void dieAnimation() {
    	final long[] PLAYER_ANIMATE = new long[] { 100, 100 };        
        animate(PLAYER_ANIMATE, 6, 7, false);
        body.applyLinearImpulse(new Vector2(0, 30), body.getPosition());
        
        ArrayList<Fixture> fixtureList = body.getFixtureList();
        for (int i = 0; i < fixtureList.size(); i++) {
			Fixture fix = fixtureList.get(i);
			if(fix.getBody().getUserData().equals("mario")) {
				body.destroyFixture(fix);
			}
		}
    }
    
    public void increaseFootContacts()
    {
        footContacts++;
    }

    public void decreaseFootContacts()
    {
        footContacts--;
    }

	public Body getPlayerBody() {
		return body;
	}

	public void setPlayerBody(Body body) {
		this.body = body;
	}
	
	public void stopMoving() {
		this.body.setLinearVelocity(0, 0);
		stopAnimation();
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setIsJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	public int getFootContacts() {
		return footContacts;
	}

	public void setFootContacts(int footContacts) {
		this.footContacts = footContacts;
	}	

	public PlayerDirection getLastDirection() {
		return lastDirection;
	}

	public void setLastDirection(PlayerDirection lastDirection) {
		this.lastDirection = lastDirection;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public void die() {
		ResourcesManager.getInstance().lives--;
		if(ResourcesManager.getInstance().mario_song_music.isPlaying()) {
    		ResourcesManager.getInstance().mario_song_music.stop();
    	}
    	
    	dieAnimation();                    	    	
    	ResourcesManager.getInstance().mario_game_over_sound.play();
    	
    	SceneManager.getInstance().loadGameScene(SceneManager.getInstance().getEngine());		
	}	
	
}
