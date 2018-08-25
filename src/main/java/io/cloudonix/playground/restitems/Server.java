package io.cloudonix.playground.restitems;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import tech.greenfield.vertx.irked.Router;

public class Server extends AbstractVerticle {
	
	@Inject
	Configuration conf;
	
	@Inject
	Router router;
	
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		Json.mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
		Injector injector = Guice.createInjector(new AppConfiguration(config(), vertx));
		injector.injectMembers(this);
		Future<HttpServer> fut = Future.future();
		vertx.createHttpServer().requestHandler(router::accept).listen(conf.getPort(), fut);
		fut.<Void>map(v -> null).setHandler(startFuture);
	}

	public static void main(String[] args) {
		Launcher.executeCommand("run", Server.class.getName());
	}
}
