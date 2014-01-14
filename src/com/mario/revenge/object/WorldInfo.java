package com.mario.revenge.object;

public class WorldInfo {
	
	private String worldCode;
	private int amountOfTime;
	
	public WorldInfo() {}
	
	public WorldInfo(String worldCode, int amountOfTime) {
		this.worldCode = worldCode;
		this.amountOfTime = amountOfTime;
	}
	
	public String getWorldCode() {
		return worldCode;
	}
	public void setWorldCode(String worldCode) {
		this.worldCode = worldCode;
	}
	public int getAmountOfTime() {
		return amountOfTime;
	}
	public void setAmountOfTime(int amountOfTime) {
		this.amountOfTime = amountOfTime;
	}	
	
	
}
