package net.cakemc.swarm.logger

import net.cakemc.swarm.Member
import java.util.*
import java.util.logging.LogRecord

class LogEvent(
    val logRecord: LogRecord,
    val groupName: String,
    var member: Member
) : EventObject(member) {}
