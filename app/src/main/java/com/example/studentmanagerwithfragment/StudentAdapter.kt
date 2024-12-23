package com.example.studentmanagerwithfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class StudentAdapter(private val context: Context, private var students: MutableList<Student>) : BaseAdapter() {

    override fun getCount(): Int = students.size

    override fun getItem(position: Int): Any = students[position]

    override fun getItemId(position: Int): Long = students[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_student, parent, false)
        val student = students[position]
        view.findViewById<TextView>(R.id.student_name).text = student.name
        view.findViewById<TextView>(R.id.student_id).text = student.studentId
        return view
    }

    fun updateStudents(newStudents: List<Student>) {
        students.clear()
        students.addAll(newStudents)
        notifyDataSetChanged()
    }

    fun remove(student: Student) {
        students.remove(student)
        notifyDataSetChanged()
    }
}