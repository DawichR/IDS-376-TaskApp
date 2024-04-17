package com.example.taskapp.fragments

import android.app.DatePickerDialog
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
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentAddTaskBinding
import com.example.taskapp.entity.Task
import com.example.taskapp.viewmodel.TaskViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddTaskFragment : Fragment(R.layout.fragment_add_task), MenuProvider {

    private  lateinit var firebaseAuth: FirebaseAuth
    private var addTaskBinding: FragmentAddTaskBinding? = null
    private val binding get() = addTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel

    private lateinit var addTaskView: View
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        addTaskBinding = FragmentAddTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        addTaskView = view
        binding.saveButton.setOnClickListener{
            saveNote(addTaskView)
        }
        binding.addTaskDueDate.setOnClickListener{showDatePickerDialog()}
    }

    private fun saveNote(view: View){
        val taskTitle = binding.addTaskTitle.text.toString().trim()
        val taskDescription = binding.addTaskDesc.text.toString().trim()
        val dueDate = binding.addTaskDueDate.text.toString();
        val isCompleted = false;
        val userId = firebaseAuth.currentUser!!.uid

        if(taskTitle.isNotEmpty()){
            val task = Task(0, taskTitle, taskDescription, isCompleted, dueDate, userId)
            taskViewModel.addTask(task)

            Toast.makeText(addTaskView.context, "Has agregado una tarea!", Toast.LENGTH_LONG).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        }else{
            Toast.makeText(addTaskView.context, "Favor agregar un titulo", Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_task, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu -> {
                saveNote(addTaskView)
                true
            }
            else-> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addTaskBinding = null
    }


    private fun showDatePickerDialog(){
            // Create a DatePickerDialog
            val datePickerDialog = DatePickerDialog(addTaskView.context, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    // Create a new Calendar instance to hold the selected date
                    val selectedDate = Calendar.getInstance()
                    // Set the selected date using the values received from the DatePicker dialog
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    // Format the selected date into a string
                    val formattedDate = dateFormat.format(selectedDate.time)
                    binding.addTaskDueDate.setText(formattedDate.toString());
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            // Show the DatePicker dialog
            datePickerDialog.show()
    }
}