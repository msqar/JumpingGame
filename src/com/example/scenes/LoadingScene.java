package com.example.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.example.base.BaseScene;
import com.example.managers.ResourcesManager;
import com.example.managers.SceneManager.SceneType;

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
