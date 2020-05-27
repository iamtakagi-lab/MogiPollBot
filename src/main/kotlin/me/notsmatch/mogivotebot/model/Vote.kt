package me.notsmatch.mogivotebot.model

data class Vote(val guildId: Long, val channelId: Long, val startedAt: Long, val users: MutableMap<Long, Int>) {

    /**
     * 投票します
     */
    fun addOne(userId: Long, num: Int) {
        if(users.containsKey(userId)){
            users.replace(userId, num)
            return
        }
        users[userId] = num
    }

    /**
     * 2分経過か
     */
    fun hasExpired() : Boolean {
        return System.currentTimeMillis() >= startedAt + (1000 * 60 * 2)
    }

    /**
     * @param num
     * 投票数を返します
     */
    fun count(num: Int) : Int {
        return users.filter { entry -> entry.value == num }.size
    }

    /**
     * 投票数が一番多かった形式を返します
     */
    fun getWinner() : String {
        //形式, 投票数
        val map = mutableMapOf<Int, Int>().apply {
            put(1, 0)
            put(2, 0)
            put(3, 0)
            put(4, 0)
            put(5, 0)
        }

        users.values.forEach{
            map[it]!!.plus(1)
        }

        val list = map.toList().sortedByDescending { it.second }

        when (list[0].first) {
            1 -> {
                return "FFA"
            }
            2 -> {
                return "2v2"
            }
            3 -> {
                return "3v3"
            }
            4 -> {
                return "4v4"
            }
            5 -> {
                return "5v5"
            }
            else -> {
                return "None"
            }
        }
    }

    /**
     * 投票結果 送信用メッセージ
     */
    fun getResult() : String {
        return "**投票は終了しました**\n" +
                "\n" +
                "``1.`` FFA - **${count(1)}**\n" +
                "``2.`` 2v2 - **${count(2)}**\n" +
                "``3.`` 3v3 - **${count(3)}**\n" +
                "``4.`` 4v4 - **${count(4)}**\n" +
                "``5.`` 6v6 - **${count(5)}**\n" +
                "\n" +
                "``Winner`` - **${getWinner()}**"
    }
}