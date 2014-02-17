package braid.grader

import groovy.io.FileType
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

import org.apache.commons.io.FileUtils

import braid.grader.parsers.java.checkstyle.CheckstyleParser
import braid.grader.parsers.java.junit.JUnitParser

class JavaGrader {
	def completeMapWithQualifications(def map, def score, def detail) {
		map.put('testScore', score.score)
		map.put('testDetail', detail)
	}
	
	def jsonify(def map) {
		new JsonOutput().toJson(new JsonBuilder().call(map))
	}

	def grade(def json) {
	
		def p = "git clone -b resolucion git@bitbucket.org:tallerweb/${json.nombreTarea}.git grader-${json.nombreTarea}".execute()
		p.waitFor()
		
		def rutaGraderConfiable = "./grader-${json.nombreTarea}"
	
		def correctionsMap = [:]
		correctionsMap << ['solutionId': json.solutionId]
		
		def nombreTemporalCarpeta = 'pull-' + new Date().time
		def rutaTareaAlumno
		
		p = "git clone git@github.com:${json.githubUser}/${json.nombreTarea}.git ${nombreTemporalCarpeta}".execute()
		p.waitFor()
	
		// No existía el repo, o hubo problemas con git
		if (p.exitValue() == 1) {
			completeMapWithQualifications(correctionsMap, new Score(0, 10), p.in.text)
	
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
			completeMapWithQualifications(correctionsMap, new Score(0, 10), 'El repositorio suministrado no es válido')
			return jsonify(correctionsMap)
		}
	
		p = "gradle -p ${rutaTareaAlumno} compileJava".execute()
		p.waitFor()
	
		// No compila el código
		if (p.exitValue() == 1) {
			completeMapWithQualifications(correctionsMap, new Score(0, 10), p.in.text)
	
			return jsonify(correctionsMap)
		}
	
		// Ejecutamos la tarea mvn test
		p = "gradle -p ${rutaTareaAlumno}".execute()
		p.waitFor()
	
		// Tomamos el reporte y lo mostramos
		try {
	
			def junitParser = new JUnitParser()
			def checkstyleParser = new CheckstyleParser()
			
			def junitIssuesMap
			
			def myTestsFile = new File("${rutaTareaAlumno}/build/test-results")
			myTestsFile.eachFile(FileType.FILES) {
				junitIssuesMap = junitParser.parse(it)
				// por el momento todos los tests se encuentran en el mismo archivo
				// pero no sabemos el nombre exacto
			}
			def notaFuncional = junitIssuesMap["score"]
			
			
			def myCheckstyleFile = new File("${rutaTareaAlumno}/build/reports/checkstyle/main.xml")
			def checkstyleIssuesList = checkstyleParser.parse(myCheckstyleFile)
			def notaEstilo = new Score((20 - checkstyleIssuesList.size())>0?((20 - checkstyleIssuesList.size())/10):0, 2)
			
			def nota = notaFuncional + notaEstilo
			
			def reporte = '=== Reporte completo ===\n'
			reporte += "Nota general: ${nota.normalize()}\n"
			reporte += "\tNota funcional: ${notaFuncional}\n"
			reporte += "\tNota de estilo: ${notaEstilo}\n"
			reporte += '\n\n******************'
			reporte += '\nAnálisis funcional\n'
			reporte += '******************\n'
			reporte += reportarIssuesJUnit(junitIssuesMap)
			reporte += '\n\n******************'
			reporte += "\nAnálisis de estilo\n"
			reporte += '******************\n'
			reporte += reportarIssuesCheckstyle(checkstyleIssuesList)
			
			completeMapWithQualifications(correctionsMap, nota, reporte)
	
		} catch (FileNotFoundException e) {
			e.printStackTrace()
			completeMapWithQualifications(correctionsMap, new Score(0, 10), 'Se ha enviado una tarea que no corresponde con la interfaz provista\nAsegúrese de no haberla cambiado desde el momento en que comenzó su tarea')
		}
	
		try {
	
			def tareaAlumno = new File(rutaTareaAlumno)
			//tareaAlumno.deleteDir()
	
		} catch (FileNotFoundException fe) {
			println "Tarea incorrecta, no se eliminan archivos"
		}
	
		return jsonify(correctionsMap)
	
	}

	def reportarIssuesCheckstyle(def lista) {
		lista ? lista.inject("") { acc, val -> acc + val + "\n"} : 'Sin incidencias' 
	}
		
	def reportarIssuesJUnit(def map) {
		String result = ''
		result += map['errors'].inject("") { acc, val -> acc + val + "\n"}
		result += '\n'
		result += map['failures'].inject("") { acc, val -> acc + val + "\n"}
		
		result
	}
}
