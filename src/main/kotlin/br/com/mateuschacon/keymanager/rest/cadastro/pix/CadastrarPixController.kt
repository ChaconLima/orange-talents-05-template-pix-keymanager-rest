package br.com.mateuschacon.keymanager.rest.cadastro.pix

import br.com.mateuschacon.keymanager.grpc.KeymanagerRegistraServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Controller(value = "api/clientes/{identificadorCliente}")
@Validated
class CadastrarPixController(
    @Inject private val registrarChavePixCliente: KeymanagerRegistraServiceGrpc.KeymanagerRegistraServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Post(value = "/nova-chave")
    fun cadastra(
        identificadorCliente:UUID,
        @Body @Valid novaChavePixRequest: NovaChavePixRequest
    ):HttpResponse<String> =
        let{
            this.logger.info("Requisição de Cadastro : ${novaChavePixRequest}")
            this.registrarChavePixCliente.registra(
                novaChavePixRequest.paraNovaChavePixRequestGrpc(identificadorCliente))
        }.let {
            HttpResponse.uri("/api/clientes/$identificadorCliente/chave-pix/${it.indentificadorPix}")
        }.run {
            HttpResponse.created<String?>(this)
        }.also {
            this.logger.info("Requisição de Cadastro : Concluida ")
        }
}