package ca.me.band.dao

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
	 * Searches the database for an user with the given e-mail. If no user is found, null is returned.
	 *
	 * @param email The email to be searched.
	 * @return The user found or null if no user is found.
	 */
	fun findByEmail(email : String) : User?

	/**
	 * Searches the database for an user with the given activation link. If no user is found, null is returned.
	 *
	 * @param activationLink The activation link to be searched.
	 * @return The user found or null if no user is found.
	 */
	fun findByActivationLink(activationLink : String) : User?
}