package net.cakemc.swarm.networking

import net.cakemc.swarm.Member
import net.cakemc.swarm.event.EventBus
import net.cakemc.skrilla.networking.handler.ConnectionHandler

interface EndPoint {
    fun start()
    fun close()
    fun handler(): ConnectionHandler
    fun eventBus(): EventBus
    fun member(): Member
}

