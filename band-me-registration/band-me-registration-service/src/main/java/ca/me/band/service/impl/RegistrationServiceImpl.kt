package ca.me.band.service.impl

import ca.me.band.dao.UserDao
import ca.me.band.model.User
import ca.me.band.service.RegistrationService
import ca.me.band.service.exception.RegistrationException
import ca.me.band.utils.LogUtils
import org.apache.logging.log4j.LogManager
import org.hibernate.PropertyValueException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Class responsible for implementing all the necessary methods to allow user registration and other related
 * functionalities on the system.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @see RegistrationService
 *
 * TODO Add Logger class.
 * TODO Setup Hazelcast as second level cache for the application.
 */
@Service("registrationService")
@PropertySource("classpath:registration.properties")
open class RegistrationServiceImpl : RegistrationService {
	// Constant declarations
	companion object {
		const val LINK_EXP_DAYS_PROP = "link.expiration.days"
	}

	private val logger = LogManager.getLogger(javaClass.simpleName)
	private val logUtils = LogUtils

	@Autowired
	private var env : Environment? = null

	@Autowired
	private var passwordEncoder : PasswordEncoder? = null

	@Autowired
	private var userDao : UserDao? = null

	/**
	 * @see UserDao
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Throws(RegistrationException::class)
	override fun register(user : User): User {
		try {
			// User already exists on the database
			if (null != userDao!!.findByEmail(user.email!!)) {
				val message = "The user ${user.email} is already registered on the system!"
				logUtils.error(logger, message)
				throw RegistrationException(message)
			}

			// Encodes the user password and creates the activation link.
			logUtils.info(logger,"Encoding the password and generating activation link")
			user.encodePassword(passwordEncoder!!)
			user.generateActivationLink(passwordEncoder!!, env!!.getProperty(LINK_EXP_DAYS_PROP)!!.toLong())

			// Inserts the user in the database, as it doesn't exist.
			logUtils.info(logger, "Registering the user on the database")
			userDao!!.insert(user)

			// TODO Send activation email to the user.
		} catch (ex : PropertyValueException) {
			val message = "The user is not valid for registration. Please, fix it before proceeding!"
			logUtils.error(logger, message, ex)
			throw RegistrationException(message, ex)
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
		val user = userDao!!.findByActivationLink(activationLink) ?: throw RegistrationException("The given activation link is invalid and cannot be used!")

		if (user.linkExpirationDate!!.isBefore(LocalDateTime.now())) {
			user.generateActivationLink(passwordEncoder!!, env!!.getProperty(LINK_EXP_DAYS_PROP)!!.toLong())
			// TODO send a new activation link email
			throw RegistrationException("Cannot activate user as the given activation link is already expired!")
		}

		user.active = true
		user.lastActivationDate = LocalDateTime.now()
		return userDao!!.update(user)
	}

	/**
	 * @see UserDao
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	override fun inactivate(user: User): User {
		// Only goes to the database if the user is active.
		if (user.active) {
			userDao!!.delete(user)
		}

		return user
	}
}