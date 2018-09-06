package io.cloudonix.playground.restitems;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import io.cloudonix.playground.restitems.model.Item;
import io.cloudonix.playground.restitems.model.ItemService;

public class PersonalData extends AbstractList<Item> {
	
	Instant lastAccess = Instant.now();
	
	private HashMap<Integer, Item> data = new HashMap<>();
	private int size = 0;
	
	public PersonalData(ItemService service) {
		int count = 10 + new Random().nextInt(50);
		for (int i = 0; i < count; i++)
			add(service.create());
	}

	public boolean old() {
		return Instant.now().isAfter(lastAccess.plus(Duration.ofDays(7)));
	}

	@Override
	public Item get(int index) {
		return data.get(index);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Item remove(int index) {
		return data.remove(index);
	}

	@Override
	public Item set(int index, Item element) {
		element.setId(index);
		return data.put(index, element);
	}

	@Override
	public boolean add(Item e) {
		int index = size();
		e.setId(index);
		data.put(index, e);
		size++;
		return true;
	}
}
