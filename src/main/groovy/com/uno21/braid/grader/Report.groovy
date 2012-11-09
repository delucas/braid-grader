package com.uno21.braid.grader

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Report {

	def entries = [] as List
	
	void addEntry(Score score, String detail) {
		entries << new TestEntry(score, detail)
	}
	
	Score getTotalScore() {
		return entries.collect { it.score }.inject(new Score(0, 0)) { oldValue, element -> 
			element + oldValue
		}
	}
	
}