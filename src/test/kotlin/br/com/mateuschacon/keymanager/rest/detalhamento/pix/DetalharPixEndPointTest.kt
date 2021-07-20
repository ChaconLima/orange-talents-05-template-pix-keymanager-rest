package br.com.mateuschacon.keymanager.rest.detalhamento.pix

import br.com.mateuschacon.keymanager.grpc.*
import br.com.mateuschacon.keymanager.rest.cliente.grpc.KeyManagerGrpcFactory
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest( transactional = false)
internal class DetalharPixEndPointTest {

    companion object {
        val identificadorCliente = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        val identificadorPix = "100d1f63-76b4-4332-a8a6-34b3ae7aa3d5"
        val tipoChave = "CPF"
        val valorChave = "33059192057"
        val titularNome = "Steve Jobs"
        val titularCpf = "33059192057"
        val nomeInstituicaoFinanceira = "ITAU UNIBANCO S.A."
        val agencia = "0001"
        val numeroConta = "123456"
        val tipoConta = "CONTA_CORRENTE"
        val ispb = "60701190"
        val criadoEm = "2021-07-20T19:46:58.815524"
    }

    // *************************************************************
    // Conf
    // *************************************************************
    @Inject
    lateinit var detalharStubMock: KeymanagerDetalhamentoServiceGrpc.KeymanagerDetalhamentoServiceBlockingStub

    @Inject
    @field:Client(value = "/")
    lateinit var deleteClient: HttpClient

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(KeymanagerDetalhamentoServiceGrpc.KeymanagerDetalhamentoServiceBlockingStub::class.java)

    }
    // *************************************************************
    // Testes
    // *************************************************************
    @Test
    fun `deve detalhar os dados da chave pix quando pesquisado pelos identificadores internos`(){

        //cenario
        Mockito.`when`(
            this.detalharStubMock.detalha(Mockito.any())
        ).thenReturn(this.dadosReponseGrpc_identificadores())

        //ação
        val request = HttpRequest.GET<Any>("/api/clientes/${identificadorCliente}/chave-pix/${identificadorPix}")
        val response = deleteClient.toBlocking().exchange(request, Any::class.java)

        //validação
        with(response){
            assertEquals(HttpStatus.OK, this.status)
            assertNotNull(this.body())
        }

    }

    @Test
    fun `nao deve detalhar os dados da chave pix quando pesquisado pelos identificadores internos invalidos`(){
        //senario

        //ação
        val request = HttpRequest.GET<Any>("/api/clientes/INVALIDO/chave-pix/INVALIDO")

        val thrown = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            deleteClient.toBlocking().exchange(request, Any::class.java)
        }

        //validação
        with(thrown){
            assertEquals(HttpStatus.BAD_REQUEST.code , this.status.code)
        }

    }

    @Test
    fun `deve detalhar os dados da chave pix quando pesquisado pelo valor da chave`(){

        //cenario
        Mockito.`when`(
            this.detalharStubMock.detalha(Mockito.any())
        ).thenReturn(this.dadosReponseGrpc_chave())

        //ação
        val request = HttpRequest.GET<Any>("/api/chave-pix?chavePix=${valorChave}")
        val response = deleteClient.toBlocking().exchange(request, Any::class.java)

        //validação
        with(response){
            assertEquals(HttpStatus.OK, this.status)
            assertNotNull(this.body())
        }

    }

    // *************************************************************
    // Dados
    // *************************************************************
    private fun dadosReponseGrpc_identificadores(): ChavePixDetalhesResponse =
        ChavePixDetalhesResponse.newBuilder()
            .setIdentificadoresChave(
                ChavePixResponse.newBuilder()
                    .setIndentificadorPix(identificadorPix)
                    .setIdentificadorCliente(identificadorCliente)
                    .build()
            )
            .setTipoChave(TipoChave.valueOf(tipoChave))
            .setValorChave(valorChave)
            .setTitular(
                Titular.newBuilder()
                    .setCpf(titularCpf)
                    .setNome(titularNome)
                    .build()
            )
            .setContaVinculada(
                ContaVinculada.newBuilder()
                    .setNomeInstituicaoFinanceira(nomeInstituicaoFinanceira)
                    .setAgencia(agencia)
                    .setNumeroConta(numeroConta)
                    .setTipoConta(TipoConta.valueOf(tipoConta))
                    .setIspb(ispb)
                    .build()
            )
            .setCriadoEm(criadoEm)
            .build()

    private fun dadosReponseGrpc_chave(): ChavePixDetalhesResponse =
        ChavePixDetalhesResponse.newBuilder()
            .setTipoChave(TipoChave.valueOf(tipoChave))
            .setValorChave(valorChave)
            .setTitular(
                Titular.newBuilder()
                    .setCpf(titularCpf)
                    .setNome(titularNome)
                    .build()
            )
            .setContaVinculada(
                ContaVinculada.newBuilder()
                    .setNomeInstituicaoFinanceira(nomeInstituicaoFinanceira)
                    .setAgencia(agencia)
                    .setNumeroConta(numeroConta)
                    .setTipoConta(TipoConta.valueOf(tipoConta))
                    .setIspb(ispb)
                    .build()
            )
            .setCriadoEm(criadoEm)
            .build()

}