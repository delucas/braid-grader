package com.uno21.braid.grader

import groovy.transform.EqualsAndHashCode;

import com.uno21.braid.grader.exceptions.ScoreException

@EqualsAndHashCode
class TestEntry {

	String title
	Score score
	String detail
	
	public TestEntry(String title, Score score, String detail) {
		this.title = title
		this.score = score
		this.detail = detail
	}
	
}
