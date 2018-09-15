package com.alanrf.cadastraproduto.db.dao

import android.arch.persistence.room.*
import com.alanrf.cadastraproduto.db.entity.Produto

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM produto")
    fun selecionarTodos(): List<Produto>

    @Query("SELECT * FROM produto WHERE id = :idParam")
    fun selecionarProduto(idParam: Int): Produto

    @Insert
    fun inserir(vararg produto: Produto)

    @Delete
    fun remover(vararg produto: Produto)

}