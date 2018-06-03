package ca.me.band.rest.config

import org.glassfish.jersey.server.ResourceConfig
import javax.ws.rs.ApplicationPath

@ApplicationPath("")
class RestApplication() : ResourceConfig() {
	init {
		packages("ca.me.band.rest")
	}
}