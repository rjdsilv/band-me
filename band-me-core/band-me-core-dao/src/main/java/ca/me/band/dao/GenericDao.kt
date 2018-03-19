package ca.me.band.dao

import ca.me.band.model.GenericModel
import java.io.Serializable

/**
 * Generic interface containing all the common methods that must be implemented by any DAO class.
 *
 * @param PK The primary key type that is going to be used.
 * @param M The model type that is going to be used.
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @see GenericModel
 */
interface GenericDao<PK : Serializable, M : GenericModel<PK>> {
	/**
	 * Finds an object of type M containing the given id.
	 *
	 * @param id The id to be found.
	 * @return The object found or null if no object is found on the database.
	 */
	fun findById(id : PK) : M?

	/**
	 * Inserts the give object into the database.
	 *
	 * @param dbObject The object to be inserted.
	 * @return The inserted object.
	 */
	fun insert(dbObject : M) : M

	/**
	 * Updates the given object into the database.
	 *
	 * @param dbObject The object to be updated.
	 * @return The updated object.
	 */
	fun update(dbObject : M) : M

	/**
	 * Logically removes the given object from the database. All database objects have a active property indicating
	 * if it is removed or not.
	 *
	 * @param dbObject The object to be removed.
	 */
	fun delete(dbObject : M)
}