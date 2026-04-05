package com.choreroll.app.data.repository

import com.choreroll.app.data.db.dao.CategoryDao
import com.choreroll.app.data.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    fun getAll(): Flow<List<CategoryEntity>> = categoryDao.getAll()

    suspend fun getOrCreate(name: String): Long {
        val existing = categoryDao.getByName(name.trim())
        return existing?.id ?: categoryDao.insert(CategoryEntity(name = name.trim()))
    }

    suspend fun delete(category: CategoryEntity) = categoryDao.delete(category)
}
