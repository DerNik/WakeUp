package de.der_nik.wakeup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.der_nik.wakeup.ui.alarmclock.AlarmClockActivity
import de.der_nik.wakeup.util.AlarmClockManager

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        AlarmClockManager.getInstance().startAlarm(context!!)
        val activityIntent = Intent(context, AlarmClockActivity::class.java)
        activityIntent.putExtra("id", intent?.getIntExtra("id", 0))
        context.startActivity(activityIntent)
    }
}