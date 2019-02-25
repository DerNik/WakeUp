package de.der_nik.wakeup

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import de.der_nik.wakeup.model.AlarmTime
import de.der_nik.wakeup.persistence.WakeUpDao

class WakeUpRepository(private val wakeUpDao: WakeUpDao) {
    val allAlarms: LiveData<List<AlarmTime>> = wakeUpDao.getAllAlarms()

    @WorkerThread
    fun insert(alarmTime: AlarmTime) {
        wakeUpDao.insert(alarmTime)
    }

    @WorkerThread
    fun getAlarmByID(id: Int): AlarmTime {
        return wakeUpDao.getAlarmByID(id)
    }
}