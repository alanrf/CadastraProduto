package com.alanrf.cadastraproduto

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.View
import com.alanrf.cadastraproduto.MainActivity.Companion.meusProdutosArrayList
import com.alanrf.cadastraproduto.MainActivity.Companion.produtoDao
import com.alanrf.cadastraproduto.db.entity.Produto
import kotlinx.android.synthetic.main.content_cadastro.*
import java.text.SimpleDateFormat
import java.util.*

class CadastroActivity : AppCompatActivity() {

    private val myFormat = "dd/MM/yyyy"
    private val sdf = SimpleDateFormat(myFormat)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        defineComportamentoDataValidade()
    }

    private fun defineComportamentoDataValidade() {
        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val str = sdf.format(cal.time)
            edt_data_validade.editText?.setText(str)

        }

        edt_data_validade.editText?.inputType = InputType.TYPE_NULL;
        edt_data_validade.editText?.showSoftInputOnFocus = false;
        edt_data_validade.editText?.setOnClickListener {
            DatePickerDialog(this@CadastroActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun gravar(view: View) {
        val nomeProduto = edt_nome_produto.editText?.text.toString()
        val descricaoProduto = edt_descricao_produto.editText?.text.toString()
        val quantidadeProduto = edt_quantidade_produto.editText?.text.toString().toInt()
        val dataValidade = sdf.parse(edt_data_validade.editText?.text.toString());

        val produto = Produto(nome = nomeProduto, descricao = descricaoProduto, quantidade = quantidadeProduto, validade = dataValidade)

        produtoDao.inserir(produto)

        meusProdutosArrayList.add(produto)

        finish()
    }
}
