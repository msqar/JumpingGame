package com.mario.revenge.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.mario.revenge.managers.ResourcesManager;

public class BoxCoin extends AnimatedSprite {	


    public BoxCoin(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().box_coin_region, vbo);
        animateCoin();
    }
    
    public void addToScore(int totalScore, int coins, int i, Text scoreCoinsText, Text totalScoreText)
    {
    	coins += i;
        totalScore += 200;
        scoreCoinsText.setText(String.valueOf(coins));
        totalScoreText.setText(String.valueOf(totalScore));
        ResourcesManager.getInstance().grab_coin_sound.setVolume(1.0f);
        ResourcesManager.getInstance().grab_coin_sound.play();
    }
    
    private void animateCoin() {    	 	
        final long[] COIN_ANIMATE = new long[] { 100, 100, 100, 100 };            
        animate(COIN_ANIMATE, 0, 3, false);
    }
    
    public void afterBlockPunchedAnimation(VertexBufferObjectManager vbom) {  	
    	    	
    }

}
