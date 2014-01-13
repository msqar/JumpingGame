package com.example.managers;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import com.example.activity.GameActivity;


public class ResourcesManager
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public GameActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;
    public IRectangleVertexBufferObject vbo;
    
    public ITextureRegion splash_region;
    private BuildableBitmapTextureAtlas splashTextureAtlas;    

        
    private BuildableBitmapTextureAtlas menuTextureAtlas;
    
    // Responsible for Loading font
    public Font font;
    public Font menu_font;
    public Font loading_font;
    
    // Pre-Load Textures
    private BuildableBitmapTextureAtlas preLevelTextureAtlas;
    
    // Game Texture
    public BuildableBitmapTextureAtlas gameTextureAtlas;
    
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    
    // Game Texture Regions
    public ITextureRegion brick_floor_region;
//    public ITextureRegion platform3_region;    
    
    public ITiledTextureRegion box_coin_region;
    public ITiledTextureRegion simple_coin_region;
    public ITextureRegion coin_picked_unit_region;
    
    // Player
    public ITiledTextureRegion mario_region;
    public int lives = 3;
    public String currentWorld = "1-1";
	public int coins = 0;
	public int totalScore = 0;
    
    // Level Complete Window
    public ITextureRegion complete_window_region;
    public ITiledTextureRegion complete_stars_region;
    
    // Controls
//	public ITextureRegion control_knob_region;
//	public ITextureRegion control_base_region;
	public ITextureRegion control_left_arrow_region;
	public ITextureRegion control_right_arrow_region;
	public ITextureRegion control_a_button_region;
	
	// Background Game
	public ITextureRegion parallax_game_background_region;
	public ITextureRegion parallax_game_background_clouds_region;
	
	// Background Extra Decoration
	public ITextureRegion mountain_one_region;
	public ITextureRegion triple_bush_region;
	
	
	// Background Menu
    public ITextureRegion menu_background_region;
    public ITextureRegion menu_background_clouds_region;
    public ITextureRegion menu_background_logo_region;
    public ITextureRegion menu_background_revenge_region;
    
    // Game HUD
    public ITiledTextureRegion menu_hud_coin_region;
    public ITextureRegion gamehud_music_on_region;
    public ITextureRegion gamehud_music_off_region;
    
    // Background Splash
    public ITextureRegion splash_background_andengine_region;
    
    // World Info 
    public ITextureRegion world_info_window_region;
    public ITextureRegion world_info_mario_region;
    
	// Menu Buttons    
//    public ITextureRegion play_region;
//    public ITextureRegion options_region;
    
    
    //---------------------------------------------
    // SOUND & MUSIC
    //---------------------------------------------
    
    // Game
    public Sound grab_coin_sound;
    public Sound level_completed_sound;
    public Sound mario_game_over_sound;
    public Sound mario_jump_sound;
    public Music mario_song_music;
    
    // Menu
    public Sound its_me_mario_sound;
    public Sound scream_sound;
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
        loadLoadingFonts();
    }
    
    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }
        
    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }
    
    public void loadPreLevelTextures()
    {
        preLevelTextureAtlas.load();
    }
    
    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }

	private void loadMenuGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
    	
        // Background
    	menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
    	menu_background_clouds_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background_clouds.png");
    	
    	menu_background_logo_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "mario_bros_logo.png");
    	menu_background_revenge_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_revenge_subtitle.png");
    	
//    	play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
//    	options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");
    	       
    	try 
    	{
    	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.menuTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    		Debug.e(e);
    	}
    }    
    
    private void loadMenuAudio()
    {
    	SoundFactory.setAssetBasePath("mfx/");
    	try {
    		its_me_mario_sound = SoundFactory.createSoundFromAsset(SceneManager.getInstance().getEngine().getSoundManager(), activity, "menu/its_me_mario.mp3");
    		scream_sound = SoundFactory.createSoundFromAsset(SceneManager.getInstance().getEngine().getSoundManager(), activity, "menu/menu_scream.mp3");
		    
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}        
    }

    private void loadGameGraphics()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
        
        // Tiles
        brick_floor_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "map/brick.png");
        
        // Extras
        box_coin_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "flipping_coin.png", 4, 1);
        simple_coin_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "simple_coin.png", 3, 1);
        coin_picked_unit_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_picked_unit.png");
        
        mario_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "mario.png", 7, 2);
        
        // Game States
        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "states/level_complete_window.png");
        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);
        
        world_info_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "states/world_info_background.png");
        world_info_mario_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "states/pre_world_mario_icon.png");

        // Controllers        
