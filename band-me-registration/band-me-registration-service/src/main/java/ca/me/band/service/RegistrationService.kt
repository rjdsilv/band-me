package ca.me.band.service

import ca.me.band.model.User
import ca.me.band.service.exception.RegistrationException

/**
 * Interface containing all the methods that needs to be implemented by the registration service in order to
 * register users in the system.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 */
interface RegistrationService {
	/**
	 * Registers the user in the system if all the minimum requirements are met..
	 *
	 * @param user The user to be registered.
	 * @return The registered user.
	 * @throws RegistrationException if any error occur during the registration.
	 */
	@Throws(RegistrationException::class)
	fun register(user : User) : User

	/**
	 * Activates the user containing the given activation link provided it is not yet expired. If the link is
	 * already expired, an exception is thrown to indicate that the user could not be activated, a new activation
	 * link is generated and a new email is sent.
	 *
	 * @param activationLink The activation link for which the user must be activated.
	 * @return The activated user.
	 * @throws RegistrationException if the link is expired and the activation fails or the activation link is invalid.
	 */
	@Throws(RegistrationException::class)
	fun activate(activationLink : String) : User

	/**
	 * Inactivates an existing user in the database.
	 *
	 * @param user The user to be inactivated.
	 * @return The inactivated user.
	 */
	@Throws(RegistrationException::class)
	fun inactivate(user : User) : User
}