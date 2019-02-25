package de.der_nik.wakeup.ui.alarmclock

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import de.der_nik.wakeup.WakeUpRepository
import de.der_nik.wakeup.model.AlarmTime
import de.der_nik.wakeup.persistence.WakeUpRoomDatabase
import de.der_nik.wakeup.util.AlarmClockManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AlarmClockViewModel (application: Application) : AndroidViewModel(application) {
    val alarm: MutableLiveData<AlarmTime> by lazy { MutableLiveData<AlarmTime>() }

    private val repository: WakeUpRepository
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    init {
        val wakeUpDao = WakeUpRoomDatabase.getDatabase(application).wakeUpDao()
        repository = WakeUpRepository(wakeUpDao)
    }

    fun loadAlarm(id: Int)  = scope.launch(Dispatchers.IO) {
        alarm.postValue(repository.getAlarmByID(id))
    }

    fun stopAlarm(): Boolean {
        val context = getApplication<Application>()
        AlarmClockManager.getInstance().stopAlarm(context)
        return AlarmClockManager.getInstance().deactivateAlarm(context,alarm)
    }

    fun getName(): String{
        return alarm.value?.name ?: "kein Text"
    }
}
