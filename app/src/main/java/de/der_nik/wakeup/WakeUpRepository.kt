package de.der_nik.wakeup

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.WorkerThread
import de.der_nik.wakeup.model.AlarmTime
import de.der_nik.wakeup.persistence.WakeUpDao

class WakeUpRepository(private val wakeUpDao: WakeUpDao) {
    private val allAlarms: LiveData<List<AlarmTime>> = wakeUpDao.getAllAlarms()
    private val medAlarms = MediatorLiveData<List<AlarmTime>>()

    init {
        medAlarms.addSource(allAlarms) {result: List<AlarmTime>? ->
            result?.let { medAlarms.value = sortAlarms(result) }
        }
    }

    private fun sortAlarms(list: List<AlarmTime>?): List<AlarmTime>? {
        return list?.sortedWith(compareByDescending(AlarmTime::active).thenBy(AlarmTime::date))
    }

    @WorkerThread
    fun insert(alarmTime: AlarmTime) {
        wakeUpDao.insert(alarmTime)
    }

    @WorkerThread
    fun getAlarmByID(id: Int): AlarmTime {
        return wakeUpDao.getAlarmByID(id)
    }

    fun getAlarms(): LiveData<List<AlarmTime>>{
        return medAlarms
    }
}