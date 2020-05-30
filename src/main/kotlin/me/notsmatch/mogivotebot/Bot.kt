package me.notsmatch.mogivotebot

import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import me.notsmatch.mogivotebot.comand.AboutCommand
import me.notsmatch.mogivotebot.comand.GuildlistCommand
import me.notsmatch.mogivotebot.comand.VoteComamnd
import me.notsmatch.mogivotebot.service.VoteService
import me.notsmatch.mogivotebot.util.NumberUtils
import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import java.util.*


class Bot (private val token: String) {

    companion object {
        @JvmStatic
        lateinit var instance: Bot
    }

    lateinit var jda: JDA
    val voteService = VoteService()
    val eventWaiter = EventWaiter()

    fun start() {
        instance = this
        jda = JDABuilder(AccountType.BOT).setToken(token).setStatus(OnlineStatus.ONLINE).build()
        val builder = CommandClientBuilder()
        builder.addCommands(VoteComamnd(voteService),
            GuildlistCommand(eventWaiter),
            AboutCommand(Color.GREEN, "https://github.com/notsmatch/mogi-votebot", Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_READ, Permission.MESSAGE_ADD_REACTION))

        builder.setOwnerId("695218967173922866")
        builder.setPrefix("_")

        builder.setHelpWord("mvbot")

        val client = builder.build()
        jda.addEventListener(Listener())
        jda.addEventListener(client)
    }

    inner class Listener : ListenerAdapter() {

        override fun onReady(event: ReadyEvent) {
            event.jda.guilds.forEach { guild -> println(guild.name) }

            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    event.jda.apply {
                        presence.setPresence(
                            OnlineStatus.ONLINE,
                            Activity.watching("type _mvbotabout | ${guilds.size} servers")
                        )
                    }
                }
            }, 0, 1000 * 300)
        }

        override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
            event.apply {
                val vote = voteService.getVote(guild.idLong, channel.idLong)
                if (vote != null){
                    val msg = message.contentRaw
                    if(NumberUtils.isInteger(msg)){
                        val num = msg.toInt()
                        if(num in 1..5) {
                            vote.addOne(author.idLong, num)
                        }
                    }else{
                        when(msg.toLowerCase()){
                            "ffa" -> {
                                vote.addOne(author.idLong, 1)
                            }
                            "2v3" -> {
                                vote.addOne(author.idLong, 2)
                            }
                            "3v3" -> {
                                vote.addOne(author.idLong, 3)
                            }
                            "4v4" -> {
                                vote.addOne(author.idLong, 4)
                            }
                            "6v6" -> {
                                vote.addOne(author.idLong, 5)
                            }
                        }
                    }
                }
            }
        }
    }
}