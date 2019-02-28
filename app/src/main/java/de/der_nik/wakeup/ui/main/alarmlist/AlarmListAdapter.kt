package de.der_nik.wakeup.ui.main.alarmlist

import android.arch.lifecycle.*
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.der_nik.wakeup.model.AlarmTime
import java.util.*

class AlarmListAdapter internal constructor(val viewModel: AlarmListItemViewModel): RecyclerView.Adapter<AlarmListAdapter.AlarmViewHolder>() {

    private var items: LiveData<List<AlarmTime>>? = null

    inner class AlarmViewHolder(val containerView: View, val binding: de.der_nik.wakeup.databinding.AlarmItemBinding): RecyclerView.ViewHolder(containerView), LifecycleOwner {

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.markState(Lifecycle.State.INITIALIZED)
        }

        fun markAttach() {
            lifecycleRegistry.markState(Lifecycle.State.STARTED)
        }

        fun markDetach() {
            lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = de.der_nik.wakeup.databinding.AlarmItemBinding.inflate(inflater, parent, false)
        val viewHolder = AlarmViewHolder(binding.root, binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val current = items!!.value!![position]

        holder.apply {

            viewModel.alarmDate.value = Date(current.date).toString()
            viewModel.alarmActive.value = current.active
            viewModel.alarmInfo.value = current.name

            containerView.setOnClickListener { viewModel.onItemClick(current) }
            binding.alarmListItemActiva.setOnClickListener { viewModel.ontoggleActiveSwitch(current) }
            binding.viewModel = viewModel

//            Example 2 Way DataBinding
//            viewModel.alarmInfo.observe(this, android.arch.lifecycle.Observer {
//                it?.also {
//                    current.name = it
//                }
//            })
        }
    }

    override fun getItemCount() = items?.value?.size ?: 0

    override fun onViewAttachedToWindow(holder: AlarmViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: AlarmViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }

    fun replaceItems(items: LiveData<List<AlarmTime>>) {
        this.items = items
        notifyDataSetChanged()
    }
}