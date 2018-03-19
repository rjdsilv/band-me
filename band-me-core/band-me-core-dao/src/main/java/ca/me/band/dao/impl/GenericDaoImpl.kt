package ca.me.band.dao.impl

import ca.me.band.dao.GenericDao
import ca.me.band.model.GenericEntity
import ca.me.band.model.GenericModel
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable

/**
 * Abstract class implementing the common methods found in all the DAO classes.
 *
 * @param PK The primary key type to be used.
 * @param M The model type to be used.
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @see GenericModel
 * @see GenericDao
 */
abstract class GenericDaoImpl<PK : Serializable, M : GenericModel<PK>> : GenericDao<PK, M> {
	@Autowired
	protected var sessionFactory: SessionFactory? = null

	abstract var modelClass : Class<M>

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	override fun findById(id: PK): M?  = sessionFactory!!.currentSession.get(modelClass, id)

	@Transactional(propagation = Propagation.REQUIRED)
	override fun insert(dbObject: M): M {
		sessionFactory!!.currentSession.saveOrUpdate(dbObject)
		return dbObject
	}

	@Transactional(propagation = Propagation.REQUIRED)
	override fun update(dbObject: M): M {
		sessionFactory!!.currentSession.merge(dbObject)
		return dbObject
	}

	@Transactional(propagation = Propagation.REQUIRED)
	override fun delete(dbObject: M) {
		if (dbObject is GenericEntity<*>) {
			if (dbObject.active) {
				dbObject.active = false
				update(dbObject)
			}
		} else {
			sessionFactory!!.currentSession.delete(dbObject)
		}
	}
}