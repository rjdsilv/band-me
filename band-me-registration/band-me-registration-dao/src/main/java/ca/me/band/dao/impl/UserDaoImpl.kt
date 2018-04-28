package ca.me.band.dao.impl

import ca.me.band.dao.UserDao
import ca.me.band.model.User
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
 *
 * TODO Add Logger class.
 * TODO Setup Hazelcast as second level cache for the application.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Repository("userDao")
open class UserDaoImpl : GenericDaoImpl<Long, User>(), UserDao {
	override var modelClass : Class<User> = User::class.java

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