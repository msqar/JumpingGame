package com.example.object;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
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
import com.example.managers.ResourcesManager;

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
    	body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y));    	
        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };            
        animate(PLAYER_ANIMATE, 1, 3, true);
        setFlippedHorizontal(false);
    }
    
    public void setRunningLeft()
    {
    	lastDirection = PlayerDirection.LEFT;
    	body.setLinearVelocity(new Vector2(-5, body.getLinearVelocity().y));    	
        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };            
        animate(PLAYER_ANIMATE, 1, 3, true);
        setFlippedHorizontal(true);
    }
    
    public void jump()
    {
        if (footContacts < 1) 
        {
            return; 
        }        

    	final long[] PLAYER_ANIMATE = new long[] { 100, 100 };
    	
    	animate(PLAYER_ANIMATE, 4, 5, false);
    	
    	final Vector2 velocity = Vector2Pool.obtain(body.getLinearVelocity().x, 19);
    	
    	body.setLinearVelocity(velocity);
    	Vector2Pool.recycle(velocity);
//      body.applyLinearImpulse(new Vector2(0, 10), body.getPosition());
    	ResourcesManager.getInstance().mario_jump_sound.play();    
//    	setCurrentTileIndex(0);
        
    }
    
    public void jumpingStart() {
        this.isJumping = true;
    }

	public void jumpingEnd() {
        this.isJumping = false;
        if (isMoving()) {
        	System.out.println("Im moving while jumping!");
//            if (lastDirection == PlayerDirection.LEFT) {
//                setRunningLeft();
//            } else if (lastDirection == PlayerDirection.RIGHT) {
//                setRunningRight();
//            }
        } else {
        	System.out.println("Im jumping on my place!");
            if (lastDirection == PlayerDirection.LEFT) {
            	stopAnimation(0);            	
            } else if (lastDirection == PlayerDirection.RIGHT) {
            	stopAnimation(0);
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
	
}
