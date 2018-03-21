package ca.me.band.dao.exception

/**
 * Class that will be thrown whenever any registration error occur in the application.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @see Exception
 */
class RegistrationException : Exception {
	constructor(message: String?) : super(message)

	constructor(message: String?, cause: Throwable?) : super(message, cause)
}