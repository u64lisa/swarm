plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "swarm"
include("dns")
include("cluster")
include("loader")
include("store")
