package braid.grader.parsers.java.checkstyle

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class CheckstyleIssue {
	
	String file
	Integer line
	String message
	
	@Override
	public String toString() {
		"${file}:${line} -> ${message}"
	}
	
}
