package com.example.studentmanagerwithfragment

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): StudentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentDatabase::class.java,
                    "student_database"
                )
                    .addCallback(StudentDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class StudentDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.studentDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(studentDao: StudentDao) {
            // Xóa tất cả nội dung
            studentDao.deleteAll()

            // Thêm dữ liệu mẫu
            var student = Student(name = "Nguyen Van A", studentId = "123456")
            studentDao.insert(student)
            student = Student(name = "Tran Thi B", studentId = "654321")
            studentDao.insert(student)
        }
    }
}