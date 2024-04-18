package com.example.taskapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.adapter.TaskAdapter
import com.example.taskapp.databinding.FragmentCompletedTaskBinding
import com.example.taskapp.entity.Task
import com.example.taskapp.viewmodel.TaskViewModel
import com.google.firebase.auth.FirebaseAuth

class CompletedTaskFragment : Fragment(R.layout.fragment_completed_task) {
    private lateinit var binding: FragmentCompletedTaskBinding

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompletedTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = (activity as MainActivity).taskViewModel
        setupHomeRecyclerView()
    }
    private fun updateUI(task: List<Task>?){
        if(task != null){
            if(task.isNotEmpty()){
                binding.emptyNotesImage.visibility = View.GONE
                binding.completedRecyclerView.visibility = View.VISIBLE
            }else{
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.completedRecyclerView.visibility = View.GONE
            }
        }
    }
    private fun setupHomeRecyclerView() {
        taskAdapter = TaskAdapter()
        binding.completedRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = taskAdapter
        }

        activity?.let {
            taskViewModel.getAllCompletedTasks(userId = firebaseAuth.currentUser!!.uid).observe(viewLifecycleOwner){ task ->
                taskAdapter.differ.submitList(task)
                updateUI(task)
            }
        }
    }

}