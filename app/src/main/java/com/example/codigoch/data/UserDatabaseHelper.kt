package com.example.codigoch.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "CodigoCH.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "usuarios"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_IS_LOGGED_IN = "is_logged_in"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT UNIQUE,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_IS_LOGGED_IN INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }
    fun registerUser(email: String, password: String): Boolean {
        val db = writableDatabase

        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null, null, null,
        )

        val exists = cursor.moveToFirst()
        cursor.close()

        if (exists) {
            return false
        }

        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_IS_LOGGED_IN, 0)
        }

        val id = db.insert(TABLE_USERS, null, values)
        return id != -1L
    }
    fun login(email: String, password: String): Boolean {
        val db = writableDatabase

        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_EMAIL),
            "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password),
            null, null, null,
        )

        val userFound = cursor.moveToFirst()
        cursor.close()

        if (!userFound) {
            return false
        }

        logoutAll()

        val values = ContentValues().apply {
            put(COLUMN_IS_LOGGED_IN, 1)
        }

        val updated = db.update(TABLE_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email))
        return updated > 0
    }

    fun getActiveSessionUser(): String? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_EMAIL),
            "$COLUMN_IS_LOGGED_IN = ?",
            arrayOf("1"),
            null, null, null,
        )

        var email: String? = null
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        }
        cursor.close()
        return email
    }

    fun logoutAll() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IS_LOGGED_IN, 0)
        }
        db.update(TABLE_USERS, values, null, null)
    }
}
