package net.cakemc.swarm.networking.handler

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import net.cakemc.swarm.logger.Logger
import net.cakemc.skrilla.networking.codec.BossHandler
import net.cakemc.swarm.networking.codec.PacketDecoder
import net.cakemc.swarm.networking.codec.PacketEncoder

class ServerChannelInitializer(
    private val bossHandler: BossHandler,
) : ChannelInitializer<SocketChannel>() {

    private val logger = Logger.getLogger("server-initializer")

    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        pipeline.addFirst("decoder", PacketDecoder())
        pipeline.addAfter("decoder", "encoder", PacketEncoder())
        pipeline.addAfter("encoder", "boss", bossHandler)
    }

}
