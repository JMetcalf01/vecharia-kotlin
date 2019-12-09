package vecharia.logging

/**
 * Logs to the console.
 *
 * @author Jonathan Metcalf
 * @since 1.0
 *
 * @param level the level for output
 */
class ConsoleLogger(private val level: Logger.Level) : Logger {
    /**
     * Logs a debug message, if the log level is at or above DEBUG.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @see vecharia.logging.Logger.Level
     * @param message the message to log.
     */
    override fun debug(message: String) {
        if (level.ordinal == 0)
            println("[DEBUG] $message")
    }

    /**
     * Logs an info message, if the log level is at or above INFO.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @see vecharia.logging.Logger.Level
     * @param message the message to log.
     */
    override fun info(message: String) {
        if (level.ordinal <= 1)
            println("[INFO] $message")
    }

    /**
     * Logs a warn message, if the log level is at or above WARN.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @see vecharia.logging.Logger.Level
     * @param message the message to log.
     */
    override fun warn(message: String) {
        if (level.ordinal <= 2)
            println("[WARN] $message")
    }

    /**
     * Logs a error message, if the log level is at or above ERROR.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @see vecharia.logging.Logger.Level
     * @param message the message to log.
     */
    override fun error(message: String) {
        if (level.ordinal <= 3)
            println("[ERROR] $message")
    }
}