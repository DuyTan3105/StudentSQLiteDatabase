package com.example.studentmanagerwithfragment

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
    private lateinit var studentDao: StudentDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = StudentDatabase.getDatabase(requireContext(), lifecycleScope)
        studentDao = db.studentDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val studentList = view.findViewById<ListView>(R.id.student_list)
        studentAdapter = StudentAdapter(requireContext(), mutableListOf())
        studentList.adapter = studentAdapter

        lifecycleScope.launch {
            val students = studentDao.getAllStudents()
            studentAdapter.updateStudents(students)
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.button_add_student -> {
                findNavController().navigate(R.id.action_mainFragment_to_addAndEditFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
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
                lifecycleScope.launch {
                    studentDao.delete(student)
                    studentAdapter.remove(student)
                }
            }
        }
        return super.onContextItemSelected(item)
    }
}