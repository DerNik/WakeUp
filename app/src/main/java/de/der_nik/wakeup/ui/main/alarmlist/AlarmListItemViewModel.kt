package de.der_nik.wakeup.ui.main.alarmlist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.util.Log
import android.widget.Toast
import de.der_nik.wakeup.WakeUpRepository
import de.der_nik.wakeup.model.AlarmTime
import de.der_nik.wakeup.persistence.WakeUpRoomDatabase
import de.der_nik.wakeup.ui.editaddalarm.EditAddAlarmActivity
import de.der_nik.wakeup.util.AlarmClockManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AlarmListItemViewModel(application: Application): AndroidViewModel(application){

    val alarmInfo = MutableLiveData<String>()
    val alarmDate = MutableLiveData<String>()
    val alarmActive = MutableLiveData<Boolean>()

    private val repository: WakeUpRepository
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        val wakeUpDao = WakeUpRoomDatabase.getDatabase(application).wakeUpDao()
        repository = WakeUpRepository(wakeUpDao)
    }

    fun onItemClick(alarm: AlarmTime){
        Log.d("AlarmItem", "Button Clicked: " + alarm.name)
        val context = getApplication<Application>()
        val intent = Intent( context, EditAddAlarmActivity::class.java)
        intent.putExtra("id", alarm.id)
        context.startActivity(intent)
    }

    fun ontoggleActiveSwitch(alarm: AlarmTime){
        val alarmLD = MutableLiveData<AlarmTime>()
        alarmLD.value = alarm
        if(!alarmLD.value!!.active){
            AlarmClockManager.getInstance().activateAlarm(getApplication(), alarmLD.value!!)
        } else {
            AlarmClockManager.getInstance().deactivateAlarm(getApplication(), alarmLD.value!!)
        }
        saveSingle(alarmLD)
    }

    fun saveSingle(alarm: LiveData<AlarmTime>) = scope.launch(Dispatchers.IO) {
        repository.insert(alarm.value!!)
    }
}