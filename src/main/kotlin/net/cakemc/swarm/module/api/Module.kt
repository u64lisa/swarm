package net.cakemc.swarm.module.api

abstract class Module {

    open fun load() {}
    open fun unload() {}

}