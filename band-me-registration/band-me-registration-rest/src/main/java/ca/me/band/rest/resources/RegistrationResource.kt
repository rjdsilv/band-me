package ca.me.band.rest.resources

import ca.me.band.model.RegistrationToken
import ca.me.band.service.RegistrationService
import ca.me.band.service.exception.RegistrationException
import ca.me.band.utils.LogUtils
import org.apache.logging.log4j.LogManager
import org.glassfish.grizzly.http.server.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component
@Path("registration")
open class RegistrationResource {
	private val logger = LogManager.getLogger(javaClass.simpleName)
	private val logUtils = LogUtils

	@Autowired
	private var registrationService : RegistrationService? = null

	@Autowired
	private var passwordEncoder : PasswordEncoder? = null

	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Transactional(propagation = Propagation.REQUIRED)
	open fun test() : Response {
		return Response.ok("Webservice called").build()
	}

	/**
	 * Resource method responsible for generate the authentication token for the registration process.
	 * @param request The request from where to get the IP address.
	 * @return The generated response to the client.
	 * @throws WebApplicationException if any error occur during the token generation.
	 */
	@GET
	@Path("authToken")
	@Produces(MediaType.APPLICATION_JSON)
	open fun generateRegistrationAuthToken(@Context request : Request) : Response {
		try {
			logUtils.debug(logger, "Calling the Resource to generate the registration token")
			val registrationToken = RegistrationToken()
			registrationToken.token = passwordEncoder!!.encode("" + request.remoteAddr + Date())
			logUtils.info(logger, "Generated Token: ${registrationToken.token}")
			return Response.ok(registrationToken).build()
		} catch (ex : Exception) {
			logUtils.error(logger, ex.message!!, ex)
			throw WebApplicationException(
					RegistrationException("An error occurred during the request to the authentication token", ex),
					Response.serverError().build()
			)
		}
	}
}