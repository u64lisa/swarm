package testing.networking

import net.cakemc.swarm.logger.Logger
import java.util.logging.Level

fun main() {

    val logger = Logger.getLogger("test")
    logger.log(Level.INFO, "test")

}
