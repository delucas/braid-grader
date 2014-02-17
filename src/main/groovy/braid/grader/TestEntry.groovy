package braid.grader

import groovy.transform.EqualsAndHashCode

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
