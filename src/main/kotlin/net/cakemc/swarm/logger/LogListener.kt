package net.cakemc.swarm.logger

interface LogListener {
    fun log(logEvent: LogEvent?)
}
