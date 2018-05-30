package ca.me.band.rest.resources

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Component
@Path("registration")
open class RegistrationResource {
	@GET
	@Produces("text/plain")
	@Transactional(propagation = Propagation.REQUIRED)
	open fun test() : Response = Response.ok("OK").build()
}