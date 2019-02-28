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

            day_of_week_mo.isChecked = alarm?.mo ?: false
            day_of_week_di.isChecked = alarm?.di ?: false
            day_of_week_mi.isChecked = alarm?.mi ?: false
            day_of_week_do.isChecked = alarm?.don ?: false
            day_of_week_fr.isChecked = alarm?.fr ?: false
            day_of_week_sa.isChecked = alarm?.sa ?: false
            day_of_week_so.isChecked = alarm?.so ?: false
        }
        viewModel.alarmLD.observe(this, alarmObserver)

        button_save.setOnClickListener{
            if(viewModel.createActivateAndSafeAlarm(
                    picker_time.currentHour,
                    picker_time.currentMinute,
                    day_of_week_mo.isChecked,
                    day_of_week_di.isChecked,
                    day_of_week_mi.isChecked,
                    day_of_week_do.isChecked,
                    day_of_week_fr.isChecked,
                    day_of_week_sa.isChecked,
                    day_of_week_so.isChecked,
                    edit_name.text.toString())) {
                activity?.finish()
            } else {
                // error handling
            }
        }
    }
}
