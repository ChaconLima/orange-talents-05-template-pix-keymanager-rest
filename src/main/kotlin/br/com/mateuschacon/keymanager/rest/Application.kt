package br.com.mateuschacon.keymanager.rest

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.mateuschacon.keymanager.rest")
		.start()
}

