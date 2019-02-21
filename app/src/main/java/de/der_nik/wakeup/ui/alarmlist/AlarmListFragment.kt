package de.der_nik.wakeup.ui.alarmlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import de.der_nik.wakeup.ui.editaddalarm.EditAddAlarmActivity
import de.der_nik.wakeup.R
import de.der_nik.wakeup.model.AlarmTime
import kotlinx.android.synthetic.main.alarm_list_fragment.*

class AlarmListFragment : Fragment() {

    companion object {
        fun newInstance() = AlarmListFragment()
    }

    private lateinit var viewModel: AlarmListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.alarm_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AlarmListViewModel::class.java)

        val context = requireContext()
        val recyclerView: RecyclerView = alarm_list_rv
        val adapter = AlarmListAdapter(viewModel.AlarmListItemViewModel())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel.allAlarms.observe(this, Observer { alarms -> alarms?.let { adapter.replaceItems(viewModel.allAlarms) } })
    }
}
