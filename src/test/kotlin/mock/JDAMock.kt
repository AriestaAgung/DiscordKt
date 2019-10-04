package mock

import io.mockk.*
import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.api.dsl.CommandsContainer
import me.aberrantfox.kjdautils.discord.Discord
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Category
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.requests.RestAction

val categoryMock = mockk<Category> {
    every { id } returns FakeIds.Category
}

val messageMock = mockk<Message> {
    every { id } returns FakeIds.Message
}

val guildMock = mockk<Guild> {
    every { id } returns FakeIds.Guild
}

val restActionMessageMock = mockk<RestAction<Message>> {
    every { complete() } returns messageMock
}

val channelMock = mockk<TextChannel> {
    every { retrieveMessageById(FakeIds.Message) } returns restActionMessageMock
}

val jdaMock = mockk<JDA> {
    every { getCategoryById(FakeIds.Category) } returns categoryMock
    every { getTextChannelById(FakeIds.Channel) } returns channelMock
    every { getGuildById(FakeIds.Guild) } returns guildMock
}

val discordMock = mockk<Discord> {
    every { jda } returns jdaMock
}

val commandContainerMock = mockk<CommandsContainer> {

}

val commandEventMock = mockk<CommandEvent> {
    every { discord } returns discordMock
    every { container } returns commandContainerMock
    every { channel } returns channelMock
}
