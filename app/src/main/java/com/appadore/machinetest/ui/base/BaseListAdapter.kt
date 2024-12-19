package com.appadore.machinetest.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import androidx.recyclerview.widget.RecyclerView
import com.appadore.machinetest.BR

abstract class BaseListAdapter : RecyclerView.Adapter<BaseListAdapter.BaseListHolder>() {

    inner class BaseListHolder(
        val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Any, listener: Any?, position: Int) {
            binding.setVariable(BR.item, item)
            if (listener != null) {
                binding.setVariable(BR.listener, listener)
            }
            //binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return BaseListHolder(
            DataBindingUtil.inflate(
                inflater,
                getLayoutIdForType(viewType),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseListHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(getDataAtPosition(position), getClickListener(), position)
    }

    abstract fun getLayoutIdForType(viewType: Int): Int

    abstract fun getDataAtPosition(position: Int): Any

    abstract fun getClickListener(): Any?
}