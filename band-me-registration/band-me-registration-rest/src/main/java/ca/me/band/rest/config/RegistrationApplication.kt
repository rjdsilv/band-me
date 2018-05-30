package ca.me.band.rest.config

import org.glassfish.jersey.server.ResourceConfig
import org.springframework.stereotype.Component
import javax.ws.rs.ApplicationPath

@Component
@ApplicationPath("rest")
class RegistrationApplication() : ResourceConfig() {
	init {
		packages("ca.me.band.rest")
	}
}