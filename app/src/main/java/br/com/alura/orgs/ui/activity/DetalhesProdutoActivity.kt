package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityDetalhesProdutoBinding
import br.com.alura.orgs.extensions.formataParaMoedaBrasileira
import br.com.alura.orgs.extensions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto
import kotlinx.coroutines.*

class DetalhesProdutoActivity : AppCompatActivity() {

    private var id: Long = 0L
    private var produto: Produto? = null
    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy { AppDatabase.instance(this).produtoDao() }
    private val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tentaCarregarProduto()
    }

    override fun onResume() {
        super.onResume()
        val job1 = MainScope().launch(job) {
            withContext(Dispatchers.IO){
            produto = produtoDao.searchById(id)}
            produto?.let {
                preencheCampos(it)
            } ?: finish()
        }
        job1.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_detalhes_produto_editar -> {
                val intent = Intent(this, FormularioProdutoActivity::class.java)
                    .apply {
                        putExtra("ID", produto?.id)
                    }
                startActivity(intent)
            }

            R.id.menu_detalhes_produto_apagar -> {
                produto?.let {
                    produtoDao.delete(it)
                }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun tentaCarregarProduto() {
        id = intent.getLongExtra("ID", 0L)

    }

    private fun preencheCampos(produtoCarregado: Produto) {
        with(binding) {
            activityDetalhesProdutoImagem.tentaCarregarImagem(produtoCarregado.imagem)
            activityDetalhesProdutoNome.text = produtoCarregado.nome
            activityDetalhesProdutoDescricao.text = produtoCarregado.descricao
            activityDetalhesProdutoValor.text =
                produtoCarregado.valor.formataParaMoedaBrasileira()
        }
    }

}