//        control_knob_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud/onscreen_control_knob.png");
//        control_base_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud/onscreen_control_base.png");        
        control_left_arrow_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud/left_arrow.png");
        control_right_arrow_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud/right_arrow.png");
        control_a_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud/a_button.png");    	
    	menu_hud_coin_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "hud/hud_coin.png", 3, 1);
    	
    	gamehud_music_on_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud/sound_on_icon.png"); 
    	gamehud_music_off_region =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud/sound_off_icon.png"); 
        
        // Background
        parallax_game_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "map/game_background.png");
        parallax_game_background_clouds_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "map/game_background_clouds.png");
        
        // Background Extras
        mountain_one_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "map/mountain.png");
        triple_bush_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "map/triple_bush.png");
        
        try 
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }
    
    public void unloadGameTextures()
    {
    	gameTextureAtlas.unload();
    	brick_floor_region = null;
    	box_coin_region = null;
    	simple_coin_region = null;
    	coin_picked_unit_region = null;
    	mario_region = null;
    	complete_window_region = null;
    	complete_stars_region = null;
    	control_left_arrow_region = null;
    	control_right_arrow_region = null;
    	control_a_button_region = null;
    	parallax_game_background_region = null;
    	parallax_game_background_clouds_region = null;
    	mountain_one_region = null;
    	triple_bush_region = null;
    	menu_hud_coin_region = null;
    	gamehud_music_on_region = null;
    	gamehud_music_off_region = null;
    }
    
    private void loadGameFonts()
    {
    	FontFactory.setAssetBasePath("fonts/");
    	final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "mariobros-font.ttf", 20, true, Color.WHITE_ABGR_PACKED_INT, 1.0f, Color.WHITE_ABGR_PACKED_INT);
        font.load();
    }
    
    private void loadGameAudio()
    {
    	SoundFactory.setAssetBasePath("mfx/");
    	MusicFactory.setAssetBasePath("mfx/");
    	try {
    		mario_song_music = MusicFactory.createMusicFromAsset(SceneManager.getInstance().getEngine().getMusicManager(), activity, "game/mario_song.ogg");
    		grab_coin_sound = SoundFactory.createSoundFromAsset(SceneManager.getInstance().getEngine().getSoundManager(), activity, "game/grab_coin.mp3");
			level_completed_sound = SoundFactory.createSoundFromAsset(SceneManager.getInstance().getEngine().getSoundManager(), activity, "game/level_completed.mp3");
		    mario_game_over_sound = SoundFactory.createSoundFromAsset(SceneManager.getInstance().getEngine().getSoundManager(), activity, "game/mario_game_over.mp3");
		    mario_jump_sound = SoundFactory.createSoundFromAsset(SceneManager.getInstance().getEngine().getSoundManager(), activity, "game/mario_jump.mp3");		
		    
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void loadSplashScreen()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	splash_background_andengine_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "andengine_logo.png");
    	
    	try 
        {
            this.splashTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.splashTextureAtlas.load();
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }
    
    public void unloadSplashScreen()
    {
    	splashTextureAtlas.unload();
    	splash_region = null;
    }
    
    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    /**
     * Responsible for loading the Menu Loading Font
     * 
     */
    private void loadMenuFonts()
    {
    	FontFactory.setAssetBasePath("fonts/");
    	final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
    	       
        menu_font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "mario-mini.ttf", 12, true, Color.WHITE_ABGR_PACKED_INT, 0.0f, Color.BLACK_ABGR_PACKED_INT);
        menu_font.load();
    }
    
    private void loadLoadingFonts() 
    {
    	FontFactory.setAssetBasePath("fonts/");
    	final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
    	
    	loading_font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "mariobros-font.ttf", 20, true, Color.WHITE_ABGR_PACKED_INT, 1.0f, Color.WHITE_ABGR_PACKED_INT);
        loading_font.load();   	
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }

	public void resetGameVariables() {
		lives = 3;
		currentWorld = "1-1";
		coins = 0;
		totalScore = 0;
	}
}
