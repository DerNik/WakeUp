package de.der_nik.wakeup.ui.editaddalarm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.der_nik.wakeup.R

class EditAddAlarmActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_add_alarm_activity)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            val id = intent.getIntExtra("id", 0)
            val editAddAlarmFragment = EditAddAlarmFragment.newInstance(id)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, editAddAlarmFragment)
                .commitNow()


        }
    }
}
