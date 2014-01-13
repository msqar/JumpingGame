package com.mario.revenge.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.mario.revenge.base.BaseScene;
import com.mario.revenge.managers.ResourcesManager;
import com.mario.revenge.managers.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{
	@Override
	public void createScene()
	{	
	    setBackground(new Background(Color.BLACK));
	    attachChild(new Text(400, 240, ResourcesManager.getInstance().loading_font, "loading...", vbom)); //240
	}

    @Override
    public void onBackKeyPressed()
    {
        return;
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene()
    {
    	
    }
}
