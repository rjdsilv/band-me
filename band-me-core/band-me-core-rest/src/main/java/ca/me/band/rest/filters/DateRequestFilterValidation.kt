package ca.me.band.rest.filters

import org.apache.logging.log4j.LogManager
import org.glassfish.grizzly.http.server.Request
import java.util.*
import javax.inject.Inject
import javax.ws.rs.WebApplicationException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider

/**
 * Filter class that will be responsible for validating if a given request is within the validity date or not.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 */
@Provider
class DateRequestFilterValidation : ContainerRequestFilter {
	// TODO Read value from configuration file.
	private val logger = LogManager.getLogger(javaClass.simpleName)
	private val reqValInMin = 1L

	@Inject
	private var requestProvider : javax.inject.Provider<Request>? = null

	/**
	 * Validates the validity date for the given request.
	 */
	override fun filter(request: ContainerRequestContext?) {
		val date = request!!.date

		if ((date == null) || (Date().time - date.time > reqValInMin * 60L * 1000L)) {
			logger.error("########## :: ${requestProvider!!.get().remoteAddr} - Invalid Request: REQUEST TO OLD")
			throw WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build())
		}
	}
}