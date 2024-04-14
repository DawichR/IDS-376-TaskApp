package com.example.taskapp.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentEditTaskBinding
import com.example.taskapp.entity.Task
import com.example.taskapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EditTaskFragment :Fragment(R.layout.fragment_edit_task), MenuProvider {

    private var editTaskBinding: FragmentEditTaskBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var editTaskView: View

    private lateinit var currentTask: Task
    private val args: EditTaskFragmentArgs by navArgs()
    private val calendar = Calendar.getInstance()


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
        editTaskView = view

        taskViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        binding.editTaskTitle.setText(currentTask.Title)
        binding.editTaskDesc.setText(currentTask.Description)
        binding.editTaskDueDate.setText(currentTask.DueDate)
        binding.editTaskDueDate.setOnClickListener{showDatePickerDialog()}
        binding.isCompleted.isChecked = currentTask.IsCompleted;

        binding.editTaskFab.setOnClickListener{
            val taskTitle = binding.editTaskTitle.text.toString().trim()
            val taskDesc = binding.editTaskDesc.text.toString().trim()
            val dueDate = binding.editTaskDueDate.text.toString();
            val isCompleted = binding.isCompleted.isChecked;

            if(taskTitle.isNotEmpty()){
                val task = Task(currentTask.Id, taskTitle, taskDesc, isCompleted, dueDate)
                taskViewModel.updateTask(task)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }else{
                Toast.makeText(context, "Favor introducir un titulo", Toast.LENGTH_SHORT)
            }
        }

        editTaskBinding!!.editTaskTitle.setOnEditorActionListener(OnEditorActionListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // hide virtual keyboard
                val imm =
                    view.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editTaskBinding!!.editTaskTitle.getWindowToken(), 0)
                return@OnEditorActionListener true
            }
            false
        })
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


    private fun showDatePickerDialog(){
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(editTaskView.context, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            // Create a new Calendar instance to hold the selected date
            val selectedDate = Calendar.getInstance()
            // Set the selected date using the values received from the DatePicker dialog
            selectedDate.set(year, monthOfYear, dayOfMonth)
            // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            // Format the selected date into a string
            val formattedDate = dateFormat.format(selectedDate.time)
            binding.editTaskDesc.setText(formattedDate.toString());
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }
}