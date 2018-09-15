package com.alanrf.cadastraproduto.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class Produto(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val nome: String,
        val descricao: String,
        val quantidade: Int,
        val validade: Date = Date(),
        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        val imagem: ByteArray? = null
)