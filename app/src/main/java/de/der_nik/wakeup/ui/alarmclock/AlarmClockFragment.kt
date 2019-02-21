package de.der_nik.wakeup.ui.alarmclock

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.der_nik.wakeup.R
import de.der_nik.wakeup.databinding.AlarmClockFragmentBinding
import kotlinx.android.synthetic.main.alarm_clock_fragment.*

class AlarmClockFragment : Fragment() {

    companion object {
        fun newInstance() = AlarmClockFragment()
    }

    private lateinit var viewModel: AlarmClockViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProviders.of(this).get(AlarmClockViewModel::class.java)
        viewModel.loadAlarm(activity?.intent?.getStringExtra("id") ?: "")
        val binding: AlarmClockFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.alarm_clock_fragment, container,false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_stop.setOnClickListener {
            if(viewModel.stopAlarm()){
                activity?.finish()
            }
        }
    }
}