package de.der_nik.wakeup.ui.editaddalarm

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
import java.util.*
import kotlin.coroutines.CoroutineContext

class EditAddAlarmViewModel(application: Application) : AndroidViewModel(application) {

    val alarmLD: MutableLiveData<AlarmTime> by lazy { MutableLiveData<AlarmTime>() }

    private val repository: WakeUpRepository
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private var id = 0;


    init {
        val wakeUpDao =WakeUpRoomDatabase.getDatabase(application).wakeUpDao()
        repository = WakeUpRepository(wakeUpDao)
    }

    fun createActivateAndSafeAlarm(setDate:  Long, info: String): Boolean {
        val newAlarm = AlarmTime(setDate, info, false)
        if(this.id != 0) {
            newAlarm.id = this.id
        }
        if(alarmLD.value == null) {
        }
        alarmLD.setValue(newAlarm)
        if (AlarmClockManager.getInstance().activateAlarm(getApplication(), alarmLD)) {
            save()
            return true
        }
        return false
    }

    fun save() = scope.launch(Dispatchers.IO) {
        repository.insert(alarmLD.value!!)
    }

    fun handleReceivedID (id: Int)  {
        if(id != 0) {
            this.id = id;
            loadAlarm(id)
        }
    }

    fun loadAlarm(id: Int)  = scope.launch(Dispatchers.IO) {
        alarmLD.postValue(repository.getAlarmByID(id))
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
