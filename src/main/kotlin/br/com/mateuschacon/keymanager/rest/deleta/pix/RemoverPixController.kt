package br.com.mateuschacon.keymanager.rest.deleta.pix

import br.com.mateuschacon.keymanager.grpc.ChavePixExistenteRequest
import br.com.mateuschacon.keymanager.grpc.ChavePixExistenteResponse
import br.com.mateuschacon.keymanager.grpc.KeymanagerRemoverServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller(value = "/api")
class RemoverPixController(
    @Inject private val deleteChavePix: KeymanagerRemoverServiceGrpc.KeymanagerRemoverServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Delete(value = "/clientes/{identificadorCliente}/chave-pix/{identificadorPix}")
    fun deleta(
        @PathVariable identificadorCliente:UUID,
        @PathVariable identificadorPix: UUID
    ): MutableHttpResponse<Any>? = let{
            this.logger.info("Requisição de Exclusão da chave ${identificadorPix}")
            this.deleteChavePix.delete( ChavePixExistenteRequest.newBuilder()
                .setIndentificadorPix(identificadorPix.toString())
                .setIdentificadorCliente(identificadorCliente.toString())
                .build())
        }
        .run<ChavePixExistenteResponse?, MutableHttpResponse<Any>?> {
            HttpResponse.ok()
        }.also {
            this.logger.info("Requisição de Exclusão: Concluida")
        }
}