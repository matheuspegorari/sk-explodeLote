package br.com.sankhya.cptalk.explodelote.service

import br.com.sankhya.jape.wrapper.JapeFactory
import br.com.sankhya.modelcore.util.DynamicEntityNames
import java.math.BigDecimal
import java.sql.Timestamp


class CabService {
    companion object {
        fun getAtualEst(tipOper: BigDecimal, dhAlter:Timestamp): BigDecimal {
            val top = JapeFactory.dao(DynamicEntityNames.TIPO_OPERACAO)
                .findByPK(tipOper, dhAlter)

            val atualEst = when (top.asString("ATUALEST")) {
                "N" -> BigDecimal(0)
                "E" -> BigDecimal(1)
                "B" -> BigDecimal(-1)
                else -> throw IllegalArgumentException("Invalid value")
            }

            return atualEst
        }
    }
}