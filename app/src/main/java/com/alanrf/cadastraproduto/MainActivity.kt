package com.alanrf.cadastraproduto

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.SimpleAdapter
import com.alanrf.cadastraproduto.db.BancoDados
import com.alanrf.cadastraproduto.db.MIGRATION_1_2
import com.alanrf.cadastraproduto.db.dao.ProdutoDao
import com.alanrf.cadastraproduto.db.entity.Produto
import com.alanrf.cadastraproduto.swipehelper.SwipeToDeleteCallback
import com.alanrf.cadastraproduto.swipehelper.SwipeToEditCallback

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.lista_produto_item.view.*

class MainActivity : AppCompatActivity() {

    companion object {
        internal val nomeBancoDados: String = "nomebancodedados"
        internal lateinit var produtoDao: ProdutoDao
        internal lateinit var meusProdutosArrayList: ArrayList<Produto>
        internal lateinit var produtoAdapter: ProdutoListaAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        configuraBanco()
        configurarComportamentoListaRecyclerView(meusProdutosArrayList)

        fab.setOnClickListener { _ ->
            val produtoCadastro = Intent(this, CadastroActivity::class.java)
            startActivity(produtoCadastro)
        }
    }

    override fun onResume() {
        super.onResume()
        produtoAdapter.substituirTodosProdutos(produtoDao.selecionarTodos())
    }

    private fun configuraBanco() {
        val db = Room.databaseBuilder(this,
                BancoDados::class.java,
                Companion.nomeBancoDados).addMigrations(MIGRATION_1_2).allowMainThreadQueries().build()

        produtoDao = db.produtoDao()
        meusProdutosArrayList = carregarProdutos()
    }


    private fun configurarComportamentoListaRecyclerView(meusProdutosArrayList: ArrayList<Produto>) {
        rcv_lista_produtos.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        produtoAdapter = ProdutoListaAdapter(meusProdutosArrayList, context = this)
        rcv_lista_produtos.adapter = produtoAdapter

        val itemTouchHelperDelete = ItemTouchHelper(
                object : SwipeToDeleteCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        var posicao = viewHolder.adapterPosition
                        val prod = meusProdutosArrayList.get(posicao)
                        produtoDao.remover(prod)
                        meusProdutosArrayList.removeAt(posicao)
                        produtoAdapter.notifyItemRemoved(posicao)
                    }
                }
        )
        itemTouchHelperDelete.attachToRecyclerView(rcv_lista_produtos)

        val produtoCadastroIntent = Intent(this, CadastroActivity::class.java)
        val itemTouchHelperEdit = ItemTouchHelper(
                object : SwipeToEditCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        var posicao = viewHolder.adapterPosition
                        val prod = meusProdutosArrayList.get(posicao)
//                        produtoCadastroIntent.putExtra("selectedIndex", posicao)
                        produtoCadastroIntent.putExtra("produto", prod)

                        startActivity(produtoCadastroIntent)
                    }
                }
        )
        itemTouchHelperEdit.attachToRecyclerView(rcv_lista_produtos)
    }

    private fun carregarProdutos(): ArrayList<Produto> {
        return produtoDao.selecionarTodos() as ArrayList<Produto>
    }

}
