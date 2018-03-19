package ca.me.band.model

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class GenericEntity<out PK : Serializable> : GenericModel<PK>() {
	@Column(name = "IS_ACTIVE", nullable = false)
	var active: Boolean = false

	@Column(name = "CREATE_DATE", nullable = false, updatable = false)
	var createDate: LocalDateTime? = null

	@Column(name = "LAST_UPDATE_DATE")
	var lastUpdateDate: LocalDateTime? = null

	@Column(name = "LAST_INACTIVATION_DATE")
	var lastInactivationDate: LocalDateTime? = null

	@Column(name = "LAST_ACTIVATION_DATE")
	var lastActivationDate: LocalDateTime? = null
}