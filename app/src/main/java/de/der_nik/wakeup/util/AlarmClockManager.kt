package de.der_nik.wakeup.util

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.util.Log
import com.github.debop.javatimes.toDateTime
import de.der_nik.wakeup.AlarmReceiver
import de.der_nik.wakeup.model.AlarmTime
import java.util.*

class AlarmClockManager {

    private var ringtone: Ringtone? = null

    fun activateAlarm(context: Context, alarm: LiveData<AlarmTime>): Boolean {
        if(alarm.value == null) {
            return false
        }
        val now = Date()
        var dateToSet = Date(alarm.value!!.date)
        if(dateToSet <= now){
            var hour = dateToSet.hours
            var min = dateToSet.minutes
            alarm.value!!.date = setDate(hour, min)
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent(context, alarm)
        Log.d("AlarmDate", "Alarm aktiviert für: " + Date(alarm.value!!.date).toString())
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.value!!.date, pendingIntent)
        alarm.value!!.active = true
        return true
    }

    fun deactivateAlarm(context: Context, alarm: LiveData<AlarmTime>): Boolean {
        if(alarm.value == null) {
            return false
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = AlarmClockManager.getInstance().getPendingIntent(context, alarm)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
        alarm.value!!.active = false
        return true
    }

    private fun getPendingIntent(context: Context, alarm: LiveData<AlarmTime>): PendingIntent {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val id = alarm.value!!.id
        alarmIntent.putExtra("id", id)
        return PendingIntent.getBroadcast(context, id, alarmIntent, 0)
    }
    fun setDate(hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance() // locale-specific
        cal.time = Date()
        val curTime = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val timeToSet = cal.timeInMillis
        if (curTime > timeToSet){
            cal.add(Calendar.DATE, 1)
        }
        return cal.timeInMillis
    }


    fun getRingtone(context: Context): Ringtone {
        if (ringtone != null) {
            val tmpringtone = ringtone!!
            return tmpringtone
        }
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val tmpRingtone = RingtoneManager.getRingtone(context, uri)
        ringtone = tmpRingtone
        return tmpRingtone
    }

    fun startAlarm(context: Context){
        getRingtone(context).play()
    }

    fun stopAlarm(context: Context) {

        getRingtone(context).stop()
    }

    companion object {
        @Volatile
        private var INSTANCE: AlarmClockManager? = null

        fun getInstance(): AlarmClockManager {
            val tmpInstance = INSTANCE
            if(tmpInstance != null ) {
                return tmpInstance
            }

            val instance  = AlarmClockManager()
            INSTANCE = instance
            return instance
        }
    }
}