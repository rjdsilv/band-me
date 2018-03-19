package ca.me.band.dao.impl

import ca.me.band.config.HibernateConfig
import ca.me.band.dao.UserDao
import ca.me.band.model.User
import org.hibernate.PropertyValueException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Test class designed to test the UserDaoImpl class methods.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @since 1.0.0
 * @see UserDaoImpl
 */
@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = [(UserDaoImpl::class), (HibernateConfig::class)])
open class UserDaoImplTest {
	@Autowired
	private var userDao : UserDao? = null

	/**
	 * Test method to get exception when not null fields are set to null.
	 */
	@Test(expected = PropertyValueException::class  )
	@Transactional(readOnly = true)
	open fun testNullFields() {
		var user = User()
		user.active = true
		user.createDate = LocalDateTime.now();
		user = userDao!!.insert(user)
		assertEquals(user, userDao!!.findById(user.id))
	}

	/**
	 * Test method to test the findById method.
	 */
	@Test
	@Transactional
	open fun testFindById() {
		assertNull(userDao!!.findById(1))

		var user = User()
		user.email = "email@email.com"
		user.password = "password"
		user.firstName = "First"
		user.lastName = "Last"
		user.active = true
		user.createDate = LocalDateTime.now();

		user = userDao!!.insert(user)
		assertEquals(user, userDao!!.findById(user.id))
	}
}