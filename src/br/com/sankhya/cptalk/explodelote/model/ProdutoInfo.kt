package br.com.sankhya.cptalk.explodelote.model

import java.math.BigDecimal

data class ProdutoInfo(
    var codProd: BigDecimal,
    var codLocal: BigDecimal,
    var controle: String,
    var estoque: BigDecimal
)
