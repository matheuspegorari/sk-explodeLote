package br.com.sankhya.cptalk.explodelote.service

import br.com.sankhya.jape.wrapper.JapeFactory
import br.com.sankhya.modelcore.util.DynamicEntityNames
import java.math.BigDecimal
import java.sql.Timestamp


class CabService {
    companion object {
        fun getAtualEst(tipOper: BigDecimal, dhAlter:Timestamp): Pair<BigDecimal, String> {
            val top = JapeFactory.dao(DynamicEntityNames.TIPO_OPERACAO)
                .findByPK(tipOper, dhAlter)

            val atualEst :Pair<BigDecimal, String> = when (top.asString("ATUALEST")) {
                "N" -> Pair(BigDecimal(0), "N")
                "E" -> Pair(BigDecimal(1), "N")
                "B" -> Pair(BigDecimal(-1), "N")
                "R" -> Pair(BigDecimal(1),"S")
                else -> throw IllegalArgumentException("Invalid value")
            }

            return atualEst
        }
    }
}