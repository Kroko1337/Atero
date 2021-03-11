package de.verschwiegener.atero.util;

public class TimeUtils {
	
	private static long lastMS = 0L;
	private long prevMS;
	
	public TimeUtils(){
		this.prevMS = 0L;
	}
	
	public boolean isDelayComplete(long delay) {
		if(System.currentTimeMillis() - lastMS >= delay) {
			return true;
		}
		
		return false;
	}
	
	public static long getCurrentMS(){
		return System.nanoTime() / 1000000L;
	}
	
	public void setLastMS(long lastMS) {
		this.lastMS = lastMS;
	}
	
	public static void setLastMS() {
		lastMS = System.currentTimeMillis();
	}
	
	public int convertToMS(int d) {
		return 1000 /d;
	}
	
	public static boolean hasReached(float f){
		return (float) (getCurrentMS() - lastMS) >= f; 
	}

	public static void reset(){
		lastMS = getCurrentMS();
	}
	
	public boolean delay(float milliSec){
		return (float)(getTime() - this.prevMS) >= milliSec;
	}
	
	private long getTime(){
		return System.nanoTime() / 1000000L;
	}
}
