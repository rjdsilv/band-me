package ca.me.band.dao

import ca.me.band.dao.exception.RegistrationException
import ca.me.band.model.User

/**
 * Interface containing all the methods that needs to be implemented by an user in order to interface
 * with the database.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @see GenericDao
 */
interface UserDao : GenericDao<Long, User> {
	/**
	 * Validates if the user is allowed to login into the database.
	 *
	 * @param user The user to be registered.
	 * @Return The registered user.
	 * @throws RegistrationException if any error occur during the registration.
	 */
	@Throws(RegistrationException::class)
	fun register(user : User) : User
}