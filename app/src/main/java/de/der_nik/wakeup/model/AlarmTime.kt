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

    var repetitive = false
        get() {
        if(mo) return true
        if(di) return true
        if(mi) return true
        if(don) return true
        if(fr) return true
        if(sa) return true
        if(so) return true
        return false
    }

    var mo: Boolean = false
    var di: Boolean = false
    var mi: Boolean = false
    var don: Boolean = false
    var fr: Boolean = false
    var sa: Boolean = false
    var so: Boolean = false


}