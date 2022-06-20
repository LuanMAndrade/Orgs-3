package br.com.alura.orgs.database.dao

import androidx.room.*
import br.com.alura.orgs.model.Produto

@Dao

interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun search(): List<Produto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(produto: Produto)

    @Delete
    fun delete(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id= :id")
    fun searchById(id: Long): Produto?
}