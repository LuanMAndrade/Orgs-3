package br.com.alura.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.alura.orgs.database.converter.Converter
import br.com.alura.orgs.database.dao.ProdutoDao
import br.com.alura.orgs.model.Produto


@Database(entities = [Produto::class], version = 1)
@TypeConverters(Converter::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao() : ProdutoDao

    companion object {
        fun instance(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java,"Orgs.db").build()
        }
    }
}