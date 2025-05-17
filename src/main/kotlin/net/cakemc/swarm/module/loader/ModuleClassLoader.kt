package net.cakemc.cerberus.addon.loader

import net.cakemc.cerberus.addon.validation.ModuleClassValidation
import net.cakemc.swarm.module.api.*
import java.net.URL
import java.net.URLClassLoader
import java.util.ArrayList
import java.util.LinkedList
import java.util.jar.JarFile

class ModuleClassLoader(val url: URL): URLClassLoader(arrayOf(url)) {
    val blackListed = listOf("module-info", "package-info")

    val loadedClasses = LinkedList<Class<*>>()

    val validator = ModuleClassValidation()

    init {
        registerAsParallelCapable()

        val classes = ArrayList<Class<*>>()
        val jarFile = JarFile(url.file)

        val entryIterator = jarFile.entries().asIterator();

        while (entryIterator.hasNext()) {
            val entry = entryIterator.next()
            val entryName = entry.name

            if (entry.isDirectory || !entryName.endsWith(".class"))
                continue

            if (blackListed.stream().anyMatch { entryName.contains(it) })
                continue

            val classPath = entry.name.replace("/", ".")
                .replace(".class", "");

            val classInstance = this.loadClass(classPath)
            classes.add(classInstance)
        }

        jarFile.close()

        loadedClasses.addAll(classes)
    }

    @Suppress("UNCHECKED_CAST")
    fun findMainClass(): ModuleClassWrapper? {
        for (loadedClass in this.loadedClasses) {
            if (!validator.validate(loadedClass))
                continue

            if (loadedClass.superclass != Module::class.java)
                break

            val mainClass: Class<Module> = loadedClass
                    as Class<Module>;

            val description = mainClass.getDeclaredAnnotation(ModuleInfo::class.java)

            val wrapper = ModuleClassWrapper(
                loadedClass, description
            )

            return wrapper
        }
        return null
    }

    fun shutdown() {
        this.close()
        this.loadedClasses.clear()
    }

}