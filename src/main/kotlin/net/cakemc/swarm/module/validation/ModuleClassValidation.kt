package net.cakemc.cerberus.addon.validation

import net.cakemc.swarm.logger.Logger
import net.cakemc.swarm.module.api.ModuleInfo
import net.cakemc.swarm.module.api.Module

class ModuleClassValidation {

    val logger = Logger.getLogger("module-loader")

    fun validate(aClass: Class<*>): Boolean {
        val addonClass = aClass.superclass == Module::class.java &&
                aClass.getDeclaredAnnotation(ModuleInfo::class.java) != null

        if (!addonClass)
            return false

        return true
    }

}