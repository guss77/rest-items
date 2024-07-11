package io.cloudonix.playground.restitems.api;

import java.util.Objects;
import java.util.stream.Collector;

import com.google.inject.Inject;

import io.cloudonix.playground.restitems.model.Item;
import io.cloudonix.playground.restitems.model.ItemService;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import tech.greenfield.vertx.irked.Controller;
import tech.greenfield.vertx.irked.Request;
import tech.greenfield.vertx.irked.annotations.*;
import tech.greenfield.vertx.irked.status.BadRequest;
import tech.greenfield.vertx.irked.status.NoContent;
import tech.greenfield.vertx.irked.status.NotFound;

public class Items extends Controller {

	@Inject
	ItemService service;

	@Get("/")
	Handler<ItemsRequest> index = r -> {
		r.sendJSON(r.getData().stream().filter(Objects::nonNull).map(JsonObject::mapFrom)
				.collect(Collector.<JsonObject, JsonArray>of(JsonArray::new, JsonArray::add, JsonArray::add)));
	};

	@Post("/")
	Handler<ItemsRequest> create = r -> {
		r.getData().add(r.getBodyAs(Item.class));
		r.put("id", r.getData().size() - 1);
		r.next();
	};

	@Put("/:id")
	Handler<ItemsRequest> update = r -> {
		try {
			int index = Integer.parseInt(r.pathParam("id"));
			r.getData().set(index, r.getBodyAs(Item.class));
			r.put("id", index);
			r.next();
		} catch (NullPointerException e) {
			r.sendError(new NotFound());
		}
	};

	@Patch("/:id")
	Handler<ItemsRequest> merge = r -> {
		try {
			int index = Integer.parseInt(r.pathParam("id"));
			JsonObject body = r.body().asJsonObject();
			if (body.containsKey("sku")) {
				r.fail(new BadRequest());
				return;
			}
			r.getData().set(index, JsonObject.mapFrom(r.getData().get(index)).mergeIn(body,100).mapTo(Item.class).purgeProfile());
			r.put("id", index);
			r.next();
		} catch (NullPointerException e) {
			r.sendError(new NotFound());
		}
	};

	@Put("/:id")
	@Patch("/:id")
	@Post("/")
	@Get("/:id")
	Handler<ItemsRequest> retrieve = r -> {
		int index = r.data().containsKey("id") ? r.get("id") : Integer.parseInt(r.pathParam("id"));
		Item val = r.getData().get(index);
		if (Objects.isNull(val))
			r.sendError(new NotFound());
		else
			r.sendObject(val);
	};

	@Delete("/:id")
	Handler<ItemsRequest> delete = r -> {
		int index = Integer.parseInt(r.pathParam("id"));
		r.getData().remove(index);
		r.sendError(new NoContent());
	};

	@Override
	protected Request getRequestContext(Request request) {
		return new ItemsRequest(request, service);
	}
}
