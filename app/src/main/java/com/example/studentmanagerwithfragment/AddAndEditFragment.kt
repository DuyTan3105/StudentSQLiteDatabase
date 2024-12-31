package com.example.studentmanagerwithfragment

import DatabaseHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

class AddAndEditFragment : Fragment() {
    private var studentId: Int = -1
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentId = it.getInt("studentId", -1)
        }
        dbHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_and_edit, container, false)

        // Populate UI for editing
        if (studentId != -1) {
            val student = dbHelper.getStudentById(studentId)
            student?.let {
                view.findViewById<TextView>(R.id.textView_title).text = "CHỈNH SỬA THÔNG TIN SINH VIÊN"
                view.findViewById<EditText>(R.id.edit_hoten).setText(it.name)
                view.findViewById<EditText>(R.id.edit_mssv).setText(it.studentId)
            }
        }

        // Handle submit
        view.findViewById<Button>(R.id.button_submit).setOnClickListener {
            val name = view.findViewById<EditText>(R.id.edit_hoten).text.toString()
            val studentId = view.findViewById<EditText>(R.id.edit_mssv).text.toString()

            if (name.isEmpty() || studentId.isEmpty()) {
                Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show()
            } else {
                val student = Student(id = this.studentId, name = name, studentId = studentId)
                if (this.studentId == -1) {
                    dbHelper.insertStudent(student)
                } else {
                    dbHelper.updateStudent(student)
                }
                findNavController().navigate(R.id.action_addAndEditFragment_to_mainFragment)
            }
        }

        return view
    }
}