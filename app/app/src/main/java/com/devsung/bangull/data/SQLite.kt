package com.devsung.bangull.data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLite(
    context: Context,
    name: String
) : SQLiteOpenHelper(context, name, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) { }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }

    fun getCustomer(phoneNumber: String): Customer? {
        val query = "SELECT * FROM `address` WHERE `cell_phone` = '${phoneNumber}' OR `home_phone` = '${phoneNumber}'"
        val cursor = readableDatabase.rawQuery(query, null).apply { moveToNext() }
        if (cursor.count == 0)
            return null
        return Customer(
            getString(cursor, "name"),
            getString(cursor, "cell_phone"),
            getString(cursor, "home_phone"),
            getString(cursor, "order"),
            getString(cursor, "address"),
            getString(cursor, "time")
        ).also { cursor.close() }
    }

    private fun getString(cursor: Cursor, string: String) =
        cursor.getString(cursor.getColumnIndex(string))
}