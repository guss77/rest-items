package io.cloudonix.playground.restitems;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;

public class Server extends AbstractVerticle {
	
	@Inject
	Configuration conf;
	
	@Inject
	HttpServer httpServer;
	
	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		try {
			Injector injector = Guice.createInjector(new AppConfiguration(config(), vertx));
			injector.injectMembers(this);
			httpServer.listen(conf.getPort()).<Void>mapEmpty().andThen(startPromise);
		} catch (Exception e) {
			startPromise.fail(e);
		}
	}

	public static void main(String[] args) {
		Launcher.executeCommand("run", Server.class.getName());
	}
}
