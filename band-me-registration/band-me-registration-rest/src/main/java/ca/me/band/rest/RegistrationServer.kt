package ca.me.band.rest

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import javax.ws.rs.core.UriBuilder


fun main(args : Array<String>) {
	val server = GrizzlyHttpServerFactory
			.createHttpServer(
					UriBuilder.fromUri("http://localhost/").port(9998).build(),
					ResourceConfig().packages("ca.me.band.rest"))//ResourceConfig.forApplicationClass(RegistrationApplication::class.java))
	server.start()
}