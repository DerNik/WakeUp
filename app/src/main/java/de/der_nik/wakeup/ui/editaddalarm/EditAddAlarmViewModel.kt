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
    var uuid: String = ""

    private val repository: WakeUpRepository
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    init {
        val wakeUpDao =WakeUpRoomDatabase.getDatabase(application).wakeUpDao()
        repository = WakeUpRepository(wakeUpDao)
    }

    fun createActivateAndSafeAlarm(setDate:  Long, info: String): Boolean {
        val newAlarm = AlarmTime(uuid, setDate, info, false)
        alarmLD.setValue(newAlarm)
        if (AlarmClockManager.getInstance().activateIntent(getApplication(), alarmLD)) {
            save()
            return true
        }
        return false
    }

    fun save() = scope.launch(Dispatchers.IO) {
        repository.insert(alarmLD.value!!)
    }

    fun handleReceivedUUID (uuid: String?)  {
        if(!uuid.isNullOrEmpty()) {
            this.uuid = uuid
            loadAlarm(uuid)
        } else {
            this.uuid = UUID.randomUUID().toString()
        }
    }

    fun setDate(hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance() // locale-specific
        cal.time = Date()
        val curTime = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val setTime = cal.timeInMillis
        if (curTime > setTime){
            cal.add(Calendar.DATE, 1)
        }
        return cal.timeInMillis
    }

    fun loadAlarm(uuid: String)  = scope.launch(Dispatchers.IO) {
        alarmLD.postValue(repository.getAlarmByID(uuid))
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
        uuid = ""
    }
}
