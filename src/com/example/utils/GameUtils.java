package com.example.utils;

import com.example.managers.ResourcesManager;

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
		String[] splitted = ResourcesManager.getInstance().currentWorld.trim().split("-");
		if(splitted != null && splitted.length != 0) {
			return splitted[0] + " - " + splitted[1];
		}
    	return ResourcesManager.getInstance().currentWorld;
	}
	
	public static String getResolvedCurrentScore() {
		String zeros = "0000";
		if(ResourcesManager.getInstance().totalScore == 0) {
			zeros = "00000";
		}
    	if(ResourcesManager.getInstance().totalScore > 9) {
			zeros = "000";
		}else if(ResourcesManager.getInstance().totalScore > 99) {
			zeros = "00";
		}else if(ResourcesManager.getInstance().totalScore > 999) {
			zeros = "0";
		}else if(ResourcesManager.getInstance().totalScore > 9999) {
			zeros = "";		
		}
		
		return zeros + ResourcesManager.getInstance().totalScore;
	}


}
