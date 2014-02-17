package braid.grader

import groovy.transform.EqualsAndHashCode
import braid.grader.exceptions.ScoreException

@EqualsAndHashCode
class Score {

	Double score
	Double maxScore
	
	public Score(def score, def maxScore) {
		if (score > maxScore) {
			throw new ScoreException('Cannot score more than maxScore')
		}
		if (score < 0) {
			throw new ScoreException('Cannot score negative')
		}
		
		this.score = score
		this.maxScore = maxScore
	}
	
	def plus(Score that) {
		new Score(this.score + that.score, this.maxScore + that.maxScore)
	}
	
	def normalize(Double localMax = 10.0) {
		new Score((this.score / this.maxScore) * localMax, localMax)
	}
	
	def fromRatio(Double ratio) {
		new Score((ratio * this.maxScore), this.maxScore)
	}
	
	@Override
	String toString() {
		"${this.score} / ${this.maxScore}"
	}
	
}
