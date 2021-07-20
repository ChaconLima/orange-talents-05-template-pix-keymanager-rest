package br.com.mateuschacon.keymanager.rest.cadastro.pix

import br.com.mateuschacon.keymanager.grpc.*
import br.com.mateuschacon.keymanager.grpc.NovaChavePixRequest
import br.com.mateuschacon.keymanager.rest.cadastro.pix.NovaChavePixRequest as Request
import br.com.mateuschacon.keymanager.rest.cliente.grpc.KeyManagerGrpcFactory
import br.com.mateuschacon.keymanager.rest.enums.TipoChaveEnum
import br.com.mateuschacon.keymanager.rest.enums.TipoContaEnum
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.inject.Inject
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class CadastrarPixEndPointTest {
    companion object {
        val CLIENTE_ID = UUID.randomUUID()
        val CHAVE_ID = UUID.randomUUID()
        val TIPO_CONTA = TipoContaEnum.CONTA_CORRENTE
        val TIPO_CHAVE = TipoChaveEnum.TELEFONE
        val VALOR_CHAVE = "+34998832651"
    }

    // *************************************************************
    // Conf
    // *************************************************************
    @Inject
    lateinit var cadastraStubMock: KeymanagerRegistraServiceGrpc.KeymanagerRegistraServiceBlockingStub

    @Inject
    @field:Client(value = "/")
    lateinit var registraClient: HttpClient


    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(KeymanagerRegistraServiceGrpc.KeymanagerRegistraServiceBlockingStub::class.java)

    }
    // *************************************************************
    // Testes
    // *************************************************************

    @Test
    fun `deve cadastrar uma nova chave pix`() {

        //cenario

        `when`(this.cadastraStubMock.registra(Mockito.any())).thenReturn(this.dadosNovaChavePixGrpcResponse())

        val novaChavePix: Request = Request(tipoConta = TIPO_CONTA, tipoChave = TIPO_CHAVE, valorChave = VALOR_CHAVE)

        //acao
        val request = HttpRequest.POST("api/clientes/$CLIENTE_ID/nova-chave", novaChavePix)
        val response = registraClient.toBlocking().exchange(request, Request::class.java)

        //validação
        with(response) {
            assertEquals(HttpStatus.CREATED, this.status)
            assertTrue(this.headers.contains("Location"))
            assertTrue(this.header("Location")!!.contains(CHAVE_ID.toString()))
        }

    }

    @Test
    fun `nao deve cadastrar uma nova chave pix quando os dados forem invalidos`() {

        //cenario
        val novaChavePix: Request = Request(tipoConta = null, tipoChave = null, valorChave = VALOR_CHAVE)

        val request = HttpRequest.POST("api/clientes/$CLIENTE_ID/nova-chave", novaChavePix)

        val thrown = assertThrows<HttpClientResponseException> {
            registraClient.toBlocking().exchange(request, Request::class.java)
        }

        with(thrown){
            assertEquals( HttpStatus.BAD_REQUEST.code , status.code)
            assertEquals("Bad Request" , this.localizedMessage)
        }
    }

    // *************************************************************
    // Dados
    // *************************************************************
    private fun dadosNovaChavePixGrpc(): NovaChavePixRequest = NovaChavePixRequest.newBuilder()
        .setTipoConta(TipoConta.valueOf(TIPO_CONTA.name))
        .setTipoChave(TipoChave.valueOf(TIPO_CHAVE.name))
        .setValorChave(VALOR_CHAVE)
        .setIndentificadorCliente(CLIENTE_ID.toString())
        .build()


    private fun dadosNovaChavePixGrpcResponse(): ChavePixResponse = ChavePixResponse.newBuilder()
        .setIdentificadorCliente(CLIENTE_ID.toString())
        .setIndentificadorPix(CHAVE_ID.toString())
        .build()

}