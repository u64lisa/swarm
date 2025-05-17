package net.cakemc.swarm.logger

interface LoggerFactory {
    fun getLogger(name: String): ILogger
}
