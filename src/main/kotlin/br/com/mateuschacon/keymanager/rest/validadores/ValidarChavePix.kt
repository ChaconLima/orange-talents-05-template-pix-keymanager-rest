package br.com.mateuschacon.keymanager.rest

import br.com.mateuschacon.keymanager.rest.cadastro.pix.NovaChavePixRequest
import br.com.mateuschacon.keymanager.rest.cadastro.pix.TipoChaveEnum
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ChavePixValidador::class])
annotation class ValidarChavePix(
    val message: String = "Chave Pix inválida (\${validatedValue.tipoChave})",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ChavePixValidador() : ConstraintValidator<ValidarChavePix, NovaChavePixRequest> {
    override fun isValid(value: NovaChavePixRequest?, context: ConstraintValidatorContext?): Boolean {

        if (value?.tipoChave == null)
            return false

        return value.tipoChave.valida(value.valorChave)
    }

}
