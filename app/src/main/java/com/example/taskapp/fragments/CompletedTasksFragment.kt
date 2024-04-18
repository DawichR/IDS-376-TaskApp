package com.example.taskapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.adapter.TaskAdapter
import com.example.taskapp.databinding.FragmentCompletedTaskBinding
import com.example.taskapp.viewmodel.TaskViewModel
import com.google.firebase.auth.FirebaseAuth

class CompletedTasksFragment: Fragment(R.layout.fragment_completed_task) {

    private lateinit var completedTaskBinding : FragmentCompletedTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        completedTaskBinding = FragmentCompletedTaskBinding.inflate(inflater, container, false)
        return completedTaskBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskViewModel = (activity as MainActivity).taskViewModel
        firebaseAuth = FirebaseAuth.getInstance()

        setupHomeRecyclerView()

    }

    private fun setupHomeRecyclerView(){
        taskAdapter = TaskAdapter()
        completedTaskBinding.completedRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = taskAdapter
        }

        activity?.let {
            taskViewModel.getAllCompletedTasks(userId = firebaseAuth.currentUser!!.uid).observe(viewLifecycleOwner){ task ->
                taskAdapter.differ.submitList(task)
            }
        }
    }

}