package br.com.mateuschacon.keymanager.rest.validacao

import br.com.mateuschacon.keymanager.rest.validadores.GlobalExceptionHandler
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
internal class GlobalExceptionHandlerTest {

    companion object {
        val MENSAGEM_NOT_FOUND = "recurso não encontrado"
        val MENSAGEM_ALREADY_EXISTIS = "chave já cadastrada"
        val MENSAGEM_INVALID_ARGUMENT = "Dados de Requisição estão inválidos"
    }
    val request = HttpRequest.GET<Any>("/")

    @Test
    fun `deve retornar status 404 quando o servidor GRPC devolver NOT_FOUND`() {

        //senario
        val exception = StatusRuntimeException(Status.NOT_FOUND.withDescription(MENSAGEM_NOT_FOUND))

        //acao
        val response = GlobalExceptionHandler().handle(request, exception)

        //validacao
        with(response){
            assertEquals(HttpStatus.NOT_FOUND, this.status)
            assertNotNull(this.body())
            assertEquals(MENSAGEM_NOT_FOUND, (this.body() as JsonError).message)
        }


    }

    @Test
    fun `deve retornar status 422 quando servidor GRPC devolver ALREADY_EXISTIS`() {

        //cenario
        val exception = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(MENSAGEM_ALREADY_EXISTIS))

        //ação
        val response = GlobalExceptionHandler().handle(request, exception)

        //validação
        with(response){
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.status)
            assertNotNull(this.body())
            assertEquals(MENSAGEM_ALREADY_EXISTIS, (this.body() as JsonError).message)
        }

    }

    @Test
    fun `deve retornar status 400 quando servidor GRPC devolver INVALID_ARGUMENT`() {

        //cenario
        val exception = StatusRuntimeException(Status.INVALID_ARGUMENT)

        //ação
        val response = GlobalExceptionHandler().handle(request, exception)

        //validação
        with(response){
            assertEquals(HttpStatus.BAD_REQUEST, this.status)
            assertNotNull(this.body())
            assertEquals(MENSAGEM_INVALID_ARGUMENT, (this.body() as JsonError).message)
        }

    }

    @Test
    fun `deve retornar status 500 quando tiver algum erro desconhecido`() {

        //cenario
        val exception = StatusRuntimeException(Status.INTERNAL)

        //ação
        val response = GlobalExceptionHandler().handle(request, exception)

        //validação
        with(response){
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.status)
            assertNotNull(this.body())
            assertTrue((this.body() as JsonError).message.contains("INTERNAL"))
        }

    }
}