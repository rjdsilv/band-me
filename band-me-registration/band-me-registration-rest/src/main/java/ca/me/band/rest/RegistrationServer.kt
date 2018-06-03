package ca.me.band.rest

/**
 * Reads the port parameter from the command line and starts the server. If no parameter is passed, the default
 * value 9998 will be used.
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 */
fun main(args : Array<String>) {
	val server = RestServer()
	val portParam = "--port"
	val portParamIdx = 0
	val portValIdx = 1

	// Only reads the parameter if the correct number of parameters are passed.
	if (args.size == 2) {
		if (args[portParamIdx].equals(portParam)) {
			server.restPort = args[portValIdx].toInt()
		}
	}

	// Starts the server.
	server.start()
}