package com.example.object;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.managers.ResourcesManager;
import com.example.scenes.GameScene;
import com.example.testingand.GameActivity;

public class SimpleCoin extends Sprite {	
	
	private PhysicsHandler mPhysicsHandler;
	private Sprite sprite;
	private float originX, originY;

    public SimpleCoin(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().simple_coin_region, vbo);
        ITextureRegion region = ResourcesManager.getInstance().coin_picked_unit_region;
    	Sprite sprite = new Sprite(getX(), getY(), region, GameActivity.getResourcesManager().vbom);
        mPhysicsHandler = new PhysicsHandler(sprite);
        registerUpdateHandler(mPhysicsHandler);
        
        originX = pX;
        originY = pY;
    }
    
    public void addToScore(Text scoreCoinsText, Text totalScoreText)
    {    	
    	GameScene.coins += 1;
        GameScene.totalScore += 200;
        System.out.println("Current coins: " + GameScene.coins + " and current totalScore: " + GameScene.totalScore);
        
        resolveCoinText(scoreCoinsText);
        resolveTotalScoreText(totalScoreText);
       
        ResourcesManager.getInstance().grab_coin_sound.setVolume(1.0f);
        ResourcesManager.getInstance().grab_coin_sound.play();
    }
    
    private void resolveTotalScoreText(Text totalScoreText) {
		String zeros = "0000";
    	if(GameScene.totalScore > 9) {
			zeros = "000";
		}else if(GameScene.totalScore > 99) {
			zeros = "00";
		}else if(GameScene.totalScore > 999) {
			zeros = "0";
		}else if(GameScene.totalScore > 9999) {
			zeros = "";		
		}
    	
    	totalScoreText.setText(zeros + GameScene.totalScore);
	}

	private void resolveCoinText(Text scoreCoinsText) {
		String zeros = "0";
    	if(GameScene.coins > 9) {
			zeros = "";
		}
    	scoreCoinsText.setText("x" + zeros + GameScene.coins);
	}

	public void pickedFromGroundShowPointsAnimation() {
    	mPhysicsHandler.setVelocityY((originY - getY()) * 5);
    	mPhysicsHandler.setAccelerationY(50);    	
    }

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public float getOriginX() {
		return originX;
	}

	public void setOriginX(float originX) {
		this.originX = originX;
	}

	public float getOriginY() {
		return originY;
	}

	public void setOriginY(float originY) {
		this.originY = originY;
	}   

}
