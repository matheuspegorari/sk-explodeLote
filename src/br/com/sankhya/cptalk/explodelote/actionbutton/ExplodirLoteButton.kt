package br.com.sankhya.cptalk.explodelote.actionbutton

import br.com.sankhya.cptalk.explodelote.model.ProdutoInfo
import br.com.sankhya.cptalk.explodelote.service.CabService
import br.com.sankhya.cptalk.explodelote.service.EstoqueService
import br.com.sankhya.cptalk.explodelote.service.ProdutoService
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava
import br.com.sankhya.extensions.actionbutton.ContextoAcao
import br.com.sankhya.jape.wrapper.JapeFactory
import br.com.sankhya.modelcore.comercial.CentralFinanceiro
import br.com.sankhya.modelcore.comercial.impostos.ImpostosHelpper
import br.com.sankhya.modelcore.util.DynamicEntityNames
import java.math.BigDecimal

class ExplodirLoteButton : AcaoRotinaJava {
    override fun doAction(ctx: ContextoAcao?) {
        var nuNota: BigDecimal? = null
        val linhas = ctx?.linhas
        if (linhas != null) {
            for (linha in linhas) {
                nuNota = linha.getCampo("NUNOTA") as BigDecimal
                val cabVO = JapeFactory.dao(DynamicEntityNames.CABECALHO_NOTA).findByPK(nuNota)
                val atualEstoque = CabService.getAtualEst(cabVO.asBigDecimal("CODTIPOPER"), cabVO.asTimestamp("DHTIPOPER"))
                val sequencia = linha.getCampo("SEQUENCIA") as BigDecimal
                val codVol = linha.getCampo("CODVOL") as String
                val codEmp = linha.getCampo("CODEMP") as BigDecimal
                val codProd = linha.getCampo("CODPROD") as BigDecimal
                val vlrUnit = linha.getCampo("VLRUNIT") as BigDecimal
                val quantidade = linha.getCampo("QTDNEG") as BigDecimal

                // verifica se existe Lote Padrão
                val estService = EstoqueService()
                val prodService = ProdutoService()
                var produtosDemanda: List<ProdutoInfo>?
                val produtosEstoque = estService.getListaProdutoEstoque(codProd, codEmp)

                if (produtosEstoque.isEmpty()) {
                    val prodVO = JapeFactory.dao(DynamicEntityNames.PRODUTO).findByPK(codProd)
                    produtosDemanda = listOf(ProdutoInfo(
                        codProd = codProd,
                        codLocal = prodVO.asBigDecimal("CODLOCALPADRAO"),
                        controle = " ",
                        estoque = quantidade
                    ))
                } else {
                    produtosDemanda = prodService.obterProdutosParaDemanda(produtosEstoque, quantidade)
                }

                val listaItens = JapeFactory.dao(DynamicEntityNames.ITEM_NOTA).find("NUNOTA = $nuNota")
                var lastSequencia = listaItens.last().asBigDecimal("SEQUENCIA")
                for (produto in produtosDemanda) {
                    lastSequencia = lastSequencia.add(BigDecimal.ONE)
                    criaLinhaProd(
                        nuNota,
                        atualEstoque.first,
                        atualEstoque.second,
                        lastSequencia,
                        codProd,
                        produto.controle,
                        produto.codLocal,
                        codVol,
                        vlrUnit,
                        produto.estoque
                    )
                }

                deletarLinhaOriginal(nuNota, sequencia)
            }

        }
        // refaz o financeiro
        val query = ctx?.query
        query?.update("DELETE FROM TGFFIN WHERE NUNOTA = $nuNota")
        refresh(nuNota)

    }

    private fun deletarLinhaOriginal(nuNota: BigDecimal, sequencia: BigDecimal) {
        JapeFactory.dao(DynamicEntityNames.ITEM_NOTA).delete(nuNota, sequencia)
    }

    private fun criaLinhaProd(
        nuNota: BigDecimal,
        atualEstoque: BigDecimal,
        reserva: String,
        sequencia: BigDecimal,
        codProd: BigDecimal,
        controle: String,
        codLocal: BigDecimal,
        codVol: String,
        vlrUnit: BigDecimal,
        qtdNeg: BigDecimal
    ) {
        val item = JapeFactory.dao(DynamicEntityNames.ITEM_NOTA).create()
        item.set("NUNOTA", nuNota)
            .set("ATUALESTOQUE", atualEstoque)
            .set("RESERVA", reserva)
            .set("SEQUENCIA", sequencia)
            .set("CODPROD", codProd)
            .set("CODLOCALORIG", codLocal)
            .set("VLRUNIT",vlrUnit)
            .set("VLRTOT", vlrUnit.multiply(qtdNeg))
            .set("CODVOL", codVol)
            .set("CONTROLE", controle)
            .set("QTDNEG", qtdNeg)
            .save()
    }


    @Throws(Exception::class)
    fun refresh(nuNota: BigDecimal?) {
        val impostosHelper = ImpostosHelpper()
        impostosHelper.carregarNota(nuNota)
        impostosHelper.calcularImpostos(nuNota)
        impostosHelper.totalizarNota(nuNota)
        val cf = CentralFinanceiro()
        cf.inicializaNota(nuNota)
        cf.refazerFinanceiro()
    }
}