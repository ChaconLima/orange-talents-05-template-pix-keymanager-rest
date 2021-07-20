package br.com.mateuschacon.keymanager.rest.detalhamento.pix

import br.com.mateuschacon.keymanager.grpc.ChavePixDetalhesResponse
import io.micronaut.core.annotation.Introspected

@Introspected
class DetalharPixDto( fResponse: ChavePixDetalhesResponse) {

    val identificadorCliente = fResponse.identificadoresChave.identificadorCliente
    val identificadorPix = fResponse.identificadoresChave.indentificadorPix

    val tipoChave = fResponse.tipoChave
    val valorChave = fResponse.valorChave

    val titularNome = fResponse.titular.nome
    val titularCpf = fResponse.titular.cpf

    val nomeInstituicaoFinanceira = fResponse.contaVinculada.nomeInstituicaoFinanceira
    val agencia = fResponse.contaVinculada.agencia
    val numeroConta = fResponse.contaVinculada.numeroConta
    val tipoConta = fResponse.contaVinculada.tipoConta
    val ispb = fResponse.contaVinculada.ispb

    val criadoEm = fResponse.criadoEm
}