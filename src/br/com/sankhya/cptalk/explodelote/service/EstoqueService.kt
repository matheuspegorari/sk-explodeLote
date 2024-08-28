package br.com.sankhya.cptalk.explodelote.service

import br.com.sankhya.cptalk.explodelote.model.ProdutoInfo
import br.com.sankhya.jape.core.JapeSession
import br.com.sankhya.jape.core.JapeSession.SessionHandle
import br.com.sankhya.jape.dao.JdbcWrapper
import br.com.sankhya.jape.sql.NativeSql
import br.com.sankhya.modelcore.MGEModelException
import br.com.sankhya.modelcore.util.EntityFacadeFactory
import com.sankhya.util.JdbcUtils
import java.math.BigDecimal
import java.sql.ResultSet

class EstoqueService {

    @Throws(MGEModelException::class)
    fun getListaProdutoEstoque(codProd: BigDecimal, codEmp: BigDecimal): List<ProdutoInfo> {
        var jdbc: JdbcWrapper? = null
        var sql: NativeSql? = null
        var rset: ResultSet? = null
        var hnd: SessionHandle? = null

        var codLocal: BigDecimal?
        var estoque: BigDecimal?
        var controle: String?
        val listaProdutos: MutableList<ProdutoInfo> = ArrayList()

        try {
            hnd = JapeSession.open()
            hnd.findersMaxRows = -1
            val entity = EntityFacadeFactory.getDWFFacade()
            jdbc = entity.jdbcWrapper
            jdbc.openSession()

            sql = NativeSql(jdbc)

            sql.appendSql(
                ("""SELECT 
                        this.CODPROD, 
                        this.CODLOCAL, 
                        this.CONTROLE, 
                        MAX(this.DTVAL) AS DTVAL, 
                        THIS.AD_LOTEPADRAO,
                        ROUND(SUM(NVL(this.ESTOQUE, 0) - NVL(this.RESERVADO, 0)  ),NVL(PRO.DECQTD,0)) AS DISPONIVEL
                        
                     FROM 
                        TGFEST THIS 
                        INNER JOIN TGFEMP EMP ON EMP.CODEMP = this.CODEMP 
                        INNER JOIN TGFPRO PRO ON PRO.CODPROD = this.CODPROD 
                        INNER JOIN TGFGRU GRU ON GRU.CODGRUPOPROD = PRO.CODGRUPOPROD 
                    WHERE 
                        this.CODPARC = 0 
                        AND this.CODPROD = :CODPROD
                        AND (this.DTVAL IS NULL OR this.DTVAL >= TRUNC(SYSDATE)) 
                        AND EMP.CODEMP = :CODEMP
                    GROUP BY 
                        this.CODPROD, 
                        this.CODLOCAL, 
                        this.CONTROLE, 
                        NVL(PRO.DECQTD,0),
                        THIS.AD_LOTEPADRAO
                    HAVING ROUND(SUM(NVL(this.ESTOQUE, 0) - NVL(this.RESERVADO, 0)  ),NVL(PRO.DECQTD,0)) > 0 
                    ORDER BY AD_LOTEPADRAO, DTVAL""")
            )

            sql.setNamedParameter("CODPROD", codProd)
            sql.setNamedParameter("CODEMP", codEmp)

            rset = sql.executeQuery()

            while (rset.next()) {
                codLocal = rset.getBigDecimal("CODLOCAL")
                controle = rset.getString("CONTROLE")
                estoque = rset.getBigDecimal("DISPONIVEL")

                val produto = ProdutoInfo(codProd, codLocal, controle, estoque)

                listaProdutos.add(produto)
            }
        } catch (e: java.lang.Exception) {
            MGEModelException.throwMe(e)
        } finally {
            JdbcUtils.closeResultSet(rset)
            NativeSql.releaseResources(sql)
            JdbcWrapper.closeSession(jdbc)
            JapeSession.close(hnd)
        }
        return listaProdutos
    }

}