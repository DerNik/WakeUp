package de.der_nik.wakeup.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import java.util.*

@Entity(tableName = "alarm_table")
data class AlarmTime(
    var date: Long,
    var name: String,
    var active: Boolean) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}