package io.cloudonix.playground.restitems.api;

import com.google.inject.Inject;

import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import tech.greenfield.vertx.irked.Controller;
import tech.greenfield.vertx.irked.Request;
import tech.greenfield.vertx.irked.annotations.Endpoint;
import tech.greenfield.vertx.irked.annotations.OnFail;

public class Main extends Controller {

	@Endpoint("/*")
	BodyHandler bodyHandler = BodyHandler.create();
	
	@Endpoint("/*")
	CorsHandler corsHandler = CorsHandler.create("*");
	
	@Inject
	@Endpoint("/items")
	Items items;
	
	@OnFail
	WebHandler failHandler = Request.failureHandler();
}
