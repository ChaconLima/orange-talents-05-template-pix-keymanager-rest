package br.com.mateuschacon.keymanager.rest.validacao

import br.com.mateuschacon.keymanager.rest.enums.TipoChaveEnum
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
internal class ValidacaoTipoChaveTest {

    @Nested
    inner class ChaveAleatoriaTest {

        @Test
        fun `deve validar a chave do tipo aleatoria`() {

            //cenario
            val tipoChave = TipoChaveEnum.ALEATORIA

            //acao
            val isValid = tipoChave.valida("")

            //validacao
            with(isValid) {
                Assertions.assertEquals(true, isValid)
            }

        }

        @Test
        fun `nao deve validar a chave do tipo aleatoria`() {

            //cenario
            val tipoChave = TipoChaveEnum.ALEATORIA

            //acao
            val isValid = tipoChave.valida("invalido")

            //validacao
            with(isValid) {
                Assertions.assertEquals(false, isValid)
            }

        }

    }

    @Nested
    inner class ChaveEmailTest {

        @Test
        fun `deve validar a chave do tipo email`() {

            //cenario
            val tipoChave = TipoChaveEnum.EMAIL

            //acao
            val isValid = tipoChave.valida("email@email.com")

            //validacao
            with(isValid) {
                Assertions.assertEquals(true, isValid)
            }

        }

        @Test
        fun `nao deve validar a chave do tipo email quando estiver fora do padrao`() {

            //cenario
            val tipoChave = TipoChaveEnum.EMAIL

            //acao
            val isValid = tipoChave.valida("email-invalido-email.com")

            //validacao
            with(isValid) {
                Assertions.assertEquals(false, isValid)
            }

        }

        @Test
        fun `nao deve validar a chave do tipo email quando estiver em branco`() {

            //cenario
            val tipoChave = TipoChaveEnum.EMAIL

            //acao
            val isValid = tipoChave.valida("")

            //validacao
            with(isValid) {
                Assertions.assertEquals(false, isValid)
            }

        }

    }

    @Nested
    inner class ChaveTelefoneTest {

        @Test
        fun `deve validar a chave do tipo telefone`() {

            //cenario
            val tipoChave = TipoChaveEnum.TELEFONE

            //acao
            val isValid = tipoChave.valida("+55998832651")

            //validacao
            with(isValid) {
                Assertions.assertEquals(true, isValid)
            }

        }

        @Test
        fun `nao deve validar a chave do tipo telefone quando estiver fora do padrao`() {

            //cenario
            val tipoChave = TipoChaveEnum.ALEATORIA

            //acao
            val isValid = tipoChave.valida("invalido")

            //validacao
            with(isValid) {
                Assertions.assertEquals(false, isValid)
            }

        }

        @Test
        fun `nao deve validar a chave do tipo telefone quando estiver em branco`() {

            //cenario
            val tipoChave = TipoChaveEnum.TELEFONE

            //acao
            val isValid = tipoChave.valida("")

            //validacao
            with(isValid) {
                Assertions.assertEquals(false, isValid)
            }

        }
    }

    @Nested
    inner class ChaveCPFTest {

        @Test
        fun `deve validar a chave do tipo cpf`() {

            //cenario
            val tipoChave = TipoChaveEnum.CPF

            //acao
            val isValid = tipoChave.valida("99999999999")

            //validacao
            with(isValid) {
                Assertions.assertEquals(true, isValid)
            }

        }

        @Test
        fun `nao deve validar a chave do tipo cpf quando estiver fora do padrao`() {

            //cenario
            val tipoChave = TipoChaveEnum.CPF

            //acao
            val isValid = tipoChave.valida("invalido")

            //validacao
            with(isValid) {
                Assertions.assertEquals(false, isValid)
            }

        }

        @Test
        fun `nao deve validar a chave do tipo cpf quando estiver em branco`() {

            //cenario
            val tipoChave = TipoChaveEnum.CPF

            //acao
            val isValid = tipoChave.valida("")

            //validacao
            with(isValid) {
                Assertions.assertEquals(false, isValid)
            }

        }
    }

    @Nested
    inner class ChavePadraoTest {

        @Test
        fun `deve validar a chave do tipo padrao`() {

            //cenario
            val tipoChave = TipoChaveEnum.DEFAULT_TIPO_CHAVE

            //acao
            val isValid = tipoChave.valida("")

            //validacao
            with(isValid) {
                Assertions.assertEquals(false, isValid)
            }

        }

        @Test
        fun `nao deve validar a chave do tipo padrao`() {

            //cenario
            val tipoChave = TipoChaveEnum.ALEATORIA

            //acao
            val isValid = tipoChave.valida("invalido")

            //validacao
            with(isValid) {
                Assertions.assertEquals(false, isValid)
            }

        }

    }
}