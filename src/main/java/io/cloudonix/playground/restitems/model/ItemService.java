package io.cloudonix.playground.restitems.model;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import io.vertx.core.json.Json;

public class ItemService {

	private List<Item> itemlist;
	
	public ItemService() {
		itemlist = Json.decodeValue(readFile(ClassLoader.getSystemResourceAsStream("items.json")), new TypeReference<List<Item>>() {})
				.stream().map(i -> { i.setDescription(i.getName()); return i; }).collect(Collectors.toList());
	}
	
	public Item create() {
		return itemlist.get(new Random().nextInt(itemlist.size()));
	}
	
	private String readFile(InputStream inputStream) {
		try {
			StringWriter st = new StringWriter();
			try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream))) {
				bf.transferTo(st);
			}
			return st.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
