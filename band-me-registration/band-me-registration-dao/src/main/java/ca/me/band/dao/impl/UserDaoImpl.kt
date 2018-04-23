package ca.me.band.dao.impl

import ca.me.band.dao.UserDao
import ca.me.band.dao.exception.RegistrationException
import ca.me.band.model.User
import org.hibernate.PropertyValueException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Class responsible for implementing all the necessary methods to allow an user interact with
 * the database.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @see UserDao
 *
 * TODO Add Logger class.
 * TODO Setup Hazelcast as second level cache for the application.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Repository("userDao")
@PropertySource("classpath:registration.properties")
open class UserDaoImpl : GenericDaoImpl<Long, User>(), UserDao {
	@Autowired
	private var env : Environment? = null

	@Autowired
	private var passwordEncoder : PasswordEncoder? = null

	override var modelClass : Class<User> = User::class.java

	/**
	 * @see UserDao
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Throws(RegistrationException::class)
	override fun register(user : User): User {
		try {
			// User already exists on the database
			if (null != findByEmail(user.email!!)) {
				throw RegistrationException("The user ${user.email} is already registered on the system!")
			}

			// Encodes the user password and creates the activation link.
			user.encodePassword(passwordEncoder!!)
			user.generateActivationLink(passwordEncoder!!, env!!.getProperty("link.expiration.days").toLong())

			// Inserts the user in the database, as it doesn't exist.
			insert(user)

			// TODO Send activation email to the user.
		} catch (ex : PropertyValueException) {
			throw RegistrationException("The user is not valid for registration. Please, fix it before proceeding!", ex)
		}

		return user
	}

	/**
	 * @see UserDao
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Throws(RegistrationException::class)
	override fun activate(activationLink: String): User {
		// Invalid activation link. Throws an exception.
		val user = findByActivationLink(activationLink) ?: throw RegistrationException("The given activation link is invalid and cannot be used!")

		if (user.linkExpirationDate!!.isBefore(LocalDateTime.now())) {
			user.generateActivationLink(passwordEncoder!!, env!!.getProperty("link.expiration.days").toLong())
			// TODO send a new activation link email
			throw RegistrationException("Cannot activate user as the given activation link is already expired!")
		}

		user.active = true
		user.lastActivationDate = LocalDateTime.now()
		return update(user)
	}

	/**
	 * @see UserDao
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	override fun inactivate(user: User): User {
		// Only goes to the database if the user is active.
		if (user.active) {
			delete(user)
		}

		return user
	}

	/**
	 * @see UserDao
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	override fun findByEmail(email: String): User? {
		return getSession().createQuery("select u from ca.me.band.model.User u where u.email = :email", User::class.java)
				.setParameter("email", email)
				.uniqueResult()
	}

	/**
	 * @see UserDao
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	override fun findByActivationLink(activationLink: String): User? {
		return getSession().createQuery("select u from ca.me.band.model.User u where u.activationLink = :activationLink", User::class.java)
				.setParameter("activationLink", activationLink)
				.uniqueResult()
	}
}