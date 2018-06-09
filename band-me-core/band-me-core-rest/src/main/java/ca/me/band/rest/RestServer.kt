package ca.me.band.rest

import ca.me.band.rest.config.RestApplication
import org.apache.logging.log4j.LogManager
import org.glassfish.grizzly.http.CompressionConfig
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.springframework.http.MediaType
import java.net.InetAddress
import javax.ws.rs.core.UriBuilder

/**
 * Class that will be responsible for starting a RESTFul embedded server for any rest application.
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 */
class RestServer {
	private val restHost : String = InetAddress.getLocalHost().hostName
	private val restPath = "rest"
	private val logger = LogManager.getLogger(javaClass)
	var restPort = 9998

	fun start() {
		val server = GrizzlyHttpServerFactory
				.createHttpServer(
						UriBuilder.fromUri("http://$restHost/").port(restPort).path(restPath).build(),
						ResourceConfig.forApplication(RestApplication()), false)

		// Enables compression on the server.
		if (logger.isInfoEnabled) logger.info("Enabling server compression.")
		val compression = server.getListener("grizzly").compressionConfig
		compression.compressionMode = CompressionConfig.CompressionMode.ON
		compression.compressionMinSize = 1
		compression.setCompressibleMimeTypes(
				MediaType.TEXT_PLAIN_VALUE,
				MediaType.TEXT_HTML_VALUE,
				MediaType.TEXT_XML_VALUE,
				MediaType.APPLICATION_JSON_VALUE,
				MediaType.APPLICATION_XML_VALUE)

		// Starts the server.
		try {
			if (logger.isInfoEnabled) logger.info("Server created at $restHost:$restPort/$restPath")
			server.start()
			if (logger.isInfoEnabled) logger.info("Server Successfully started")
			System.out.println("Press ENTER to stop the server")
			System.`in`.read()
		} finally {
			server.shutdownNow()
		}
	}
}