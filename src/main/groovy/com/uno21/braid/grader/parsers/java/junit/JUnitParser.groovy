package com.uno21.braid.grader.parsers.java.junit

import com.uno21.braid.grader.Score;

class JUnitParser {

	def parse(def xml) {

		def myXml = new XmlParser().parse(xml)

		def issues = [:] as Map
		issues['errors'] = []
		issues['failures'] = []

		myXml.testcase.each { test ->

			def errors = [] as List
			def failures = [] as List

			test.error.each {
				errors << new JUnitIssue(
						classname: test.attributes()['classname'],
						test: test.attributes()['name'],
						type: 'error',
						message: it.attributes()['message'],
						details: it.value()[0]
						)
			}

			test.failure.each {
				failures << new JUnitIssue(
						classname: test.attributes()['classname'],
						test: test.attributes()['name'],
						type: 'failure',
						message: it.attributes()['message'],
						details: it.value()[0]
						)
			}

			issues['errors'].addAll(errors)
			issues['failures'].addAll(failures)

		}
		
		def totalTests = myXml.attributes()["tests"] as Integer
		def totalFailures = myXml.attributes()["failures"] as Integer
		def totalErrors = myXml.attributes()["errors"] as Integer

		def score = new Score(totalTests - (totalFailures+totalErrors), totalTests)
		issues.put('score', score.normalize(8))


		return issues
	}
}
