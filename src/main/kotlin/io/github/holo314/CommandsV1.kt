package io.github.holo314

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.interaction.string
import kotlin.random.Random

class Zv1 {
    companion object {
        const val FITTING_ACTION = "zroll"
        const val DAMAGE_ACTION = "zdamage"
        suspend fun populate(kord: Kord) {
            kord.createGlobalChatInputCommand(DAMAGE_ACTION, "damage action") {
                string("range", "lethal range, must be of the format 'n-m' for 0<n≤m<12") {
                    required = true
                }
            }

            kord.createGlobalChatInputCommand(FITTING_ACTION, "fitting action") {
                int("range", "wiggle stat") {
                    required = true
                    for (i in 0..10) {
                        choice("$i", i.toLong())
                    }
                }
            }
        }
    }

    suspend fun zroll(interaction: GuildChatInputCommandInteraction) {
        val rangeRadius = interaction.command.integers["range"]!!.toInt()
        val rangeStart = 10 - rangeRadius
        val rangeEnd = 10 + rangeRadius
        val result = Random.nextInt(1, 21)
        val endingText = "Roll: $result"
        val descText = "Fitting Range: ${rangeStart}-${rangeEnd}"
        val secondLine = "*$descText | $endingText*"

        val firstLineInner =
            if (result < rangeStart)
                "Bellow Fitting"
            else if (result > rangeEnd)
                "Above Fitting"
            else
                "Fitting"
        val firstLine = "**$firstLineInner**"
        interaction.respondPublic {
            content = "$firstLine\n$secondLine"
        }
    }

    suspend fun zdamage(interaction: GuildChatInputCommandInteraction) {
        val rangeText = interaction.command.strings["range"]!!
        val rangeSides = rangeText.split('-')
        if (rangeSides.size != 2) {
            interaction.respondPublic {
                content = "lethal range, must be of the format 'n-m' for 0<n≤m<12"
            }
            return
        }

        val rangeStart = rangeSides[0].toInt()
        val rangeEnd = rangeSides[1].toInt()

        if (rangeStart >= rangeEnd
            || rangeStart < 1
            || rangeEnd > 12) {
            interaction.respondPublic {
                content = "lethal range, must be of the format 'n-m' for 0<n≤m<12"
            }
            return
        }

        val result = Random.nextInt(1, 13)
        val secondLine = "*Lethal Range: ${rangeStart}-${rangeEnd}*"

        val damageType = if (result in rangeStart..rangeEnd) "LP" else "HP"
        val firstLine = "**$result $damageType Damage**"
        interaction.respondPublic {
            content = "$firstLine\n$secondLine"
        }
    }

}