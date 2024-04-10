package com.example.taskapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentEditTaskBinding
import com.example.taskapp.entity.Task
import com.example.taskapp.viewmodel.TaskViewModel


class EditTaskFragment :Fragment(R.layout.fragment_edit_task), MenuProvider {

    private var editTaskBinding: FragmentEditTaskBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel

    private lateinit var currentTask: Task
    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editTaskBinding = FragmentEditTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        binding.editTaskTitle.setText(currentTask.Title)
        binding.editTaskDesc.setText(currentTask.Description)

        binding.editTaskFab.setOnClickListener{
            val taskTitle = binding.editTaskTitle.text.toString().trim()
            val taskDesc = binding.editTaskDesc.text.toString().trim()

            if(taskTitle.isNotEmpty()){
                val task = Task(currentTask.Id, taskTitle, taskDesc)
                taskViewModel.updateTask(task)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }else{
                Toast.makeText(context, "Favor introducir un titulo", Toast.LENGTH_SHORT)
            }
        }
    }

    private fun deleteTask(){
        AlertDialog.Builder(activity).apply {
            setTitle("Eliminar tarea")
            setMessage("Quiere eliminar esta tarea?")
            setPositiveButton("Eliminar"){_,_ ->
                taskViewModel.deleteTask(currentTask)
                Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT)
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancelar", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_task, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteMenu ->{
                deleteTask()
                true
            }   else-> false
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        editTaskBinding = null
    }
}