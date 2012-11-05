package com.uno21.braid.grader

import groovy.json.JsonBuilder
import groovy.json.JsonOutput

import org.apache.commons.io.FileUtils

class JavaGrader {
	def completeMapWithQualifications(def map, def score, def detail) {
		map.put('testScore', score)
		map.put('testDetail', detail)
	}
	
	def jsonify(def map) {
		new JsonOutput().toJson(new JsonBuilder().call(map))
	}
	
	def processNotaTests(def myFile) {
	
		def fields = myFile.readLines().get(3).split(',').toList()
		def totalTests = fields[0].split(':')[1].trim() as Integer
		def fallos = fields[1].split(':')[1].trim() as Integer
		def errores = fields[2].split(':')[1].trim() as Integer
	
		// Calculamos la nota de tests
		return 10 * ((totalTests - (fallos + errores))/totalTests) as Double
	}
	
	def processCheckstyle(def myFile) {
		
		def buffer = myFile.readLines()
		def theXml = new XmlParser().parse(myFile)
		
		def incidencias = [] as List
		theXml.file.each { file ->
			file.error.each {
			incidencias << "${file.attributes()['name'][file.attributes()['name'].lastIndexOf('src/')..-1]}:${it.attributes()['line']} -> ${it.attributes()['message']}"
			}
		}
		
		return incidencias
	}
	
	def grade(def json) {
	
		def p = "git clone git@bitbucket.org:tallerweb/${json.nombreTarea}.git grader-${json.nombreTarea}".execute()
		p.waitFor()
		
		p = "git checkout resolucion --git-dir grader-${json.nombreTarea}/.git".execute()
		p.waitFor()
		
		def rutaGraderConfiable = "./grader-${json.nombreTarea}"
	
		def correctionsMap = [:]
		def nombreTemporalCarpeta = 'pull-' + new Date().time
		def rutaTareaAlumno
		
		p = "git clone ${json.repoSshUrl} ${nombreTemporalCarpeta}".execute()
		p.waitFor()
	
		// No existía el repo, o hubo problemas con git
		if (p.exitValue() == 1) {
			completeMapWithQualifications(correctionsMap, 0.0, p.in.text)
	
			return jsonify(correctionsMap)
		}
	
		try {
			// Copiamos la carpeta de src en la src del repo de pruebas confiable
			rutaTareaAlumno = './tarea-' + new Date().time
			
			FileUtils.copyDirectory(new File(rutaGraderConfiable), new File(rutaTareaAlumno))
			FileUtils.copyDirectory(new File("./${nombreTemporalCarpeta}/src/main/java/"), new File("${rutaTareaAlumno}/src/main/java/"))
	
			def deletable = new File("./${nombreTemporalCarpeta}")
			deletable.deleteDir()
			
		} catch (FileNotFoundException fe) {
			// Se envió un repo no válido
			completeMapWithQualifications(correctionsMap, 0.0, 'El repositorio suministrado no es válido')
			return jsonify(correctionsMap)
		}
	
		p = "mvn -f ${rutaTareaAlumno}/pom.xml compile".execute()
		p.waitFor()
	
		// No compila el código
		if (p.exitValue() == 1) {
			completeMapWithQualifications(correctionsMap, 0.0, p.in.text)
	
			return jsonify(correctionsMap)
		}
	
		// Ejecutamos la tarea mvn test
		p = "mvn -f ${rutaTareaAlumno}/pom.xml test".execute()
		p.waitFor()
		
		p = "mvn -f ${rutaTareaAlumno}/pom.xml checkstyle:checkstyle".execute()
		p.waitFor()
	
		// Tomamos el reporte y lo mostramos
		try {
	
			def myTestsFile = new File("${rutaTareaAlumno}/target/surefire-reports/ar.edu.unlam.tallerweb.ecuaciones.EcuacionTest.txt")
			def notaTests = processNotaTests(myTestsFile)
			
			def incidenciasCheckstyle = processCheckstyle(new File("${rutaTareaAlumno}/target/checkstyle-result.xml"))
			def textCheckstyle = incidenciasCheckstyle.join('\n')
			def notaCheckstyle = (20 - incidenciasCheckstyle.size())/20 as Double
			notaCheckstyle = notaCheckstyle > 0.0? notaCheckstyle : 0.0
			
			def nota = (notaTests * 8.0/10.0) + (notaCheckstyle * 2.0/10.0)
	
			def reporte = '=== Reporte completo ===\n'
			reporte += "Nota general: ${nota.trunc(2)}/10.0\n"
			reporte += "\tNota funcional: ${(notaTests*8.0)/10.0}/8.0\n"
			reporte += "\tNota de estilo: ${(notaCheckstyle*2.0)/10.0}/2.0\n"
			reporte += '\n\n******************'
			reporte += '\nAnálisis funcional\n'
			reporte += '******************\n'
			reporte += myTestsFile.text
			reporte += '\n\n******************'
			reporte += "\nAnálisis de estilo\n"
			reporte += '******************\n'
			reporte += textCheckstyle
			
			completeMapWithQualifications(correctionsMap, nota.trunc(2), reporte)
	
		} catch (FileNotFoundException e) {
			e.printStackTrace()
			completeMapWithQualifications(correctionsMap, 0.0, 'Se ha enviado una tarea que no corresponde con la interfaz provista\nAsegúrese de no haberla cambiado desde el momento en que comenzó su tarea')
		}
	
		sleep(20000)
	
		try {
	
			def tareaAlumno = new File(rutaTareaAlumno)
			tareaAlumno.deleteDir()
	
		} catch (FileNotFoundException fe) {
			println "Tarea incorrecta, no se eliminan archivos"
		}
	
		return jsonify(correctionsMap)
	
	}
}
