package me.aberrantfox.kjdautils.internal.arguments

import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.internal.command.*
import java.io.File

open class FileArg(override val name: String = "File") : ArgumentType<File>() {
    companion object : FileArg()

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<File> {
        val attachments = event.message.attachments

        if (attachments.isEmpty())
            return ArgumentResult.Error("No attachments in message.")

        val file = attachments.first().downloadToFile().get()

        return ArgumentResult.Success(file, 0)
    }

    override fun generateExamples(event: CommandEvent<*>) = listOf("File")
}