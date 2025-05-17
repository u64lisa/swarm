package net.cakemc.swarm.logger

import java.util.logging.Level

interface LoggingService {
    fun addLogListener(level: Level, logListener: LogListener?)

    fun removeLogListener(logListener: LogListener?)

    fun getLogger(name: String): ILogger
}
