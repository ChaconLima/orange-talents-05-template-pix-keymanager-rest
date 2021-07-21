package br.com.mateuschacon.keymanager.rest.listagem.pix

import br.com.mateuschacon.keymanager.grpc.ChavePixClienteRequest
import br.com.mateuschacon.keymanager.grpc.KeymanagerListagemServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller(value = "api/")
class ListagemPixController(
    @Inject private val litagemGrpc: KeymanagerListagemServiceGrpc.KeymanagerListagemServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get(value = "clientes/{identificadorCliente}")
    fun listar( @PathVariable identificadorCliente: UUID): MutableHttpResponse<List<ChavePixDto>> =
        let {
            this.logger.info("Requisição de Listagem da chave ${identificadorCliente}")

            this.litagemGrpc.listar(ChavePixClienteRequest.newBuilder()
                .setIdentificadorCliente(identificadorCliente.toString())
                .build())
        }.chavePixDetalhesClienteResponseList.map{
            ChavePixDto(it)
        }.run {
            HttpResponse.ok(this)
        }.also {
            this.logger.info("Requisição de Listagem da chave: Concluida")
        }
}