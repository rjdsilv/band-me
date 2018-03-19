package ca.me.band.dao.impl

import ca.me.band.dao.UserDao
import ca.me.band.model.User
import org.springframework.stereotype.Repository

@Repository("userDao")
class UserDaoImpl : GenericDaoImpl<Long, User>(), UserDao {
	override var modelClass : Class<User> = User::class.java
}