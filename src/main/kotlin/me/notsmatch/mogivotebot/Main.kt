package me.notsmatch.mogivotebot

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        Bot(System.getenv("MK8DXVOTEBOT_TOKEN")).start()
    }
}