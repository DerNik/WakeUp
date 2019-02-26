package de.der_nik.wakeup.ui.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.der_nik.wakeup.R
import de.der_nik.wakeup.ui.main.alarmlist.AlarmListFragment
import de.der_nik.wakeup.ui.editaddalarm.EditAddAlarmActivity
import de.der_nik.wakeup.ui.main.display.DisplayFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.timer, DisplayFragment.newInstance())
            .add(R.id.list, AlarmListFragment.newInstance())
                .commitNow()
        }

        fab.setOnClickListener {
            val intent = Intent( this@MainActivity, EditAddAlarmActivity::class.java)
            intent.putExtra("id", 0)
            startActivity(intent)
        }
    }
}
