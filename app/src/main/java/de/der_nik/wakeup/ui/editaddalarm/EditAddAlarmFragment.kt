package de.der_nik.wakeup.ui.editaddalarm

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.der_nik.wakeup.R
import de.der_nik.wakeup.model.AlarmTime
import de.der_nik.wakeup.util.AlarmClockManager
import kotlinx.android.synthetic.main.edit_add_alarm_fragment.*
import java.util.*

class EditAddAlarmFragment : Fragment() {

    companion object {
        fun newInstance(id: Int): EditAddAlarmFragment{
            val args = Bundle()
            args.putInt("id", id)
            val fragment =EditAddAlarmFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: EditAddAlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.edit_add_alarm_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditAddAlarmViewModel::class.java)
        viewModel.handleReceivedID(arguments?.getInt("id") ?: 0)
        picker_time.setIs24HourView(true)
        val alarmObserver = Observer<AlarmTime> {alarm ->
            edit_name.setText(alarm?.name ?: "")
            val cal = Calendar.getInstance()
            cal.time = Date(alarm?.date ?: Date().time)
            val hours = cal.get(Calendar.HOUR_OF_DAY)
            val minutes = cal.get(Calendar.MINUTE)
            picker_time.currentHour = hours
            picker_time.currentMinute = minutes
        }
        viewModel.alarmLD.observe(this, alarmObserver)

        button_save.setOnClickListener{
            val setDate = AlarmClockManager.getInstance().setDate(picker_time.currentHour, picker_time.currentMinute)
            if(viewModel.createActivateAndSafeAlarm(setDate, edit_name.text.toString())) {
                activity?.finish()
            } else {
                // error handling
            }
        }
    }
}
