package com.example.mynetworkingkotlin.helper

import com.example.myapplication.model.Player
import io.paperdb.Paper

class LocalDatabase {

    fun storePlayer(player: Player){
        Paper.book().write<Player>("player", player)
    }
    fun loadPlayer(): Player {
        return Paper.book().read<Player>("player")
    }

    fun deletePlayer(){
        Paper.book().delete("player")
    }
    fun storePlayerList(players: List<Player>){
        Paper.book().write<List<Player>>("players", players)
    }


    fun loadPlayerList(): List<Player>{
        var players: List<Player>? = Paper.book().read<List<Player>>("players")
        if (players == null) players = ArrayList<Player>()
        return players
    }

    fun deletePlayerList(){
        Paper.book().delete("player")
    }
}