package com.uno21.braid.grader

import groovy.transform.EqualsAndHashCode;

import com.uno21.braid.grader.exceptions.ScoreException

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
	
	def normalize() {
		new Score((this.score * 10.0) / this.maxScore, 10.0)
	}
	
	@Override
	String toString() {
		"${this.score} / ${this.maxScore}"
	}
	
}
