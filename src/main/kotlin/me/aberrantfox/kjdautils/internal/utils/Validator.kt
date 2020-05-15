package me.aberrantfox.kjdautils.internal.utils

import me.aberrantfox.kjdautils.api.dsl.KConfiguration
import me.aberrantfox.kjdautils.api.dsl.command.CommandsContainer
import me.aberrantfox.kjdautils.internal.arguments.EitherArg

internal class Validator {
    companion object {
        fun validateCommandMeta(commandsContainer: CommandsContainer) {
            commandsContainer.commands.forEach { command ->
                val args = command.expectedArgs.arguments
                val commandName = command.names.first()

                args.filterIsInstance<EitherArg<*, *>>().forEach {
                    if (it.left == it.right) {
                        val arg = it.left::class.toString().substringAfterLast(".").substringBefore("$")
                        InternalLogger.error("Detected EitherArg with identical args ($arg) in command: $commandName")
                    }
                }

                if (command.isFlexible) {
                    if (args.size < 2)
                        InternalLogger.error("Flexible commands must accept at least 2 arguments ($commandName)")
                    else {
                        val actualCount = args.size
                        val distinctCount = args.distinct().size

                        if (distinctCount != actualCount)
                            InternalLogger.error("Flexible commands must accept distinct arguments ($commandName)")
                    }
                }
            }
        }

        fun validateReaction(config: KConfiguration) {
            val currentReaction = config.commandReaction ?: return
            val emojiRegex = "[^\\x00-\\x7F]+ *(?:[^\\x00-\\x7F]| )*".toRegex()
            val isValid = emojiRegex.matches(currentReaction)

            if (!isValid) {
                InternalLogger.error("Provided command reaction is not valid. Falling back to no-reaction mode.")
                config.commandReaction = null
            }
        }
    }
}