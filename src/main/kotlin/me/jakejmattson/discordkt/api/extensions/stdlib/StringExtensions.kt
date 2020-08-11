@file:Suppress("unused")

package me.jakejmattson.discordkt.api.extensions.stdlib

import me.jakejmattson.discordkt.api.Discord
import me.jakejmattson.discordkt.api.extensions.jda.fullName

private val urlRegexes = listOf(
        "[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&//=]*)",
        "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&//=]*)"
).map { it.toRegex() }

private val inviteRegex = "(\n|.)*((discord|discordapp).(gg|me|io|com/invite)/)(\n|.)*".toRegex()

/**
 * Whether ot not this string matches a URL regex.
 */
fun String.containsURl() = urlRegexes.any { replace("\n", "").contains(it) }

/**
 * Whether or not this string matches the invite regex.
 */
fun String.containsInvite() = inviteRegex.matches(this)

/**
 * Whether or not this string is a valid boolean value (true/false/t/f).
 */
fun String.isBooleanValue() =
        when (this.toLowerCase()) {
            "true" -> true
            "false" -> true
            "t" -> true
            "f" -> true
            else -> false
        }

/**
 * Sanitize all mentions and replace them with their resolved discord names.
 */
fun String.sanitiseMentions(discord: Discord): String {
    val userRegex = "<@!?(\\d+)>".toRegex()
    val roleRegex = "<@&(\\d+)>".toRegex()
    val hereRegex = "@+here".toRegex()
    val everyone = "@+everyone".toRegex()

    val mentionMap = userRegex.findAll(this).map {
        val mention = it.value

        val resolvedName = discord.retrieveEntity { jda ->
            jda.retrieveUserById(mention.trimToID()).complete()?.fullName()
        } ?: mention

        mention to resolvedName
    } + roleRegex.findAll(this).map {
        val mention = it.value

        val resolvedName = discord.retrieveEntity { jda ->
            jda.getRoleById(mention.trimToID())?.name
        } ?: mention

        mention to resolvedName
    } + hereRegex.findAll(this).map { it.value to "here" }

    val newString = replaceMap(mentionMap.toList())

    val everyoneMap = everyone
            .findAll(newString)
            .map { it.value to "everyone" }
            .toList()

    return newString.replaceMap(everyoneMap)
}

/**
 * Trim any type of mention into an ID.
 */
fun String.trimToID() = takeUnless { startsWith("<") && endsWith(">") }
        ?: replaceMap(listOf("<", ">", "@", "!", "&", "#").zip(listOf("", "", "", "", "", "")))

private fun String.replaceMap(replacements: List<Pair<String, String>>): String {
    var result = this
    replacements.forEach { (l, r) -> result = result.replace(l, r) }
    return result
}