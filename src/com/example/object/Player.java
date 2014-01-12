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
	private int lastDirection = 1;
	private static int lives;
	

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------
    
    public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().mario_region, vbo);
        setLives(3);
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
    	lastDirection = 1;
    	body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y));    	
        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };            
        animate(PLAYER_ANIMATE, 1, 3, true);
    }
    
    public void setRunningLeft()
    {
    	lastDirection = 0;
    	body.setLinearVelocity(new Vector2(-5, body.getLinearVelocity().y));    	
        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };            
        animate(PLAYER_ANIMATE, 1, 3, true);
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

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		Player.lives = lives;
	}

	public void removeALife() {
		Player.lives -= 1;		
	}

	public int getLastDirection() {
		return lastDirection;
	}

	public void setLastDirection(int lastDirection) {
		this.lastDirection = lastDirection;
	}

}
