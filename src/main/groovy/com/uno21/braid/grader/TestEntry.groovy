package com.uno21.braid.grader

import groovy.transform.EqualsAndHashCode;

import com.uno21.braid.grader.exceptions.ScoreException

@EqualsAndHashCode
class TestEntry {

	Score score
	String detail
	
	public TestEntry(Score score, String detail) {
		this.score = score
		this.detail = detail
	}
	
}
