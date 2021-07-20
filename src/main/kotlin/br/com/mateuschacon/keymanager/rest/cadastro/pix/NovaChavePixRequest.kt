package br.com.mateuschacon.keymanager.rest.cadastro.pix

import br.com.mateuschacon.keymanager.grpc.NovaChavePixRequest
import br.com.mateuschacon.keymanager.grpc.TipoChave
import br.com.mateuschacon.keymanager.grpc.TipoConta
import br.com.mateuschacon.keymanager.rest.ValidarChavePix
import br.com.mateuschacon.keymanager.rest.enums.TipoChaveEnum
import br.com.mateuschacon.keymanager.rest.enums.TipoContaEnum
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@Introspected
@ValidarChavePix
data class NovaChavePixRequest(

    @field:NotNull
    val tipoChave: TipoChaveEnum?,

    @field:Size(max = 77)
    val valorChave: String?,

    @field:NotNull
    val tipoConta: TipoContaEnum?

) {
    fun paraNovaChavePixRequestGrpc(identificadorCliente:UUID): NovaChavePixRequest =
        NovaChavePixRequest.newBuilder()
            .setIndentificadorCliente( "c56dfef4-7901-44fb-84e2-a2cefb157890" )
            .setTipoChave(TipoChave.valueOf(this.tipoChave!!.name))
            .setValorChave(this.valorChave!! ?: "")
            .setTipoConta(TipoConta.valueOf(this.tipoConta!!.name))
            .build()
}