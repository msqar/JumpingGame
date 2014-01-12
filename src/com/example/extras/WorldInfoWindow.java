package com.example.extras;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.managers.ResourcesManager;

public class WorldInfoWindow extends Sprite {
	
	private Text worldText, currentWorldText, livesText;	
	private Sprite marioSprite;

	public WorldInfoWindow(VertexBufferObjectManager vbom, final Scene scene) {
		super(0, 0, 800, 480, ResourcesManager.getInstance().world_info_window_region, vbom);	
		worldText = new Text(400, 240, ResourcesManager.getInstance().font, "WORLD", ResourcesManager.getInstance().vbom);
		worldText.setText("WORLD");
		
		livesText = new Text(400, 150, ResourcesManager.getInstance().font, "0123", ResourcesManager.getInstance().vbom);
		
		currentWorldText = new Text(400, 240, ResourcesManager.getInstance().font, "0123456789", ResourcesManager.getInstance().vbom);
		marioSprite = new Sprite(360, 150, ResourcesManager.getInstance().world_info_mario_region, ResourcesManager.getInstance().vbom);
		
		attachChild(worldText);
		attachChild(currentWorldText);
		attachChild(livesText);
		attachChild(marioSprite);
		scene.attachChild(this);
		setVisible(false);		
	}
	
	public void display(String currentWorld, int cantLives, Camera camera, final Engine engine) {		
		
		String[] worldAux = currentWorld.split("-");	
		
		currentWorldText.setText(worldAux[0] + " - " + worldAux[1]);		
		
		livesText.setText("x " + cantLives);

		// Hide HUD
        camera.getHUD().setVisible(false);
        
        // Disable camera chase entity
        camera.setChaseEntity(null);
        
        setVisible(true);
        
	}
	

}
