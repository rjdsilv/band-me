package ca.me.band.rest.resources

import ca.me.band.service.RegistrationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Component
@Path("registration")
open class RegistrationResource {
	@Autowired
	private var registrationService: RegistrationService? = null

	@GET
	@Transactional(propagation = Propagation.REQUIRED)
	open fun test() : Response {
		return Response.ok("Webservice called").build()
	}
}