package me.aberrantfox.kjdautils.api.dsl

import me.aberrantfox.kjdautils.discord.Discord
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import net.dv8tion.jda.api.entities.MessageEmbed

class Conversation(val name: String,
                   val description: String,
                   val steps: List<Step>,
                   var onComplete: (ConversationStateContainer) -> Unit = {}
)

data class Step(val argumentType: ArgumentType<*>, val prompt: Any)

data class ConversationStateContainer(
    val userId: String,
    val guildId: String,
    var responses: MutableList<Any?>,
    val conversation: Conversation,
    var currentStep: Int,
    val discord: Discord) {
    fun respond(message: String) = discord.getUserById(userId)?.sendPrivateMessage(message)
    fun respond(message: MessageEmbed) = discord.getUserById(userId)?.sendPrivateMessage(message)
}

fun conversation(block: ConversationBuilder.() -> Unit): Conversation = ConversationBuilder().apply(block).build()

class ConversationBuilder {
    var name = ""
    var description = ""
    private val steps = mutableListOf<Step>()
    var onComplete: (ConversationStateContainer) -> Unit = {}

    fun steps(construct: Steps.() -> Unit) {
        val stepsBuilder = Steps()
        stepsBuilder.construct()
        steps.addAll(stepsBuilder.build())
    }

    fun onComplete(onComplete: (ConversationStateContainer) -> Unit) {
        this.onComplete = onComplete
    }

    fun build() = Conversation(name, description, steps, onComplete)
}

data class Steps(private val steps: ArrayList<Step> = arrayListOf()) {
    fun promptFor(argumentType: ArgumentType<*>, prompt: () -> Any) {
        steps.add(Step(argumentType, prompt.invoke()))
    }

    fun build() = steps
}

@Target(AnnotationTarget.FUNCTION)
annotation class Convo
