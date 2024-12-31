import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.studentmanagerwithfragment.Student

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "student_database"
        private const val DATABASE_VERSION = 1
        private const val TABLE_STUDENTS = "students"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_STUDENT_ID = "student_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_STUDENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_STUDENT_ID TEXT
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    fun insertStudent(student: Student): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_STUDENT_ID, student.studentId)
        }
        return writableDatabase.insert(TABLE_STUDENTS, null, values)
    }

    fun updateStudent(student: Student): Int {
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_STUDENT_ID, student.studentId)
        }
        return writableDatabase.update(TABLE_STUDENTS, values,
            "$COLUMN_ID = ?", arrayOf(student.id.toString()))
    }

    fun deleteStudent(student: Student): Int {
        return writableDatabase.delete(TABLE_STUDENTS,
            "$COLUMN_ID = ?", arrayOf(student.id.toString()))
    }

    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()
        val cursor = readableDatabase.query(
            TABLE_STUDENTS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val studentId = getString(getColumnIndexOrThrow(COLUMN_STUDENT_ID))
                students.add(Student(id, name, studentId))
            }
        }
        cursor.close()
        return students
    }

    fun getStudentById(id: Int): Student? {
        val cursor = readableDatabase.query(
            TABLE_STUDENTS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                Student(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    studentId = it.getString(it.getColumnIndexOrThrow(COLUMN_STUDENT_ID))
                )
            } else null
        }
    }

    fun deleteAllStudents() {
        writableDatabase.delete(TABLE_STUDENTS, null, null)
    }
}