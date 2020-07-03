package com.covid.spchacker.dto;

import java.sql.Date;

public class PatientsDetails extends UserDetails{
	
	private  Date admitDate;
	
	private Date releaseDate;
	
	private String symptoms;
	
	private int symptomsfrom;

	public Date getAdmitDate() {
		return admitDate;
	}

	public void setAdmitDate(Date admitDate) {
		this.admitDate = admitDate;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public int getSymptomsfrom() {
		return symptomsfrom;
	}

	public void setSymptomsfrom(int symptomsfrom) {
		this.symptomsfrom = symptomsfrom;
	}
	
	

}
