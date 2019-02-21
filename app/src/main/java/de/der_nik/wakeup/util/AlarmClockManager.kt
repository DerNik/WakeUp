package de.der_nik.wakeup.util

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import de.der_nik.wakeup.AlarmReceiver
import de.der_nik.wakeup.model.AlarmTime

class AlarmClockManager {

    private var ringtone: Ringtone? = null

    fun activateIntent(context: Context, alarm: LiveData<AlarmTime>): Boolean {
        if(alarm.value == null) {
            return false
        }
        alarm.value!!.active = true
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent(context, alarm)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.value!!.date, pendingIntent)
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
        alarmIntent.putExtra("id", alarm.value!!.id)
        val requestCode = alarm.value!!.date?.toInt()
        return PendingIntent.getBroadcast(context,requestCode, alarmIntent, 0)
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