package me.notsmatch.mogivotebot.comand

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.notsmatch.mogivotebot.model.Vote
import me.notsmatch.mogivotebot.service.VoteService
import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color
import java.util.*

class VoteComamnd(val voteService: VoteService) : Command() {

    init {
        this.name = "v"
        this.help = "形式投票を開始します"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {

            if (voteService.isVoting(guild.idLong, channel.idLong)) {
                return reply(EmbedBuilder().apply {
                    setColor(Color.RED)
                    setAuthor(
                        "Error",
                        null,
                        null
                    )
                    setDescription("このチャンネルでは、既に投票が進行中です")
                }.build())
            }


            voteService.votes.add(Vote(guild.idLong, channel.idLong, System.currentTimeMillis(), mutableMapOf()))

            reply(
                "**投票が開始されました**\n" +
                        "\n" +
                        "``1.`` FFA\n" +
                        "``2.`` 2v2\n" +
                        "``3.`` 3v3\n" +
                        "``4.`` 4v4\n" +
                        "``5.`` 6v6\n" +
                        "\n" +
                        "数字 または 形式 を入力して投票してください。\n" +
                "投票開始から2分経過及び、6票以上の状態で終了します。"
            )
        }
    }
}