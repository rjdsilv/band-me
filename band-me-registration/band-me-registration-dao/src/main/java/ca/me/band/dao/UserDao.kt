package ca.me.band.dao

import ca.me.band.model.User

interface UserDao : GenericDao<Long, User> {
}