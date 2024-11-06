package io.github.holo314

import dev.kord.core.Kord
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.core.behavior.interaction.*
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.string
import kotlinx.coroutines.coroutineScope
import java.nio.file.Files
import java.nio.file.Path
import kotlin.random.Random

suspend fun main() = coroutineScope {
    val z = Zv1()
    val kord = Kord(System.getProperty("token") ?: Files.readString(Path.of("token")))

    generalPopulate(kord)
    Zv1.populate(kord)
    kord.on<GuildChatInputCommandInteractionCreateEvent> {
        when (interaction.command.rootName) {
            Zv1.FITTING_ACTION -> z.zroll(interaction)
            Zv1.DAMAGE_ACTION -> z.zdamage(interaction)
        }
    }

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}

suspend fun generalPopulate(kord: Kord) {
    kord.createGlobalChatInputCommand("d", "roll dice") {
        string("dice", "the dice to roll") {
            required = true
        }
    }
}


suspend fun rollFormat(interaction: GuildChatInputCommandInteraction) {
    val params = interaction.command
    val dice = params.strings["dice"]!!.split("d")
    val amount = dice[0].toInt()
    val dSize = dice[1].toInt()
    val user = interaction.user.mention

    val result = List(amount) { Random.nextInt(1, dSize + 1)}

    val diceString = "rolled: " + result.joinToString(separator = ", ")

    val messageStart = "$user roll ${params.strings["dice"]!!} ($amount dice of size $dSize)"

    interaction.respondPublic {
        content = messageStart + "\n" + diceString
    }
}