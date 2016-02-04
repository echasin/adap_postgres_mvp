package com.innvo.drools;

import java.time.ZonedDateTime;

import com.innvo.domain.Objcategory;
import com.innvo.domain.Objclassification;
import com.innvo.domain.Objrecordtype;
import com.innvo.domain.Route;
import com.innvo.domain.enumeration.Status;


public class Todo {
	
	//private double value;
	
	private long runId;
	
	//private String details;
	
	private Status  status;
	
	private String Lastmodifiedby;
	
	private ZonedDateTime lastmodifieddate;
	
	private String domain;
	
	//private Objrecordtype objrecordtype;
	
	//private Objclassification objclassification;
	
	//private Objcategory objcategory;
		
	private Route route;
	
	private String ruleName;

	
	public Todo() {
		super();
	}



	//public double getValue() {
	//	return value;
	//}

	//public void setValue(double value) {
	//	this.value = value;
	//}

	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	//public String getDetails() {
	//	return details;
	//}

	//public void setDetails(String details) {
	//	this.details = details;
	//}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getLastmodifiedby() {
		return Lastmodifiedby;
	}

	public void setLastmodifiedby(String lastmodifiedby) {
		Lastmodifiedby = lastmodifiedby;
	}

	public ZonedDateTime getLastmodifieddate() {
		return lastmodifieddate;
	}

	public void setLastmodifieddate(ZonedDateTime lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	
	//public Objrecordtype getObjrecordtype() {
	//	return objrecordtype;
	//}



	//public void setObjrecordtype(Objrecordtype objrecordtype) {
	//	this.objrecordtype = objrecordtype;
	//}



	//public Objclassification getObjclassification() {
	//	return objclassification;
	//}



	//public void setObjclassification(Objclassification objclassification) {
	//	this.objclassification = objclassification;
	//}



	//public Objcategory getObjcategory() {
	//	return objcategory;
	//}



	//public void setObjcategory(Objcategory objcategory) {
	//	this.objcategory = objcategory;
	//}



	public Route getRoute() {
		return route;
	}



	public void setRoute(Route route) {
		this.route = route;
	}



	public String getRuleName() {
		return ruleName;
	}



	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	

	
}