package com.covid.spchacker.cache.copy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.covid.spchacker.entity.User;

public enum ApplicationCache {

	INSTANCE;
	
	public Map<String, User> userAuth = new HashMap<String, User>();
	
	/*public static Map<String, String> _state_code_map = new HashMap<String, String>();
	
	@Value("spring.state.codes.names")
	static String _state_codes;
	
	static {
		String[] ip = _state_codes.split("&&");
		for(String s : ip) {
			String[]pair = s.split("|");
			_state_code_map.put(pair[0], pair[1]);
		}
	} */
	
	
	
}
