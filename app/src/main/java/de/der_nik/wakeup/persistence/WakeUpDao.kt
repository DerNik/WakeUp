package de.der_nik.wakeup.persistence

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import de.der_nik.wakeup.model.AlarmTime

@Dao
interface WakeUpDao {

    @Query("SELECT * FROM alarm_table")
    fun getAllAlarms(): LiveData<List<AlarmTime>>

    @Query("SELECT * FROM alarm_table WHERE id LIKE :uuid")
    fun getAlarmByID(uuid: String): AlarmTime

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(alarm: AlarmTime)

}