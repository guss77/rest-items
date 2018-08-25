package io.cloudonix.playground.restitems.api;

import java.util.Objects;

import io.cloudonix.playground.restitems.DataStore;
import io.cloudonix.playground.restitems.PersonalData;
import io.cloudonix.playground.restitems.model.ItemService;
import io.vertx.ext.web.RoutingContext;
import tech.greenfield.vertx.irked.Request;
import tech.greenfield.vertx.irked.status.Unauthorized;

public class ItemsRequest extends Request {
	
	static DataStore data = new DataStore();
	private PersonalData userData;

	private ItemService service;
	
	public ItemsRequest(RoutingContext outerContext, ItemService service) {
		super(outerContext);
		this.service = service;
		readAuth(request().getHeader("Authorization"));
	}

	private void readAuth(String header) {
		if (Objects.isNull(header))
			throw new Unauthorized().uncheckedWrap();
		String[] parts = header.split(" +");
		if (parts.length != 2)
			throw new Unauthorized().uncheckedWrap();
		if (!parts[0].equalsIgnoreCase("bearer"))
			throw new Unauthorized().uncheckedWrap();
		userData = data.getUserData(parts[1], service);
	}
	
	public PersonalData getData() {
		return userData;
	}
	
}
