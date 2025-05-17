package net.cakemc.swarm.module.api

import java.util.concurrent.atomic.AtomicReference


abstract class ModuleTask(
    val identifier: String,
    val thread: AtomicReference<Thread>,
): Runnable {

    abstract fun start()

}