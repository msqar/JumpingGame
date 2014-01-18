package com.mario.revenge.utils;

import com.mario.revenge.managers.ResourcesManager;
import com.mario.revenge.object.WorldInfo;

public class GameUtils {
	
	public static String getResolvedCurrentCoins() {
		String zeros = "0";
    	if(ResourcesManager.getInstance().coins > 9) {
			zeros = "";
		}
    	return "x" + zeros + ResourcesManager.getInstance().coins;
	}
	
	public static String getResolvedCurrentLives() {
    	return " x " + ResourcesManager.getInstance().lives;
	}
	
	public static String getResolvedCurrentWorld() {
		String[] splitted = ResourcesManager.getInstance().currentWorldName.trim().split("-");
		if(splitted != null && splitted.length != 0) {
			return splitted[0] + "-" + splitted[1];
		}
    	return ResourcesManager.getInstance().currentWorldName;
	}
	
	public static String getResolvedCurrentScore() {
		return getResolvedCurrentScore(ResourcesManager.getInstance().totalScore);
	}
	
	public static String getResolvedCurrentScore(int pScore){
		return padLeft(Integer.toString(ResourcesManager.getInstance().totalScore), 6).replace(" ", "0");
	}

	public static int resolveTimeBasedOnCurrentLevel() {
		int currentWorldID = ResourcesManager.getInstance().currentWorldID;
		WorldInfo info = ResourcesManager.worldLevels.get(currentWorldID);
		return info.getAmountOfTime();
	}

	public static String getResolvedCurrentTime(){
		return padLeft(Integer.toString(ResourcesManager.getInstance().levelTime), 3);
	}
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}

}
