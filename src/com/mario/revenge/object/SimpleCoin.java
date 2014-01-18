package com.mario.revenge.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseExponentialOut;

import com.mario.revenge.activity.GameActivity;
import com.mario.revenge.managers.ResourcesManager;
import com.mario.revenge.utils.GameUtils;

public class SimpleCoin extends AnimatedSprite {	
	
	private Sprite coinScoreSprite;
	private SequenceEntityModifier jumpMod;
	private ITextureRegion coinTextureRegion;

    public SimpleCoin(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().simple_coin_region, vbo);
        coinTextureRegion = ResourcesManager.getInstance().coin_picked_unit_region;
        animateCoin();
    }
    
    private void animateCoin() {
		final long[] ANIMATE_COIN = {500,500,500};
		animate(ANIMATE_COIN, 0, 2, true);	
	}

	public void addToScore(Text scoreCoinsText, Text totalScoreText)
    {    	
		ResourcesManager.getInstance().coins++;
		ResourcesManager.getInstance().totalScore += 200;
        
        resolveCoinText(scoreCoinsText);
        resolveTotalScoreText(totalScoreText);
       
        ResourcesManager.getInstance().grab_coin_sound.setVolume(1.0f);
        ResourcesManager.getInstance().grab_coin_sound.play();
    }
    
    private void resolveTotalScoreText(Text totalScoreText) {
//		String zeros = "0000";
//		if(ResourcesManager.getInstance().totalScore == 0) {
//			zeros = "00000";
//		}else if(ResourcesManager.getInstance().totalScore > 99999) {	
//			zeros = "";
//		}else if(ResourcesManager.getInstance().totalScore > 9999) {
//			zeros = "0";
//		}else if(ResourcesManager.getInstance().totalScore > 999) {
//			zeros = "00";
//		}else if(ResourcesManager.getInstance().totalScore > 99) {	
//			zeros = "000";
//		}
//    	
//    	totalScoreText.setText(zeros + ResourcesManager.getInstance().totalScore);
    	totalScoreText.setText(GameUtils.getResolvedCurrentScore(ResourcesManager.getInstance().totalScore));
	}

	private void resolveCoinText(Text scoreCoinsText) {
		String zeros = "0";
    	if(ResourcesManager.getInstance().coins > 9) {
			zeros = "";
		}
    	scoreCoinsText.setText("x" + zeros + ResourcesManager.getInstance().coins);
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
