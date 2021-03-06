package ca.me.band.dao.impl

import ca.me.band.config.HibernateConfig
import ca.me.band.dao.UserDao
import ca.me.band.model.User
import org.hibernate.PropertyValueException
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Test class designed to test the UserDaoImpl class methods.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @since 1.0.0
 * @see UserDaoImpl
 * TODO Add tests for findByEmail and findByActivationLink
 */
@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = [(UserDaoImpl::class), (HibernateConfig::class)])
open class UserDaoImplTest {
	@Autowired
	private var userDao : UserDao? = null

	/**
	 * Test method to get exception when not null fields are set to null.
	 */
	@Test(expected = PropertyValueException::class)
	@Transactional(readOnly = true)
	open fun testNullFields() {
		userDao!!.insert(User())
	}

	/**
	 * Test method to test the findById method.
	 */
	@Test
	@Transactional
	open fun testFindById() {
		assertNull(userDao!!.findById(1))

		val user = User()
		user.email = "email@email.com"
		user.password = "password"
		user.firstName = "First"
		user.middleName = "Middle"
		user.lastName = "Last"
		user.birthDate = LocalDate.of(1978, 10, 9)
		user.activationLink = "activationLink"
		user.linkExpirationDate = LocalDateTime.now()
		assertEquals(userDao!!.insert(user), userDao!!.findById(user.getKey()))
	}

	/**
	 * Test method to test the update method.
	 */
	@Test
	@Transactional
	open fun testUpdate() {
		// Creates the user.
		var user = User()
		user.email = "email@email.com"
		user.password = "password"
		user.firstName = "First"
		user.lastName = "Last"
		user.active = true
		user.activationLink = "activationLink"
		user.linkExpirationDate = LocalDateTime.now()

		// Inserts the user to be updated.
		user = userDao!!.insert(user)
		assertEquals(user, userDao!!.findById(user.id))

		// Updates the user.
		user.firstName = "Changed"
		user = userDao!!.update(user)
		assertEquals("Changed", user.firstName)
		assertNotNull(user.lastUpdateDate)
	}

	/**
	 * Test method to test the update method.
	 */
	@Test
	@Transactional
	open fun testDeleteGenericEntity() {
		// Creates the user.
		var user = User()
		user.email = "email@email.com"
		user.password = "password"
		user.firstName = "First"
		user.lastName = "Last"
		user.activationLink = "activationLink"
		user.linkExpirationDate = LocalDateTime.now()

		// Inserts the user to be updated.
		user = userDao!!.insert(user)
		assertEquals(user, userDao!!.findById(user.id))

		// Activates the user in the database.
		user.active = true
		user = userDao!!.update(user)
		assertTrue(user.active)

		// Updates the user.
		userDao!!.delete(user)
		assertFalse(user.active)
		assertNotNull(user.lastInactivationDate)
	}
}