package com.example.taskapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.databinding.TaskLayoutBinding
import com.example.taskapp.entity.Task
import com.example.taskapp.fragments.HomeFragmentDirections

class TaskAdapter :RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    class TaskViewHolder(val itemBinding: TaskLayoutBinding): RecyclerView.ViewHolder(itemBinding.root)
    private val differCallback = object : DiffUtil.ItemCallback<Task>(){

        // Sirve como comparacion para cuando mis items son exactamente lo mismo
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
           return oldItem.Id == newItem.Id &&
                   oldItem.Description == newItem.Description &&
                   oldItem.Title == newItem.Title
        }
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
       return TaskViewHolder(
           TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = differ.currentList[position]

        holder.itemBinding.TaskTitle.text = currentTask.Title
        holder.itemBinding.TaskDesc.text = currentTask.Description

        holder.itemView.setOnClickListener{
        val direction = HomeFragmentDirections.actionHomeFragmentToEditTaskFragment(currentTask)
            it.findNavController().navigate(direction)
        }
    }
}