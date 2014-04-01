package com.example;

public class Grade {
	private String subject;
	private double gradePoints;
	
	public Grade(String subject, double gradePoints) { 
		this.subject = subject;
		this.gradePoints = gradePoints;
	}

	public String getSubject() {
		return this.subject;
	}
	
	public double getGrade() {
		return this.gradePoints;
	}
}

