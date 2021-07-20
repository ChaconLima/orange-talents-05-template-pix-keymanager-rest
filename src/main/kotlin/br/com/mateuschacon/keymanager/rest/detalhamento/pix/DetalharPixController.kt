package br.com.mateuschacon.keymanager.rest.detalhamento.pix

import br.com.mateuschacon.keymanager.grpc.ChavePixDetalhesRequest
import br.com.mateuschacon.keymanager.grpc.ChavePixDetalhesResponse
import br.com.mateuschacon.keymanager.grpc.ChavePixResponse
import br.com.mateuschacon.keymanager.grpc.KeymanagerDetalhamentoServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller(value = "/api")
class DetalharPixController(
    @Inject private val detalhaChavePix: KeymanagerDetalhamentoServiceGrpc.KeymanagerDetalhamentoServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get(value = "clientes/{identificadorCliente}/chave-pix/{identificadorChave}")
    fun detalharPorIdentificadores(
        @PathVariable identificadorCliente:UUID,
        @PathVariable identificadorChave: UUID,
    ): MutableHttpResponse<DetalharPixDto> {

        val fResponse=chavePixDetalhesResponse(
            identificadorChave = identificadorChave,
            identificadorCliente = identificadorCliente,
            pix = null
        )

       return HttpResponse.ok( DetalharPixDto(fResponse)).also {
            this.logger.info("Requisição de Detalhamento: Concluida")
        }
    }

    @Get(value = "chave-pix")
    fun detalharPorChavePix(
      @QueryValue chavePix:String
    ): MutableHttpResponse<DetalharPixDto> {

        val fResponse=chavePixDetalhesResponse(
            identificadorChave = null,
            identificadorCliente = null,
            pix = chavePix
        )

        return HttpResponse.ok( DetalharPixDto(fResponse)).also {
            this.logger.info("Requisição de Detalhamento: Concluida")
        }
    }

    private fun chavePixDetalhesResponse(
        identificadorChave: UUID?,
        pix: String?,
        identificadorCliente: UUID?
    ): ChavePixDetalhesResponse {

        this.logger.info("Requisição de Detalhamento : ${identificadorChave ?: pix } ")

        return this.detalhaChavePix.detalha(
            ChavePixDetalhesRequest.newBuilder()
                .setIdentificadoresChave(
                    ChavePixResponse.newBuilder()
                        .setIdentificadorCliente(identificadorCliente.toString() ?: "")
                        .setIndentificadorPix(identificadorChave.toString() ?: "")
                        .build()
                ).setValorChavePix(pix ?: "")
                .build()
        )

    }
}