package com.alanrf.cadastraproduto

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.alanrf.cadastraproduto.db.BancoDados
import com.alanrf.cadastraproduto.db.dao.ProdutoDao
import com.alanrf.cadastraproduto.db.entity.Produto

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
                Companion.nomeBancoDados).allowMainThreadQueries().build()

        produtoDao = db.produtoDao()
        meusProdutosArrayList = carregarProdutos()
    }


    private fun configurarComportamentoListaRecyclerView(meusProdutosArrayList: ArrayList<Produto>) {

        rcv_lista_produtos.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        produtoAdapter = ProdutoListaAdapter(meusProdutosArrayList, context = this)

        rcv_lista_produtos.adapter = produtoAdapter

        val produtoCadastro = Intent(this, CadastroActivity::class.java)

        val helper = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        //Delete
                        if (ItemTouchHelper.LEFT == direction) {
                            var nomeProduto: String = viewHolder.itemView.lbProduto.text.toString()
                            var idProduto = nomeProduto.substring(0, nomeProduto.indexOf('-'))

                            val prod = produtoDao.selecionarProduto(Integer.parseInt(idProduto))
                            produtoDao.remover(prod)

                            var posicao = viewHolder.adapterPosition
                            meusProdutosArrayList.removeAt(posicao)
                            produtoAdapter.notifyItemRemoved(posicao)

                            return;
                        }

                        if (ItemTouchHelper.RIGHT == direction) {
                            var nomeProduto: String = viewHolder.itemView.lbProduto.text.toString()
                            var idProduto = nomeProduto.substring(0, nomeProduto.indexOf('-'))

                            startActivity(produtoCadastro)
                            return;
                        }
                    }
                }
        )

        helper.attachToRecyclerView(rcv_lista_produtos)
    }

    private fun carregarProdutos(): ArrayList<Produto> {
        return produtoDao.selecionarTodos() as ArrayList<Produto>
    }

}
