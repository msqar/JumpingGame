package com.example.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseExponentialOut;

import com.example.managers.ResourcesManager;
import com.example.scenes.GameScene;
import com.example.testingand.GameActivity;

public class SimpleCoin extends Sprite {	
	
	private Sprite coinScoreSprite;
	private SequenceEntityModifier jumpMod;
	private ITextureRegion coinTextureRegion;

    public SimpleCoin(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().simple_coin_region, vbo);
        coinTextureRegion = ResourcesManager.getInstance().coin_picked_unit_region;   
    }
    
    public void addToScore(Text scoreCoinsText, Text totalScoreText)
    {    	
    	GameScene.coins += 1;
        GameScene.totalScore += 200;
        
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

	public void pickedFromGroundShowPointsAnimation(float x, float y) {
		createJumpModifier(x, y);	
    }
	
	private void createJumpModifier(float x, float y) {	
			coinScoreSprite = new Sprite(x, y, coinTextureRegion, GameActivity.getResourcesManager().vbom);
		    final int jumpDuration = 1;
	        final int jumpHeight = 50;             
	        final float startY = y;
	        final float peakY = y + jumpHeight;
	        jumpMod = new SequenceEntityModifier(
	                new MoveYModifier(jumpDuration, startY, peakY, EaseExponentialOut.getInstance()),
	                new FadeOutModifier(0.5f));
	        
	        coinScoreSprite.registerEntityModifier(jumpMod);
	        getParent().attachChild(coinScoreSprite);	        
	}
}
