package com.mario.revenge.scenes;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import com.mario.revenge.base.BaseScene;
import com.mario.revenge.managers.ResourcesManager;
import com.mario.revenge.managers.SceneManager.SceneType;
import com.mario.revenge.utils.GameUtils;

public class InfoScene extends BaseScene {
	
	private Text marioTitleText;
	private Text totalScoreText;
	private Text totalCoinsText;
	private Text mapLevelText;
	private Text currentMapLevelText;
	
	private Text timeTitleText;
	private Text timeTitleValue;
	
	private Text worldText;
	private Text livesText;
	private Text currentWorldText;
	private Sprite marioSprite;
	

	@Override
	public void createScene() {
		Rectangle bg = new Rectangle(0, 0, 800, 480, ResourcesManager.getInstance().vbom);
		bg.setColor(Color.BLACK);
		
		
		marioTitleText = new Text(20, 430, resourcesManager.font, "MARIO", new TextOptions(HorizontalAlign.LEFT), vbom);
        marioTitleText.setAnchorCenter(0, 0);
        marioTitleText.setText("MARIO");
        
        totalScoreText = new Text(20, 400, resourcesManager.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        totalScoreText.setAnchorCenter(0, 0);        
        totalScoreText.setText(GameUtils.getResolvedCurrentScore());
        
        final AnimatedSprite coinHUDSprite = new AnimatedSprite(240, 410, resourcesManager.menu_hud_coin_region, vbom);
        coinHUDSprite.setScale(1.2f);
        final long[] ANIMATE_COIN_HUD = {900,900,900};
        coinHUDSprite.animate(ANIMATE_COIN_HUD, 0, 2, true);
        
        totalCoinsText = new Text(250, 400, resourcesManager.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        totalCoinsText.setAnchorCenter(0, 0);
        totalCoinsText.setText(GameUtils.getResolvedCurrentCoins());
        
        mapLevelText = new Text(380, 430, resourcesManager.font, "WORLD", new TextOptions(HorizontalAlign.LEFT), vbom);
        mapLevelText.setAnchorCenter(0, 0);
        mapLevelText.setText("WORLD");
        
        currentMapLevelText = new Text(400, 400, resourcesManager.font, "123456", new TextOptions(HorizontalAlign.LEFT), vbom);
        currentMapLevelText.setAnchorCenter(0, 0);
        currentMapLevelText.setText(GameUtils.getResolvedCurrentWorld());
        
        timeTitleText = new Text(550, 430, resourcesManager.font, "TIME", new TextOptions(HorizontalAlign.LEFT), vbom);
        timeTitleText.setAnchorCenter(0, 0);
        timeTitleText.setText("TIME");
        
        timeTitleValue = new Text(570, 400, resourcesManager.font, "123456", new TextOptions(HorizontalAlign.LEFT), vbom);
        timeTitleValue.setAnchorCenter(0, 0);
        timeTitleValue.setText(GameUtils.getResolvedCurrentTime());
		
		
		worldText = new Text(400, 300, ResourcesManager.getInstance().font, "WORLD", ResourcesManager.getInstance().vbom);
        worldText.setText("WORLD");
        
        currentWorldText = new Text(400, 250, ResourcesManager.getInstance().font, "0123456789", ResourcesManager.getInstance().vbom);
        currentWorldText.setText(GameUtils.getResolvedCurrentWorld());
        
        livesText = new Text(410, 150, ResourcesManager.getInstance().font, "x000", ResourcesManager.getInstance().vbom);
        livesText.setText(GameUtils.getResolvedCurrentLives());       
        
        marioSprite = new Sprite(360, 150, ResourcesManager.getInstance().world_info_mario_region, ResourcesManager.getInstance().vbom);
        marioSprite.setScale(1.5f);
        
        attachChild(bg);
        attachChild(marioTitleText);
        attachChild(totalScoreText);
        attachChild(totalCoinsText);
        attachChild(timeTitleText);
        attachChild(timeTitleValue);
        attachChild(mapLevelText);
        attachChild(currentMapLevelText);
        attachChild(coinHUDSprite);
        
        attachChild(worldText);
        attachChild(currentWorldText);
        attachChild(livesText);
        attachChild(marioSprite);			
	}

	@Override
	public void onBackKeyPressed() {
		return;		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_INFO;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

}
