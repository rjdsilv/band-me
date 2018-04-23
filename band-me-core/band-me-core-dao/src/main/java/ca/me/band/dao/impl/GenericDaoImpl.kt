package ca.me.band.dao.impl

import ca.me.band.dao.GenericDao
import ca.me.band.model.GenericEntity
import ca.me.band.model.GenericModel
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.time.LocalDateTime

/**
 * Abstract class implementing the common methods found in all the DAO classes.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @param PK The primary key type to be used.
 * @param M The model type to be used.
 * @see GenericModel
 * @see GenericDao
 *
 * TODO Add Logger class.
 */
abstract class GenericDaoImpl<PK : Serializable, M : GenericModel<PK>> : GenericDao<PK, M> {
	@Autowired
	private var sessionFactory: SessionFactory? = null

	abstract var modelClass : Class<M>

	/**
	 * @see GenericDao
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	override fun findById(id: PK): M?  = getSession().get(modelClass, id)

	/**
	 * @see GenericDao
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	override fun insert(dbObject: M): M {
		if (dbObject is GenericEntity<*>) {
			dbObject.active = false
			dbObject.createDate = LocalDateTime.now()
		}
		getSession().saveOrUpdate(dbObject)
		return dbObject
	}

	/**
	 * @see GenericDao
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	override fun update(dbObject: M): M {
		if (dbObject is GenericEntity<*>) {
			dbObject.lastUpdateDate = LocalDateTime.now()
		}
		getSession().merge(dbObject)
		return dbObject
	}

	/**
	 * @see GenericDao
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	override fun delete(dbObject: M) {
		if (dbObject is GenericEntity<*>) {
			if (dbObject.active) {
				dbObject.active = false
				dbObject.lastInactivationDate = LocalDateTime.now()
				update(dbObject)
			}
		} else {
			getSession().delete(dbObject)
		}
	}

	protected fun getSession() : Session = sessionFactory!!.currentSession
}