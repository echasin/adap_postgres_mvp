package com.innvo.drools;

import java.util.List;

import javax.inject.Inject;

import com.innvo.domain.Route;
import com.innvo.repository.RouteRepository;

public class RouteService {
	
	@Inject
	RouteRepository routeRepository;
	
	
	public void Save(Route route){
		routeRepository.save(route);
	}
	
	public List<Route> get(){
		return routeRepository.findAll();
	}
	
	public void test(){
		System.out.println("111111111111122222222222222233333333333");;
	}
	

}
