package br.com.mateuschacon.keymanager.rest.deleta.pix

import br.com.mateuschacon.keymanager.grpc.ChavePixExistenteResponse
import br.com.mateuschacon.keymanager.grpc.KeymanagerRemoverServiceGrpc
import br.com.mateuschacon.keymanager.rest.cliente.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class DeletarPixEndPointTest {
    companion object {
        val CLIENTE_ID = UUID.randomUUID()
        val CHAVE_ID = UUID.randomUUID()
    }

    // *************************************************************
    // Conf
    // *************************************************************
    @Inject
    lateinit var deleteStubMock: KeymanagerRemoverServiceGrpc.KeymanagerRemoverServiceBlockingStub

    @Inject
    @field:Client(value = "/")
    lateinit var deleteClient: HttpClient

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            mock(KeymanagerRemoverServiceGrpc.KeymanagerRemoverServiceBlockingStub::class.java)

    }

    // *************************************************************
    // Testes
    // *************************************************************
    @Test
    fun `deve deletar chave pix`() {

        //cenario
        `when`(
            this.deleteStubMock.delete(any())
        ).thenReturn(
            ChavePixExistenteResponse.newBuilder()
                .setOk("ok")
                .build()
        )

        //ação
        val request = HttpRequest.DELETE<Any>("/api/clientes/${CLIENTE_ID}/chave-pix/${CHAVE_ID}")
        val response = deleteClient.toBlocking().exchange(request, Any::class.java)

        //validação
        with(response) {
            assertEquals(HttpStatus.OK, this.status)
        }
    }

    @Test
    fun `nao deve deletar chave pix quando passar valores invalidos`() {

        //cenario

        //ação
        val request = HttpRequest.DELETE<Any>("/api/clientes/INVALIDO/chave-pix/INVALIDO")
        val thrown = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            deleteClient.toBlocking().exchange(request, Any::class.java)
        }

        //validação
        with(thrown) {
            assertEquals(HttpStatus.BAD_REQUEST.code , this.status.code)
        }
    }


}