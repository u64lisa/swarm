package net.cakemc.swarm.module.api

import java.net.URL

abstract class AbstractModuleLoader {

    abstract fun listFilesAndLoad()
    abstract fun unloadAddon(name: String)
    abstract fun loadSingleAddon(url: URL): ModuleClassWrapper?
    abstract fun isLoaded(name: String): Boolean
    abstract fun resolveLoadedModule(): List<ModuleClassWrapper>
}