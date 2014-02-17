package braid.grader

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Report {

	def entries = [] as List
	
	void addEntry(String title, Score score, String detail) {
		entries << new TestEntry(title, score, detail)
	}
	
	Score getTotalScore() {
		return entries.collect { it.score }.inject(new Score(0, 0)) { oldValue, element -> 
			element + oldValue
		}
	}
	
	String getSummary() {
		String report = 'Nota: ' + this.totalScore.normalize()
		entries.each { entry ->
			report += "\n\tNota ${entry.title}: ${entry.score}"
		} 
		
		report
	}
	
	String getDetails() {
		
		String details = ''
		
		entries.each { entry ->
			String title = "Detalles del Test ${entry.title}"
			String separator = '=' * title.length()
			
			details += separator + '\n'
			details += title + '\n'
			details += separator + '\n\n'
			details += entry.detail + '\n\n'
		}
		
		details[0..-2]
	}
	
}
