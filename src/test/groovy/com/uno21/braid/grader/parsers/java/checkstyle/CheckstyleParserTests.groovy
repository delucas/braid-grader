package com.uno21.braid.grader.parsers.java.checkstyle

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import com.uno21.braid.grader.parsers.java.checkstyle.CheckstyleIssue;
import com.uno21.braid.grader.parsers.java.checkstyle.CheckstyleParser;

class CheckstyleParserTests {

	CheckstyleParser parser
	
	@Before void setUp() {
		parser = new CheckstyleParser()
	}
	
	String loadXML() {
		def myXML = this.getClass().getResource('/checkstyle-result.xml')
		myXML
	}
	
	@Test void thatCanParse() {
		String xml = loadXML()
		parser.parse(xml)
	}
	
	@Test void thatCheckstyleIssueIsWellFormed() {
		CheckstyleIssue issue = new CheckstyleIssue(
			file: 'src/main/java/ar/edu/unlam/tallerweb/ecuaciones/Ecuacion.java',
			line: 16,
			message: 'Redundant \'public\' modifier.')
		
		String xml = loadXML()
		def issues = parser.parse(xml)

		assert issue == issues[0]
		
	}
	
	@Test void thatParserTakesAllIssues() {
		String xml = loadXML()
		def issues = parser.parse(xml)
		
		assert 32 == issues.size()
	}
	
}
