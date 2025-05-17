package net.cakemc.swarm.event.impl

import io.netty.channel.Channel
import net.cakemc.swarm.networking.codec.Packet

class PacketReceivedEvent(val channel: Channel, val packet: Packet)