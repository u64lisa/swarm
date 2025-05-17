package net.cakemc.swarm.module.api

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Author(
    val name: String,
    val website: String = "",
    val role: String,
)
