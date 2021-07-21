package br.com.mateuschacon.keymanager.rest.listagem.pix

import br.com.mateuschacon.keymanager.grpc.ChavePixClienteResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


class ChavePixDto( chave:ChavePixClienteResponse.ChavePixDetalhesClienteResponse ) {
    val tipoChave = chave.tipoChave.name
    val valorChave = chave.valorChave
    val tipoConta = chave.tipoConta.name
    val identiticadorPix = chave.identificadorPix
    val criadoEm = chave.criadoEm.let {
        LocalDateTime.ofInstant( Instant.ofEpochSecond( it.seconds, it.nanos.toLong()), ZoneOffset.UTC )
    }
}