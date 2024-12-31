package com.example.studentmanagerwithfragment

import DatabaseHelper
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val studentList = view.findViewById<ListView>(R.id.student_list)
        studentAdapter = StudentAdapter(requireContext(), mutableListOf())
        studentList.adapter = studentAdapter

        refreshStudentList()

        return view
    }

    private fun refreshStudentList() {
        val students = dbHelper.getAllStudents()
        studentAdapter.updateStudents(students)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val pos = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        val student = studentAdapter.getItem(pos) as Student

        when (item.itemId) {
            R.id.button_edit_student -> {
                val arguments = Bundle().apply {
                    putInt("studentId", student.id)
                }
                findNavController().navigate(R.id.action_mainFragment_to_addAndEditFragment, arguments)
            }
            R.id.button_delete_student -> {
                dbHelper.deleteStudent(student)
                refreshStudentList()
            }
        }
        return super.onContextItemSelected(item)
    }
}