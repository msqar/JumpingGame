package com.example.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.managers.ResourcesManager;

public abstract class Player extends AnimatedSprite
{
	
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	    
	private Body body;
	private boolean isJumping = false;
	private boolean isMoving = false;
	private int footContacts = 0;
	
	private String lastDirection = null;
	
	private enum PlayerDirection {
		LEFT,
		RIGHT
	};
	
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
    	isMoving = true;
    	lastDirection = "RIGHT";
    	body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y));    	
        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };            
        animate(PLAYER_ANIMATE, 1, 3, true);
    }
    
    public void setRunningLeft()
    {
    	isMoving = true;
    	lastDirection = "LEFT";
    	body.setLinearVelocity(new Vector2(-5, body.getLinearVelocity().y));    	
        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };            
        animate(PLAYER_ANIMATE, 1, 3, true);
    }
    
    public void jump()
    {
//    	isJumping = true;
        if (footContacts < 1) 
        {
            return; 
        }               
        
        final long[] PLAYER_ANIMATE = new long[] { 100, 100 };
        
        animate(PLAYER_ANIMATE, 4, 5, false);
        
//        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 8)); 
        body.applyLinearImpulse(new Vector2(0, 8), body.getPosition());
        ResourcesManager.getInstance().mario_jump_sound.play();
    }
    
    public void jumpingEnd() {
        this.isJumping = false;
        if (isMoving) {
            if (PlayerDirection.LEFT.toString().equalsIgnoreCase(lastDirection)) {
                animate(new long[] { 200, 200, 200 }, 7, 9, true);
                body.setLinearVelocity(new Vector2(-5, 0));
            } else if (PlayerDirection.RIGHT.toString().equalsIgnoreCase(lastDirection)) {
                animate(new long[] { 200, 200, 200 }, 1, 3, true);
                body.setLinearVelocity(new Vector2(5, 0));
            }
        } else {
            if (PlayerDirection.LEFT.toString().equalsIgnoreCase(lastDirection)) {
            	stopAnimation(5);
            } else if (PlayerDirection.RIGHT.toString().equalsIgnoreCase(lastDirection)) {
                stopAnimation(5);
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

}
