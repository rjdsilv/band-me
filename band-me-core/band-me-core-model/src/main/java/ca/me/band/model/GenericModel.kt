package ca.me.band.model

import java.io.Serializable
import javax.persistence.MappedSuperclass

/**
 * Superclass that will be used by any model class with a primary key.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.o
 * @param PK The primary key to be used.
 * @see Serializable
 */
@MappedSuperclass
abstract class GenericModel<out PK : Serializable> : Serializable {
	/**
	 * Method that will be used to retrieve any kind of primary key for the application.
	 *
	 * @return The model's primary key.
	 */
	abstract fun getKey() : PK

	override fun hashCode(): Int = getKey().hashCode()

	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}

		if (javaClass != other?.javaClass) {
			return false
		}

		other as GenericModel<*>

		if (getKey() != other.getKey()) {
			return false
		}

		return true
	}
}