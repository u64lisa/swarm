package net.cakemc.swarm.module.api

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModuleInfo(
    val name: String,
    val version: String,
    val description: String = "default description",
    val author: Array<Author> = emptyArray(),
)
