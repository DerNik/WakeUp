package de.der_nik.wakeup.persistence

import android.arch.persistence.room.*
import android.content.Context
import de.der_nik.wakeup.model.AlarmTime

@Database(entities = [AlarmTime::class], version = 1)
abstract class WakeUpRoomDatabase: RoomDatabase() {

    abstract fun wakeUpDao(): WakeUpDao


    companion object {
        @Volatile
        private var INSTANCE: WakeUpRoomDatabase? = null

        fun getDatabase(context: Context): WakeUpRoomDatabase {
            val tmpInstance = INSTANCE
            if(tmpInstance != null ) {
                return tmpInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WakeUpRoomDatabase::class.java,
                    "wake_up_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}