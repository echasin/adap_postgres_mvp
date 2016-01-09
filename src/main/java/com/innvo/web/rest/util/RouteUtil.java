package com.innvo.web.rest.util;

import com.innvo.domain.Location;

public class RouteUtil {
	
	private long routeId;
	private String routName;
	private String originName;
	private String destinationName;
	private Location originLocation;
	private Location destinationLocation;
	private String segmentName;
	private double averageScore; 
	
	public RouteUtil() {
		super();
	}
	
	
	public RouteUtil(long routeId, String routName, String originName, String destinationName, Location originLocation,
			Location destinationLocation, String segmentName, double averageScore) {
		super();
		this.routeId = routeId;
		this.routName = routName;
		this.originName = originName;
		this.destinationName = destinationName;
		this.originLocation = originLocation;
		this.destinationLocation = destinationLocation;
		this.segmentName = segmentName;
		this.averageScore = averageScore;
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



	public Location getOriginLocation() {
		return originLocation;
	}



	public void setOriginLocation(Location originLocation) {
		this.originLocation = originLocation;
	}



	public Location getDestinationLocation() {
		return destinationLocation;
	}



	public void setDestinationLocation(Location destinationLocation) {
		this.destinationLocation = destinationLocation;
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


	@Override
	public String toString() {
		return "RouteUtil [routeId=" + routeId + ", routName=" + routName + ", originName=" + originName
				+ ", destinationName=" + destinationName + ", originLocation=" + originLocation
				+ ", destinationLocation=" + destinationLocation + ", segmentName=" + segmentName + ", averageScore="
				+ averageScore + "]";
	}



	
}
