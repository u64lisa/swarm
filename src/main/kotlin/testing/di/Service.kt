package testing.di

import net.cakemc.swarm.di.DI
import net.cakemc.swarm.di.DILoader

interface Service {
    fun ping()
}

class LoggingService : Service {
    override fun ping() = println("LoggingService::ping() called.")
}

class App {
    @DI
    var service: Service? = null

    fun run() {
        service?.ping()
        println("$service")
    }
}

fun main() {
    val di = DILoader()
    di.bind(Service::class, LoggingService::class)
    val app = di.get(App::class)
    app.run()
}
