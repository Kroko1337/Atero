package de.verschwiegener.atero.friend;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FriendManager {
    
    private Map<String, String> friends = new LinkedHashMap<>();
    
    public void addFriend(String name, String synonym) {
	friends.putIfAbsent(name, synonym);
    }
    public boolean isFriend(String username) {
	return friends.containsKey(username);
    }

    public String getSynonym(String username) {
	if (isFriend(username)) {
	    return friends.get(username);
	}
	return username;
    }

}
