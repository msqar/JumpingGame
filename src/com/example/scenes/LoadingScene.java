package com.example.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.example.base.BaseScene;
import com.example.managers.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{
	@Override
	public void createScene()
	{	
	    setBackground(new Background(Color.BLACK));
	    attachChild(new Text(400, 240, resourcesManager.font, "Loading...", vbom));
//	    attachChild(new Text(GameActivity.getScreenWidth() / 2 - 200, GameActivity.getScreenHeight() / 2 - (resourcesManager.font.getLineHeight() / 2), resourcesManager.font, "Loading...", vbom));
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
