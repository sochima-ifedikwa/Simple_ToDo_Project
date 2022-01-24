package com.example.todoapplication_project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. Remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter that our data has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        // 1. Let's detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            // Code in her is going to be executed when the user clicks on a button
//            Log.i("Sochi", "User clicked on button")
//        }

        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)


        //Set up the button and input field, so that the user can enter a task and add it to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Get a reference to the button
        // and then set an onclick listener
        findViewById<Button>(R.id.button).setOnClickListener {
            //1. Grab the text the user has inputted into @id/TaskField
            val userinputtedTask = inputTextField.text.toString()

            //2. Add the string to our list of Tasks: listOfTasks
            listOfTasks.add(userinputtedTask)

            //Notify the adapter thtath our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }
    // Save the data that the user has inputted
    // Save data by writing and reading from a file

    // Get the file we need
    fun getDataFile() : File{

        //Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }


    // Save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }

    }
}

