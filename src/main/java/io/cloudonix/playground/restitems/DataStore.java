package io.cloudonix.playground.restitems;

import java.util.LinkedHashMap;

import io.cloudonix.playground.restitems.model.ItemService;

public class DataStore {
	
	private LinkedHashMap<String, PersonalData> data = new LinkedHashMap<String, PersonalData>() {
		private static final long serialVersionUID = -7252153494179319137L;

		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<String, PersonalData> eldest) {
			return eldest.getValue().old();
		}
	};

	public PersonalData getUserData(String key, ItemService service) {
		if (!data.containsKey(key))
			data.putIfAbsent(key, new PersonalData(service));
		return data.get(key);
	}
}
