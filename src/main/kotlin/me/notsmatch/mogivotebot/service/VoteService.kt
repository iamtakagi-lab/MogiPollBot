package me.notsmatch.mogivotebot.service

import me.notsmatch.mogivotebot.Bot
import me.notsmatch.mogivotebot.model.Vote
import java.util.*

class VoteService {

    val votes: MutableList<Vote> = mutableListOf()

    init {
        runTask()
    }

    fun runTask() {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                for(i in votes.indices){
                    val vote = votes[i]
                    vote.apply {
                        if(users.size >= 6 && hasExpired()){
                            val guild = Bot.instance.jda.getGuildById(guildId)?:return
                            val channel = guild.getTextChannelById(channelId)?:return
                            channel.sendMessage(getResult()).queue()

                            votes.removeAt(i)
                        }
                    }
                }
            }
        }, 0, 10)
    }

    fun getVotesByGuild(guildId: Long) : List<Vote> {
        return votes.filter { vote -> vote.guildId == guildId }
    }

    fun getVote(guildId: Long, channelId: Long) : Vote? {
        return votes.find { vote -> vote.guildId == guildId && vote.channelId == channelId }
    }

    fun isVoting(guildId: Long, channelId: Long) : Boolean {
        return getVote(guildId, channelId) != null
    }
}