package br.com.mateuschacon.keymanager.rest.cliente.grpc

import br.com.mateuschacon.keymanager.grpc.KeymanagerDetalhamentoServiceGrpc
import br.com.mateuschacon.keymanager.grpc.KeymanagerListagemServiceGrpc
import br.com.mateuschacon.keymanager.grpc.KeymanagerRegistraServiceGrpc
import br.com.mateuschacon.keymanager.grpc.KeymanagerRemoverServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(
    @GrpcChannel(value = "keyManager") val channel: ManagedChannel
    ) {

    @Singleton
    fun cadastraNovaChave() = KeymanagerRegistraServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removerChavePix() = KeymanagerRemoverServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun detalharChavePix() = KeymanagerDetalhamentoServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listagemChavePix() = KeymanagerListagemServiceGrpc.newBlockingStub(channel)
}