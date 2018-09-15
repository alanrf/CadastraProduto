package com.alanrf.cadastraproduto.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.alanrf.cadastraproduto.db.dao.ProdutoDao
import com.alanrf.cadastraproduto.db.entity.Produto

@Database(entities = [Produto::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class BancoDados : RoomDatabase() {

    abstract fun produtoDao() : ProdutoDao
}