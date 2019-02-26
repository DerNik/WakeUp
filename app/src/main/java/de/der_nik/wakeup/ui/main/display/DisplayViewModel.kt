package de.der_nik.wakeup.ui.main.display

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.CountDownTimer
import com.github.debop.javatimes.minus
import de.der_nik.wakeup.WakeUpRepository
import de.der_nik.wakeup.model.AlarmTime
import de.der_nik.wakeup.persistence.WakeUpRoomDatabase
import java.util.*

class DisplayViewModel(application: Application) : AndroidViewModel(application) {

    val allAlarms: LiveData<List<AlarmTime>>
    private val timeLeft = MutableLiveData<String>()
    val timer: LiveData<String> get() = timeLeft
    private val repository: WakeUpRepository
    private var countDown: CountDownTimer? = null

    init {
        val wakeUpDao = WakeUpRoomDatabase.getDatabase(application).wakeUpDao()
        repository = WakeUpRepository(wakeUpDao)
        allAlarms = repository.allAlarms
    }

    fun startCountdown() {
        if(allAlarms.value?.size ?: 0 > 0) {
            countDown?.cancel()
            val date = allAlarms.value!!.get(0).date
            val now = Date().time
            val off = date - now
            if(off > 0) {
                countDown = createCountdown(off)
                countDown?.start()
            }
        }
    }

    fun getAlarmTime(): String{
        val date = Date(allAlarms.value?.get(0)?.date ?: 0)
        val hour =  date.hours
        val min = date.minutes

        return "$hour:$min"
    }

    fun createCountdown(off: Long) :CountDownTimer{

        return object: CountDownTimer(off, 1000) {

            override fun onTick(millisLeft: Long) {
                val hoursLeft = millisLeft / 3600000
                val minutesLeft =  (millisLeft % 3600000) / 60000 +1
                timeLeft.value ="$hoursLeft:$minutesLeft"
            }

            override fun onFinish() {
                this.cancel()
            }
        }
    }
}
