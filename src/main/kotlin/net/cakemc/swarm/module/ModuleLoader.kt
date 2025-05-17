package net.cakemc.cerberus.addon

import net.cakemc.cerberus.addon.loader.ModuleClassLoader
import net.cakemc.swarm.logger.Logger
import net.cakemc.swarm.module.api.ModuleClassWrapper
import net.cakemc.swarm.module.api.AbstractModuleLoader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.Arrays
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level

class ModuleLoader(val path: Path): AbstractModuleLoader() {

    val logger = Logger.getLogger("module-loader")

    val modules = ConcurrentHashMap<ModuleClassWrapper, ModuleClassLoader>()

    override fun resolveLoadedModule(): List<ModuleClassWrapper> {
        return modules.keys().toList()
    }

    override fun listFilesAndLoad() {
        val paths = Files.list(path).filter { it.fileName.toString().endsWith(".jar") }.toList()

        logger.log(Level.INFO, "found ${paths.size} addons in directors...")
        paths.forEach {
            val start = System.currentTimeMillis()
            val tempFile = createTemporaryCopy(it)
            val url = tempFile.toUri().toURL()

            val addonWrapper = loadSingleAddon(url)
            if (addonWrapper == null)
                return

            val addon = addonWrapper.initialize()
            logger.log(Level.INFO, "initializing addon '${addonWrapper.description.name}'" +
                    " took: ${System.currentTimeMillis()-start}ms")
            addon.load()
        }
    }

    private fun createTemporaryCopy(originalPath: Path): Path {
        val tempFile = Files.createTempFile("addon-", ".jar")
        Files.copy(originalPath, tempFile, StandardCopyOption.REPLACE_EXISTING)
        tempFile.toFile().deleteOnExit()
        return tempFile
    }

    override fun unloadAddon(name: String) {
        val addon = modules.keys().toList()
            .filter { it.description.name.equals(name, true) }.first()

        if (addon == null)
            return

        if (addon.instance == null)
            return

        addon.instance!!.unload()

        val loader = modules.get(addon)
        if (loader == null)
            return

        loader.shutdown()
        modules.remove(addon)
    }

    override fun loadSingleAddon(url: URL): ModuleClassWrapper? {
        val loader = ModuleClassLoader(url)
        val main = loader.findMainClass()

        if (main == null) {
            logger.log(Level.WARNING, "can't load addon: §6${url.path}")
            logger.log(Level.WARNING, "   reason: §ccan't find main class")
            loader.close()
            return null;
        }



        logger.log(Level.INFO, "loaded addon '${main.description.name}' - " +
                "'${main.description.version}' by " +
                "'${(Arrays.stream(main.description.author).map { it.name }.toList().joinToString(", "))}'" +
                " successfully!")

        modules.put(main, loader)
        return main
    }

    override fun isLoaded(name: String): Boolean {
        return modules.any { it.key.description.name.equals(name, true) && it.key.instance != null }
    }

}