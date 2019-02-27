package de.der_nik.wakeup.ui.main.display

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import de.der_nik.wakeup.R
import de.der_nik.wakeup.ui.main.MainSharedViewModel
import kotlinx.android.synthetic.main.display_fragment.*

class DisplayFragment : Fragment() {

    companion object {
        fun newInstance() = DisplayFragment()
    }

    private lateinit var viewModel: DisplayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.display_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DisplayViewModel::class.java)
        viewModel.startCountdown()
        viewModel.allAlarms.observe(this, Observer { alarms -> viewModel.startCountdown() })
        viewModel.timer.observe(this, Observer { countdown ->
            tv_alarm_time.text = viewModel.getAlarmTime()
            tv_alarm_to_go.text = countdown ?: ""})

    }

}
