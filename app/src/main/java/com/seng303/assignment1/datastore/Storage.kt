package com.seng303.assignment1.datastore

import com.seng303.assignment1.data.Identifiable
import kotlinx.coroutines.flow.Flow

interface Storage<T> where T : Identifiable {
    fun insert(data: T): Flow<Int>
    fun insertAll(data: List<T>) : Flow<Int>
    fun getAll(): Flow<List<T>>
    fun get(where: (T) -> Boolean): Flow<T>
    fun delete(identifier: Int): Flow<Int>
    fun edit(identifier: Int, data: T): Flow<Int>
}