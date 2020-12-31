package com.example.madlevel5task2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


//todo lees abstract class door
@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GameRoomDatabase: RoomDatabase() {

    abstract fun gameDao(): GameDao

    //static
    companion object{
        private const val DATABASE_NAME = "GAME_DATABASE"

        @Volatile
        private var gameReleaseRoomDatabaseInstance: GameRoomDatabase? = null

        fun getDatabase(context: Context): GameRoomDatabase?{
            if (gameReleaseRoomDatabaseInstance == null){
                synchronized(GameRoomDatabase::class.java){
                    if (gameReleaseRoomDatabaseInstance == null){
                        gameReleaseRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            GameRoomDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return gameReleaseRoomDatabaseInstance
        }
    }
}