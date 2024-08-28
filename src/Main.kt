import br.com.sankhya.cptalk.explodelote.model.ProdutoInfo
import br.com.sankhya.cptalk.explodelote.service.ProdutoService
import java.math.BigDecimal
fun main() {
    val prodService = ProdutoService()
    val prod1 = ProdutoInfo(
        codProd = BigDecimal(100),
        codLocal = BigDecimal(1100),
        controle = "A",
        estoque = BigDecimal(100)
    )

    val demandado = prodService.obterProdutosParaDemanda(listOf(prod1), BigDecimal(9))
    println(demandado)
}