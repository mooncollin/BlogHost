package main;

import java.util.Random;


public class Ad {
	public String getAd() {
		Random rand = new Random(); 
		int randInt = rand.nextInt(9);
		String file;
		switch (randInt) {
		case 0: 
			file = "Images/AppleWatch.gif";
			break;
		case 1: 
			file = "Images/BeatsDre.gif";
			break;
		case 2: 
			file = "Images/WeatherApp.gif";
			break;
		case 3: 
			file = "Images/RecordsShop.gif";
			break;
		case 4: 
			file = "Images/GT09.gif";
			break;
		case 5: 
			file = "Images/LearnCode.gif";
			break;
		case 6: 
			file = "Images/RecordsShop.gif";
			break;
		case 7: 
			file = "Images/LuxuryWatches.gif";
			break;
		case 8: 
			file = "Images/ExploreBrazil.gif";
			break;
		default:
			file = "";
			break;
		}
		return file;
	}
	
}
