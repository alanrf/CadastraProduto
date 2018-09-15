package com.alanrf.cadastraproduto

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alanrf.cadastraproduto.MainActivity.Companion.meusProdutosArrayList
import com.alanrf.cadastraproduto.MainActivity.Companion.produtoDao
import com.alanrf.cadastraproduto.db.entity.Produto
import kotlinx.android.synthetic.main.content_cadastro.*

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
    }

    fun gravar(view: View) {
        val nomeProduto = edt_nome_produto.editText?.text.toString()
        val descricaoProduto = edt_descricao_produto.editText?.text.toString()
        val quantidadeProduto = edt_quantidade_produto.editText?.text.toString().toInt()

        val produto = Produto(nome = nomeProduto, descricao = descricaoProduto, quantidade = quantidadeProduto)

        produtoDao.inserir(produto)

        meusProdutosArrayList.add(produto)

        finish()
    }
}
