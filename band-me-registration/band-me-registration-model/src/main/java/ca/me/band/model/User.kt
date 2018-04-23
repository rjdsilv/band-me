package ca.me.band.model

import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "TBBM_USER")
class User : GenericEntity<Long>() {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", nullable = false, scale = 15, precision = 0, unique = true)
	var id : Long = -1

	@Email
	@Column(name = "EMAIL", nullable = false, length = 100, unique = true)
	var email : String? = null

	@Column(name = "PASSWORD", nullable = false, length = 60, columnDefinition = "CHAR(60)")
	var password : String? = null

	@Column(name = "FIRST_NAME", nullable = false, length = 50)
	var firstName : String? = null

	@Column(name = "MIDDLE_NAME", length = 50)
	var middleName : String? = null

	@Column(name = "LAST_NAME", nullable = false, length = 100)
	var lastName : String? = null

	@Column(name = "BIRTH_DATE")
	var birthDate : LocalDate? = null

	@Column(name = "ACTIVATION_LINK", nullable = false, length = 60, unique = true, columnDefinition = "CHAR(60)")
	var activationLink : String? = null

	@Column(name = "LINK_EXPIRATION_DATE", nullable = false)
	var linkExpirationDate : LocalDateTime? = null

	override fun getKey() : Long = id

	/**
	 * Method created to encode the user password according to the defined rules.
	 */
	fun encodePassword(passwordEncoder: PasswordEncoder) {
		password = passwordEncoder.encode(password)
	}

	/**
	 * Method to generate an activation link based on the user properties.
	 */
	fun generateActivationLink(passwordEncoder: PasswordEncoder, daysToExpire : Long) {
		activationLink = passwordEncoder.encode(email!! + password!! + LocalDateTime.now().nano)
		linkExpirationDate = LocalDateTime.now().plusDays(daysToExpire)
	}
}