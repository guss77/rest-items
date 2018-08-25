package io.cloudonix.playground.restitems;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import io.cloudonix.playground.restitems.api.Main;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import tech.greenfield.vertx.irked.Irked;
import tech.greenfield.vertx.irked.Router;
import tech.greenfield.vertx.irked.exceptions.InvalidRouteConfiguration;

public class AppConfiguration extends AbstractModule {
	private Configuration config;
	private Vertx vertx;

	public AppConfiguration(JsonObject config, Vertx vertx) {
		this.config = config.mapTo(Configuration.class);
		this.vertx = vertx;
	}

	@Override
	protected void configure() {
		super.configure();
		bind(Configuration.class).toInstance(config);
		bind(Vertx.class).toInstance(vertx);
	}
	
	@Provides
	Router provideRouter(Main main) throws InvalidRouteConfiguration {
		return Irked.router(vertx).configure(main);
	}
}
