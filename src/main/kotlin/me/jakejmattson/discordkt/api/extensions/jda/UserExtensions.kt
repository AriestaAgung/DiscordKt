@file:Suppress("unused")

package me.jakejmattson.discordkt.api.extensions.jda

import me.jakejmattson.discordkt.internal.utils.InternalLogger
import net.dv8tion.jda.api.entities.*

/**
 * Attempt to convert this user to a member with the given guild.
 */
fun User.toMember(guild: Guild) = guild.getMemberById(id)

/**
 * Send the user a private string message.
 */
fun User.sendPrivateMessage(msg: String) =
    openPrivateChannel().queue {
        it.sendMessage(msg).queue(null) {
            notifyDirectMessageError(fullName())
        }
    }

/**
 * Send the user a private embed message.
 */
fun User.sendPrivateMessage(msg: MessageEmbed) =
    openPrivateChannel().queue {
        it.sendMessage(msg).queue(null) {
            InternalLogger.error(fullName())
        }
    }

/**
 * A user's name and discriminator - user#1234
 */
fun User.fullName() = "$name#$discriminator"

/**
 * A Discord profile link for this user.
 */
val User.profileLink
    get() = "https://discordapp.com/users/$id/"

private fun notifyDirectMessageError(user: String) = InternalLogger.error("Failed to send private message to $user")