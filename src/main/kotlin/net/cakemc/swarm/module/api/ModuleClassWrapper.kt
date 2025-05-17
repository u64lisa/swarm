package net.cakemc.swarm.module.api

class ModuleClassWrapper(
    val clazz: Class<Module>,

    // information
    val description: ModuleInfo,
) {

    var instance: Module? = null

    fun initialize(): Module {
        val construct = this.clazz.getDeclaredConstructor()
        val instance = construct.newInstance()

        this.instance = instance
        return instance
    }

}