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
		report.addEntry(new Score(1,2), 'some details')
	}
	
	@Test void thatSummarizesScores() {
		report.addEntry(new Score(1,2), 'details 1')
		report.addEntry(new Score(1,2), 'details 2')
		
		assert new Score(2, 4) == report.totalScore
	}
	
	@Test void thatSummarizesThreeScores() {
		report.addEntry(new Score(1,2), 'details 1')
		report.addEntry(new Score(1,2), 'details 2')
		report.addEntry(new Score(1,2), 'details 3')
		
		assert new Score(3, 6) == report.totalScore
	}
	
	@Test void thatCanGetTestEntry() {
		report.addEntry(new Score(1,2), 'details 1')
		report.addEntry(new Score(1,2), 'details 2')
		
		assert new TestEntry(new Score(1,2), 'details 1') == report.entries.getAt(0)
	}
	
}
