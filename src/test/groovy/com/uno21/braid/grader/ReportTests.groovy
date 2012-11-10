package com.uno21.braid.grader;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ReportTests {

	def report
	
	@Before void setUp() {
		report = new Report()
	}
	
	@Test void thatCanAddEntry() {
		report.addEntry('Test', new Score(1,2), 'some details')
	}
	
	@Test void thatSummarizesScores() {
		report.addEntry('Test', new Score(1,2), 'details 1')
		report.addEntry('Test', new Score(1,2), 'details 2')
		
		assert new Score(2, 4) == report.totalScore
	}
	
	@Test void thatSummarizesThreeScores() {
		report.addEntry('Test', new Score(1,2), 'details 1')
		report.addEntry('Test', new Score(1,2), 'details 2')
		report.addEntry('Test', new Score(1,2), 'details 3')
		
		assert new Score(3, 6) == report.totalScore
	}
	
	@Test void thatCanGetTestEntry() {
		report.addEntry('Test', new Score(1,2), 'details 1')
		report.addEntry('Test', new Score(1,2), 'details 2')
		
		assert new TestEntry('Test', new Score(1,2), 'details 1') == report.entries.getAt(0)
	}
	
	@Test void thatATestEntryHasTitle() {
		TestEntry entry = new TestEntry('Title', new Score(1,2), 'details 1')
		
		assert 'Title' == entry.title
	}
	
	@Test void testThatAReportCanGivePerfectSummary() {
		report.addEntry('Funcional', new Score(8.0,8.0), 'details 1')
		report.addEntry('Estilo', new Score(2.0,2.0), 'details 2')
		
		assert 'Nota: 10.0 / 10.0\n\tNota Funcional: 8.0 / 8.0\n\tNota Estilo: 2.0 / 2.0' ==
			report.summary
	}
	
	@Test void testThatAReportCanGiveNonPerfectSummary() {
		report.addEntry('Funcional', new Score(7.0,8.0), 'details 1')
		report.addEntry('Estilo', new Score(1.0,2.0), 'details 2')
		
		assert 'Nota: 8.0 / 10.0\n\tNota Funcional: 7.0 / 8.0\n\tNota Estilo: 1.0 / 2.0' ==
			report.summary
	}
	
	@Test void testThatAReportCanGiveDetails() {
		report.addEntry('Funcional', new Score(7.0,8.0), 'details 1')
		report.addEntry('Estilo', new Score(1.0,2.0), 'details 2')
		
		def expectedDetails = '''===========================
Detalles del Test Funcional
===========================

details 1

========================
Detalles del Test Estilo
========================

details 2
'''
		
		assert expectedDetails == report.details
	}
	
}
