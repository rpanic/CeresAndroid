package org.rpanic1308.transmission;

import java.util.HashMap;
import java.util.Map;

public abstract class Action {

	public Map<String, Double> keywords = new HashMap<>();
	public String description;
	Map<String, Object> data = new HashMap<>(); 
	
	public abstract void act();
	
	public abstract void extractData(String sentence);
	
}
