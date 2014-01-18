package com.mario.revenge.extras;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.util.Log;

import com.mario.revenge.managers.ResourcesManager;
import com.mario.revenge.managers.SceneManager;

public class LevelCompleteWindow extends Sprite
{
    private TiledSprite star1;
    private TiledSprite star2;
    private TiledSprite star3;
    
    public enum StarsCount
    {
        ONE,
        TWO,
        THREE
    }
    
    public LevelCompleteWindow(VertexBufferObjectManager pSpriteVertexBufferObject)
    {
        super(0, 0, 650, 400, ResourcesManager.getInstance().complete_window_region, pSpriteVertexBufferObject);
        attachStars(pSpriteVertexBufferObject);
    }
    
    private void attachStars(VertexBufferObjectManager pSpriteVertexBufferObject)
    {
        star1 = new TiledSprite(150, 150, ResourcesManager.getInstance().complete_stars_region, pSpriteVertexBufferObject);
        star2 = new TiledSprite(325, 150, ResourcesManager.getInstance().complete_stars_region, pSpriteVertexBufferObject);
        star3 = new TiledSprite(500, 150, ResourcesManager.getInstance().complete_stars_region, pSpriteVertexBufferObject);
        
        attachChild(star1);
        attachChild(star2);
        attachChild(star3);
    }
    
    /**
     * Change star's tile index, depends on stars count.
     * @param starsCount
     */
    public void display(StarsCount starsCount, final Scene scene, Camera camera)
    {
        // Change stars tile index, based on stars count (1-3)
        switch (starsCount)
        {
            case ONE:
                star1.setCurrentTileIndex(0);
                star2.setCurrentTileIndex(1);
                star3.setCurrentTileIndex(1);
                break;
            case TWO:
                star1.setCurrentTileIndex(0);
                star2.setCurrentTileIndex(0);
                star3.setCurrentTileIndex(1);
                break;
            case THREE:
                star1.setCurrentTileIndex(0);
                star2.setCurrentTileIndex(0);
                star3.setCurrentTileIndex(0);
                break;
        }
        
        
        // Hide HUD
        camera.getHUD().setVisible(false);
        
        // Disable camera chase entity
        camera.setChaseEntity(null);
        
        // Attach our level complete panel in the middle of camera
        setPosition(camera.getCenterX(), camera.getCenterY());
        scene.attachChild(this);
        
        //TODO: Timer to test next level transitions - Doesn't need to be here.  Put it wherever.
        scene.registerUpdateHandler(new TimerHandler(6f, new ITimerCallback(){

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				LevelCompleteWindow.this.setColor(Color.RED);
				scene.registerTouchArea(LevelCompleteWindow.this);
			}
        	
        }));
    }
    
    
  //TODO: For Testing Purposes.. 
    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
                    final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
    	if(pSceneTouchEvent.isActionUp())
    		Log.d("Info", "Moving To Next Level.");
    	SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine);
    	return true;
    }
}

