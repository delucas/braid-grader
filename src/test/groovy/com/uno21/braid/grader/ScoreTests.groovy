package com.uno21.braid.grader;

import static org.junit.Assert.*

import org.junit.Test

import com.uno21.braid.grader.exceptions.ScoreException

class ScoreTests {

	@Test void thatCanScoreWithParams() {
		def score = new Score(0, 0)
	}
	
	@Test(expected=ScoreException.class)
	void thatCannotScoreMoreThanMax() {
		def score = new Score(1, 0)
	}
	
	@Test(expected=ScoreException.class)
	void thatCannotScoreNegative() {
		def score = new Score(-1, 0)
	}
	
	@Test void thatCanAddTwoScores() {
		def score1 = new Score(1, 2)
		def score2 = new Score(2, 3)
		
		def score3 = new Score(3, 5)
		
		assert score3 == score1 + score2
	}
	
	@Test void toStringPrintsNicely() {
		def score = new Score(1, 2)
		assert '1.0 / 2.0' == score.toString()
	}
	
	@Test void thatNormalizedScoreIsOverTen() {
		def score = new Score(1, 2)
		assert 10.0 == score.normalize().maxScore
	}
	
	@Test void thatNormalizedScoreIsCorrect() {
		def score = new Score(1, 2)
		assert 5.0 == score.normalize().score
	}
	
}
