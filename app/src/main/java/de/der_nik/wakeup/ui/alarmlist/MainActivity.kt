package de.der_nik.wakeup.ui.alarmlist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.der_nik.wakeup.R
import de.der_nik.wakeup.ui.editaddalarm.EditAddAlarmActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AlarmListFragment.newInstance())
                .commitNow()
        }

        fab.setOnClickListener {
            val intent = Intent( this@MainActivity, EditAddAlarmActivity::class.java)
            intent.putExtra("id", 0)
            startActivity(intent)
        }
    }
}
