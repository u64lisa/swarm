package testing.networking

import net.cakemc.swarm.event.EventBus

class Event(val message: String)

fun main() {
  val eventBus = EventBus()

  eventBus.subscribe<Event> { event ->
    println(event.message)
  }

  eventBus.publish(Event("TEST"))
}