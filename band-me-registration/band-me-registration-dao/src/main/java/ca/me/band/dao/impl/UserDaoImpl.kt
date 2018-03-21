package ca.me.band.dao.impl

import ca.me.band.dao.UserDao
import ca.me.band.dao.exception.RegistrationException
import ca.me.band.model.User
import org.hibernate.PropertyValueException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Class responsible for implementing all the necessary methods to allow an user interact with
 * the database.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @see UserDao
 */
@Repository("userDao")
open class UserDaoImpl : GenericDaoImpl<Long, User>(), UserDao {
	override var modelClass : Class<User> = User::class.java

	/**
	 * @see UserDao
	 * TODO Setup Hazelcast as second level cache for the application.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Throws(RegistrationException::class)
	override fun register(user : User): User {
		try {
			// Searches the database to make sure the user doesn't exist yet.
			val foundUser : User? = getSession().createQuery("from User where email = :email", User::class.java)
					.setParameter("email", user.email)
					.uniqueResult()

			// User already exists on the database
			if (null != foundUser) {
				throw RegistrationException("The user ${user.email} is already registered on the system!")
			}

			// Inserts the user in the database, as it doesn't exist.
			insert(user)
		} catch (ex : PropertyValueException) {
			throw RegistrationException("The user is not valid for registration. Please, fix it before proceeding!", ex)
		}

		return user
	}
}