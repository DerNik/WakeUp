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

    fun createActivateAndSafeAlarm(hour: Int, minute: Int, mo: Boolean, di: Boolean,
                                   mi: Boolean, don: Boolean, fr: Boolean, sa: Boolean, so: Boolean, info: String): Boolean {
        val newAlarm = AlarmTime(0, info, false)
        newAlarm.mo = mo
        newAlarm.di = di
        newAlarm.mi = mi
        newAlarm.don = don
        newAlarm.fr = fr
        newAlarm.sa = sa
        newAlarm.so = so

        val date = AlarmClockManager.getInstance().setDate(hour, minute, newAlarm)

        newAlarm.date = date

        if(this.id != 0) {
            newAlarm.id = this.id
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
