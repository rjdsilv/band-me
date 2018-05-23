package ca.me.band.service.impl

import ca.me.band.config.HibernateConfig
import ca.me.band.config.SecurityConfig
import ca.me.band.dao.UserDao
import ca.me.band.dao.impl.UserDaoImpl
import ca.me.band.model.User
import ca.me.band.service.RegistrationService
import ca.me.band.service.exception.RegistrationException
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Test class designed to test the RegistrationServiceImpl class methods.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @since 1.0.0
 * @see RegistrationServiceImpl
 */
@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = [(UserDaoImpl::class), (HibernateConfig::class), (SecurityConfig::class), (RegistrationServiceImpl::class)])
open class RegistrationServiceImplTest {
	@Autowired
	private var registrationService : RegistrationService? = null

	@Autowired
	private var userDao : UserDao? = null

	/**
	 * Test method to test an invalid user registration.
	 */
	@Test(expected = RegistrationException::class)
	@Transactional
	open fun testRegisterInvalidUser() {
		val user = User()
		user.email = "email"
		user.password = "password"
		registrationService!!.register(user)
	}

	/**
	 * Test method to test a valid user registration.
	 */
	@Test
	@Transactional
	open fun testRegisterValidUser() {
		val user = User()
		user.email = "email@email.com"
		user.password = "password"
		user.firstName = "First"
		user.middleName = "Middle"
		user.lastName = "Last"
		user.birthDate = LocalDate.of(1978, 10, 9)

		val registeredUser = registrationService!!.register(user)
		Assert.assertEquals(registeredUser, userDao!!.findById(user.getKey()))
		Assert.assertEquals(60, registeredUser.password!!.length)
		Assert.assertEquals(60, registeredUser.activationLink!!.length)
		Assert.assertNotNull(registeredUser.createDate)
	}

	/**
	 * Test method to test an existing user registration.
	 */
	@Test(expected = RegistrationException::class)
	@Transactional
	open fun testRegisterExistingUser() {
		val user = User()
		user.email = "email@email.com"
		user.password = "password"
		user.firstName = "First"
		user.middleName = "Middle"
		user.lastName = "Last"
		user.birthDate = LocalDate.of(1978, 10, 9)

		try {
			Assert.assertEquals(registrationService!!.register(user), userDao!!.findById(user.getKey()))
			registrationService!!.register(user)
		} catch (ex : RegistrationException) {
			Assert.assertEquals("The user ${user.email} is already registered on the system!", ex.message)
			throw ex
		}
	}

	/**
	 * Test method to test a user activation.
	 */
	@Test
	@Transactional
	open fun testActivate() {
		val user = User()
		user.email = "email@email.com"
		user.password = "password"
		user.firstName = "First"
		user.middleName = "Middle"
		user.lastName = "Last"
		user.birthDate = LocalDate.of(1978, 10, 9)

		val registeredUser = registrationService!!.register(user)
		Assert.assertEquals(registeredUser, userDao!!.findById(registeredUser.getKey()))
		Assert.assertFalse(registeredUser.active)
		Assert.assertFalse(registeredUser.active)

		val activatedUser = registrationService!!.activate(user.activationLink!!)
		Assert.assertTrue(activatedUser.active)
		Assert.assertNotNull(activatedUser.lastActivationDate)
		Assert.assertNotNull(activatedUser.lastUpdateDate)
	}

	/**
	 * Test method to test a user activation.
	 */
	@Test(expected = RegistrationException::class)
	@Transactional
	open fun testActivateExpiredLink() {
		val user = User()
		user.email = "email@email.com"
		user.password = "password"
		user.firstName = "First"
		user.middleName = "Middle"
		user.lastName = "Last"
		user.birthDate = LocalDate.of(1978, 10, 9)

		val registeredUser = registrationService!!.register(user)
		Assert.assertEquals(registeredUser, userDao!!.findById(registeredUser.getKey()))
		Assert.assertFalse(registeredUser.active)
		Assert.assertFalse(registeredUser.active)

		user.linkExpirationDate = LocalDateTime.now().minusDays(1)
		userDao!!.update(user)

		try {
			val activatedUser = registrationService!!.activate(user.activationLink!!)
			Assert.assertTrue(activatedUser.active)
			Assert.assertNotNull(activatedUser.lastActivationDate)
			Assert.assertNotNull(activatedUser.lastUpdateDate)
		} catch (ex : RegistrationException) {
			Assert.assertEquals("Cannot activate user as the given activation link is already expired!", ex.message)
			throw ex
		}
	}

	/**
	 * Test method to test a user inactivation.
	 */
	@Test
	@Transactional
	open fun testInactivate() {
		val user = User()
		user.email = "email@email.com"
		user.password = "password"
		user.firstName = "First"
		user.middleName = "Middle"
		user.lastName = "Last"
		user.birthDate = LocalDate.of(1978, 10, 9)

		val registeredUser = registrationService!!.register(user)
		Assert.assertEquals(registeredUser, userDao!!.findById(registeredUser.getKey()))
		Assert.assertFalse(registeredUser.active)
		Assert.assertFalse(registeredUser.active)

		val activatedUser = registrationService!!.activate(user.activationLink!!)
		Assert.assertTrue(activatedUser.active)
		Assert.assertNotNull(activatedUser.lastActivationDate)
		Assert.assertNotNull(activatedUser.lastUpdateDate)

		val inactivatedUser = registrationService!!.inactivate(user)
		Assert.assertFalse(inactivatedUser.active)
		Assert.assertNotNull(inactivatedUser.lastInactivationDate)
	}
}