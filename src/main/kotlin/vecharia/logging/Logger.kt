package vecharia.logging

/**
 * A multi purpose logger.
 *
 * @author Matt Worzala
 * @since 1.0
 */
interface Logger {
    /**
     * Logs a debug message, if the log level is at or above DEBUG.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @see vecharia.logging.Logger.Level
     * @param message the message to log.
     */
    fun debug(message: String)

    /**
     * Logs an info message, if the log level is at or above INFO.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @see vecharia.logging.Logger.Level
     * @param message the message to log.
     */
    fun info(message: String)

    /**
     * Logs a warn message, if the log level is at or above WARN.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @see vecharia.logging.Logger.Level
     * @param message the message to log.
     */
    fun warn(message: String)

    /**
     * Logs a error message, if the log level is at or above ERROR.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @see vecharia.logging.Logger.Level
     * @param message the message to log.
     */
    fun error(message: String)

    /**
     * The level of log message which will be processed.
     *
     * @author Matt Worzala
     * @since 1.0
     */
    enum class Level {
        DEBUG, INFO, WARN, ERROR
    }
}