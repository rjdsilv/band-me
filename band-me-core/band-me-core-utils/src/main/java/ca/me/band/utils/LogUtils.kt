package ca.me.band.utils

import org.apache.logging.log4j.Logger

/**
 * Singleton object to help with logging in the application
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @see Logger
 */
object LogUtils {
	fun trace(logger : Logger, message : String) {
		if (logger.isTraceEnabled) {
			logger.trace("########## :: $message")
		}
	}

	fun debug(logger : Logger, message : String) {
		if (logger.isDebugEnabled) {
			logger.debug("########## :: $message")
		}
	}

	fun info(logger : Logger, message : String) {
		if (logger.isInfoEnabled) {
			logger.info("########## :: $message")
		}
	}

	fun warn(logger : Logger, message : String) {
		if (logger.isWarnEnabled) {
			logger.warn("########## :: $message")
		}
	}

	fun error(logger : Logger, message : String) {
		if (logger.isErrorEnabled) {
			logger.error("########## :: $message")
		}
	}

	fun error(logger : Logger, message : String, cause : Throwable) {
		if (logger.isErrorEnabled) {
			logger.error("########## :: $message", cause)
		}
	}

	fun fatal(logger : Logger, message : String) {
		if (logger.isFatalEnabled) {
			logger.fatal("########## :: $message")
		}
	}

	fun fatal(logger : Logger, message : String, cause : Throwable) {
		if (logger.isFatalEnabled) {
			logger.fatal("########## :: $message", cause)
		}
	}
}