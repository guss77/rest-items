package io.cloudonix.playground.restitems.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

	private int id;
	private String name;
	private String description;
	private String sku;
	private BigDecimal cost;
	private HashMap<String,Object> profile;
	
	public Item() {};
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getSku() {
		return sku;
	}
	
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public BigDecimal getCost() {
		return cost;
	}
	
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public HashMap<String,Object> getProfile() {
		return profile;
	}
	
	public void setProfile(HashMap<String,Object> profile) {
		this.profile = profile;
	}
	
	/**
	 * Remove profile entries that are null
	 * @return
	 */
	public Item purgeProfile() {
		purgeMap(profile);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	private void purgeMap(Map<String, Object> map) {
		for (var it = map.entrySet().iterator(); it.hasNext();) {
			var e = it.next();
			if (e.getValue() == null)
				it.remove();
			if (e.getValue() instanceof Map m)
				purgeMap(m);
		}
	}
}
