package br.com.sankhya.cptalk.explodelote.service

import br.com.sankhya.cptalk.explodelote.model.ProdutoInfo
import br.com.sankhya.jape.wrapper.JapeFactory
import br.com.sankhya.modelcore.util.DynamicEntityNames
import java.math.BigDecimal

class ProdutoService {
    fun obterProdutosParaDemanda(produtos: List<ProdutoInfo>, demanda: BigDecimal): List<ProdutoInfo> {
        val produtosUtilizados: MutableList<ProdutoInfo> = ArrayList()
        var restanteDemanda = demanda

        for (produto in produtos) {
            if (restanteDemanda.compareTo(BigDecimal.ZERO) > 0) {
                val quantidadeDisponivel: BigDecimal = produto.estoque
                var quantidadeUsada: BigDecimal

                if (quantidadeDisponivel.compareTo(restanteDemanda) >= 0) {
                    quantidadeUsada = restanteDemanda
                    restanteDemanda = BigDecimal.ZERO // Demanda atendida
                } else {
                    quantidadeUsada = quantidadeDisponivel
                    restanteDemanda = restanteDemanda.subtract(quantidadeDisponivel) // Subtrai o estoque do lote
                }

                val produtoUtilizado = ProdutoInfo(
                    produto.codProd,
                    produto.codLocal,
                    produto.controle,
                    quantidadeUsada
                )

                produtosUtilizados.add(produtoUtilizado)

                if (restanteDemanda.compareTo(BigDecimal.ZERO) == 0) {
                    break // Demanda atendida, sai do loop
                }
            }
        }
        if(restanteDemanda > BigDecimal.ZERO) {
            val prodRestante = produtos.first()
            val prodVO =  JapeFactory.dao(DynamicEntityNames.PRODUTO).findByPK(prodRestante.codProd)
            prodRestante.estoque = restanteDemanda
            prodRestante.codLocal = prodVO.asBigDecimal("CODLOCALPADRAO")
            prodRestante.controle = " "
            produtosUtilizados.add(prodRestante)
        }

        return produtosUtilizados
    }
}