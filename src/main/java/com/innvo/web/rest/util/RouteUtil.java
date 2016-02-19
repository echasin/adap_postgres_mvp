package com.innvo.web.rest.util;

import java.util.List;

import com.innvo.domain.Asset;
import com.innvo.domain.Location;

public class RouteUtil {
	
	private long routeId;
	private String routName;
	private String originName;
	private String destinationName;
	private String segmentName;
	private double averageScore; 
	private List<Location> originLocations;
	private List<Location> destinationLocations;
	private List<String> originNames;
	private List<String> destinationNames;
	public RouteUtil() {
		super();
	}
	
	


	public RouteUtil(long routeId, String routName, String originName, String destinationName, String segmentName,
			double averageScore, List<Location> originLocations, List<Location> destinationLocations) {
		super();
		this.routeId = routeId;
		this.routName = routName;
		this.originName = originName;
		this.destinationName = destinationName;
		this.segmentName = segmentName;
		this.averageScore = averageScore;
		this.originLocations = originLocations;
		this.destinationLocations = destinationLocations;
	}




	public long getRouteId() {
		return routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

	public String getRoutName() {
		return routName;
	}

	public void setRoutName(String routName) {
		this.routName = routName;
	}



	public String getOriginName() {
		return originName;
	}



	public void setOriginName(String originName) {
		this.originName = originName;
	}



	public String getDestinationName() {
		return destinationName;
	}



	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}



	public String getSegmentName() {
		return segmentName;
	}



	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}
	
	
	public double getAverageScore() {
		return averageScore;
	}


	public void setAverageScore(double averageScore) {
		this.averageScore = averageScore;
	}


	public List<Location> getOriginLocations() {
		return originLocations;
	}


	public void setOriginLocations(List<Location> originLocations) {
		this.originLocations = originLocations;
	}


	public List<Location> getDestinationLocations() {
		return destinationLocations;
	}


	public void setDestinationLocations(List<Location> destinationLocations) {
		this.destinationLocations = destinationLocations;
	}

	


	public List<String> getOriginNames() {
		return originNames;
	}




	public void setOriginNames(List<String> originNames) {
		this.originNames = originNames;
	}




	public List<String> getDestinationNames() {
		return destinationNames;
	}




	public void setDestinationNames(List<String> destinationNames) {
		this.destinationNames = destinationNames;
	}




	@Override
	public String toString() {
		return "RouteUtil [routeId=" + routeId + ", routName=" + routName + ", originName=" + originName
				+ ", destinationName=" + destinationName + ", segmentName=" + segmentName + ", averageScore="
				+ averageScore + ", originLocations=" + originLocations + ", destinationLocations="
				+ destinationLocations + "]";
	}


	

	
	
}
