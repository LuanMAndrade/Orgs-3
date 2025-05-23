package br.com.alura.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityFormularioProdutoBinding
import br.com.alura.orgs.extensions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.ui.dialog.FormularioImagemDialog
import kotlinx.coroutines.*
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }
    private var url: String? = null
    private var idProduto = 0L
    private val produtoDao by lazy { AppDatabase.instance(this).produtoDao() }
    private val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar produto"
        configuraBotaoSalvar()
        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this)
                .mostra(url) { imagem ->
                    url = imagem
                    binding.activityFormularioProdutoImagem.tentaCarregarImagem(url)
                }
        }
        idProduto = intent.getLongExtra("ID", 0L)
        if (idProduto != 0L) {
            title = "Alterar Produto"
            val job1 = MainScope().launch(job) {
                val produtoCarregado = withContext(Dispatchers.IO){
                produtoDao.searchById(idProduto)}
                produtoCarregado?.let {
                    url = produtoCarregado.imagem
                    with(binding) {
                        activityFormularioProdutoNome.setText(produtoCarregado.nome)
                        activityFormularioProdutoDescricao.setText(produtoCarregado.descricao)
                        activityFormularioProdutoValor.setText(produtoCarregado.valor.toPlainString())
                        activityFormularioProdutoImagem.tentaCarregarImagem(produtoCarregado.imagem)
                    }
                } ?: finish()
            }
            job1.cancel()
        }

    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar
        botaoSalvar.setOnClickListener {
            val produtoNovo = criaProduto()
            produtoDao.insert(produtoNovo)
            finish()
        }
    }

    private fun criaProduto(): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString()
        val campoDescricao = binding.activityFormularioProdutoDescricao
        val descricao = campoDescricao.text.toString()
        val campoValor = binding.activityFormularioProdutoValor
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        return Produto(
            id = idProduto,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )
    }

}