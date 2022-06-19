package br.com.alura.orgs.database.dao

import androidx.room.*
import br.com.alura.orgs.model.Produto

@Dao

interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun search() : List<Produto>

    @Insert
    fun insert(produto: Produto)

    @Delete
    fun delete(produto: Produto)

    @Update
    fun update(produto: Produto)
}