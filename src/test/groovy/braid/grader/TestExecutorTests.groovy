package braid.grader;

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor;

import org.junit.Before
import org.junit.Test

import braid.grader.TestExecutor;

class TestExecutorTests {

	TestExecutor executor
	
	@Before void setUp() {
		executor = new TestExecutor()
	}
	
	@Test void thatTestExecutes() {
		executor.run()
	}
	
}
