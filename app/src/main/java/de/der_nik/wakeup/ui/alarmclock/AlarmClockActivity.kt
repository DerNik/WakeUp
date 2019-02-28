package de.der_nik.wakeup.ui.alarmclock

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.der_nik.wakeup.R

class AlarmClockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_clock_activity)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AlarmClockFragment.newInstance())
                .commitNow()
        }
    }

}
