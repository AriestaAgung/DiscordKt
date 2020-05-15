package me.aberrantfox.kjdautils.examples

import me.aberrantfox.kjdautils.api.dsl.PrefixDeleteMode
import me.aberrantfox.kjdautils.api.startBot
import java.awt.Color

fun main(args: Array<String>) {
    val token = args.firstOrNull()
        ?: throw IllegalArgumentException("No program arguments provided. Expected bot token.")

    startBot(token) {
        configure {
            //The prefix for commands that your bot will respond to
            prefix = "!"

            //Whether or not mentioning the bot can be used as a prefix
            allowMentionPrefix = false

            //The emoji that the bot will react on invocations; null for none
            commandReaction = "\uD83D\uDC40"

            //Which invocations should be deleted (by number of prefixes)
            deleteMode = PrefixDeleteMode.None

            //Whether or not error messages should be deleted after sending
            deleteErrors = false

            //Whether or not commands in direct messages are valid
            allowPrivateMessages = false

            //Color configuration for embeds within KUtils
            colors {
                successColor = Color.GREEN
                failureColor = Color.RED
                infoColor = Color.BLUE
            }

            //An embed produced when the bot is mentioned
            mentionEmbed { event ->
                val self = event.guild.jda.selfUser

                color = Color(0x00bfff)
                thumbnail = self.effectiveAvatarUrl
                addInlineField("Prefix", prefix)

                with(discord.properties) {
                    addField("Build Info", "```" +
                        "KUtils: $version\n" +
                        "Kotlin: $kotlinVersion\n" +
                        "JDA:    $jdaVersion\n" +
                        "```")

                    addInlineField("Source", repository)
                }
            }

            //A predicate to determine if a command is visible in this context
            visibilityPredicate {
                it.command.names.first().length < 50
            }
        }
    }
}