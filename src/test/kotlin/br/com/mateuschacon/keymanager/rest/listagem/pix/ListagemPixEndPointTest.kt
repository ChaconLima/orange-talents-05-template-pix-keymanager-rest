package br.com.mateuschacon.keymanager.rest.listagem.pix

import br.com.mateuschacon.keymanager.grpc.*
import br.com.mateuschacon.keymanager.rest.cliente.grpc.KeyManagerGrpcFactory
import br.com.mateuschacon.keymanager.rest.detalhamento.pix.DetalharPixEndPointTest
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class ListagemPixEndPointTest {

    companion object {
        val identificadorCliente = "c56dfef4-7901-44fb-84e2-a2cefb157890"

        val chave_1 = ChavePixClienteResponse.ChavePixDetalhesClienteResponse.newBuilder()
            .setTipoChave(TipoChave.ALEATORIA)
            .setValorChave(UUID.randomUUID().toString())
            .setTipoConta(TipoConta.CONTA_POUPANCA)
            .setIdentificadorPix(UUID.randomUUID().toString())
            .setCriadoEm(Timestamp.newBuilder()
                    .setNanos(25171000)
                    .setSeconds(1626693609)
                .build())
            .build()
        val chave_2 = ChavePixClienteResponse.ChavePixDetalhesClienteResponse.newBuilder()
            .setTipoChave(TipoChave.CPF)
            .setValorChave("99999999999")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setIdentificadorPix(UUID.randomUUID().toString())
            .setCriadoEm(Timestamp.newBuilder()
                .setNanos(36848000)
                .setSeconds(1626365363)
                .build())
            .build()

        val chaves = listOf<ChavePixClienteResponse.ChavePixDetalhesClienteResponse>( chave_1, chave_2)
    }

    // *************************************************************
    // Conf
    // *************************************************************
    @Inject
    lateinit var listagemStubMock: KeymanagerListagemServiceGrpc.KeymanagerListagemServiceBlockingStub

    @Inject
    @field:Client(value = "/")
    lateinit var litarClient: HttpClient

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(KeymanagerListagemServiceGrpc.KeymanagerListagemServiceBlockingStub::class.java)

    }

    // *************************************************************
    // Testes
    // *************************************************************
    @Test
    fun `deve listar as chaves pix quando o identificador do cliente for passado`(){

        //cenario
        `when`(
            this.listagemStubMock.listar(Mockito.any())
        ).thenReturn(this.dadosReponseGrpc())

        //ação
        val request = HttpRequest.GET<Any>("/api/clientes/${identificadorCliente}")
        val response = litarClient.toBlocking().exchange(request, Any::class.java)

        //validação
        with(response){
            Assertions.assertEquals(HttpStatus.OK, this.status)
            Assertions.assertNotNull(this.body())
        }
    }

    @Test
    fun `nao deve listar as chaves pix quando o identificador do cliente for passado invalido`(){

        //cenario

        //ação
        val request = HttpRequest.GET<Any>("/api/clientes/invalido")
        val thrown = assertThrows<HttpClientResponseException> {
            litarClient.toBlocking().exchange(request, Any::class.java)
        }

        //validação
        with(thrown){
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.code, this.status.code)
        }
    }
    // *************************************************************
    // Dados
    // *************************************************************

    private fun dadosReponseGrpc(): ChavePixClienteResponse =
        ChavePixClienteResponse.newBuilder()
            .setIdentificadorCliente(identificadorCliente)
            .addAllChavePixDetalhesClienteResponse(chaves)
            .build()
}