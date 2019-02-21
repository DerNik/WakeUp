package de.der_nik.wakeup.ui.editaddalarm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.der_nik.wakeup.R

class EditAddAlarmActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_add_alarm_activity)
        if (savedInstanceState == null) {
            val editAddAlarmFragment = EditAddAlarmFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, editAddAlarmFragment)
                .commitNow()

            editAddAlarmFragment.setUuid(intent.getStringExtra("uuid"))
        }
    }
}
