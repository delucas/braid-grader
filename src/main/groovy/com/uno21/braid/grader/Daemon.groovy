package com.uno21.braid.grader

import groovy.json.JsonSlurper
import java.net.ServerSocket

class Daemon {

	static main(args) {

		def listenPort = 4242
		def server = new ServerSocket(listenPort)

		while(true) {
			println "> Esperando tareas..."
			server.accept { socket ->
				println "> Nueva tarea para corregir..."

				socket.withStreams { input, output ->

					def reader = input.newReader()
					def buffer = reader.readLine()

					def json = new JsonSlurper().parseText(buffer)
					println json

					def grader = new JavaGrader()
					
					output << grader.grade(json)
				}
			}
		}
	}
}
