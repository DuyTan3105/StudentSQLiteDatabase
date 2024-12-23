package com.example.studentmanagerwithfragment

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
    private lateinit var studentDao: StudentDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentId = it.getInt("studentId", -1)
        }
        val db = StudentDatabase.getDatabase(requireContext())
        studentDao = db.studentDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_and_edit, container, false)
        val titleTextView = view.findViewById<TextView>(R.id.textView_title)
        val nameEditText = view.findViewById<EditText>(R.id.edit_hoten)
        val studentIdEditText = view.findViewById<EditText>(R.id.edit_mssv)
        val submitButton = view.findViewById<Button>(R.id.button_submit)
        val cancelButton = view.findViewById<Button>(R.id.button_cancle)

        if (studentId != -1) {
            lifecycleScope.launch {
                val student = studentDao.getStudentById(studentId)
                student?.let {
                    titleTextView.text = "CHỈNH SỬA THÔNG TIN SINH VIÊN"
                    nameEditText.setText(it.name)
                    studentIdEditText.setText(it.studentId)
                }
            }
        }

        cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_addAndEditFragment_to_mainFragment)
        }

        submitButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val studentId = studentIdEditText.text.toString().trim()
            if (name.isEmpty() || studentId.isEmpty()) {
                Toast.makeText(requireActivity(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show()
            } else {
                lifecycleScope.launch {
                    if (studentId.toInt() == -1) {
                        studentDao.insert(Student(name = name, studentId = studentId))
                    } else {
                        studentDao.update(Student(id = studentId.toInt(), name = name, studentId = studentId))
                    }
                    findNavController().navigate(R.id.action_addAndEditFragment_to_mainFragment)
                }
            }
        }

        return view
    }
}