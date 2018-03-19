package ca.me.band.model

import java.io.Serializable
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class GenericModel<out PK : Serializable> : Serializable {
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