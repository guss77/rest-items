package io.cloudonix.playground.restitems.model;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.jackson.DatabindCodec;

public class ItemService {

	private List<Item> itemlist;
	
	public ItemService() {
		try {
			var is = ClassLoader.getSystemResourceAsStream("items.json");
			var json = Buffer.buffer(is.readAllBytes()).toString("UTF-8");
			itemlist = DatabindCodec.mapper().readValue(json, new TypeReference<List<Item>>() {})
					.stream().map(i -> { i.setDescription(i.getName()); return i; }).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Item create() {
		return itemlist.get(new Random().nextInt(itemlist.size()));
	}

}
