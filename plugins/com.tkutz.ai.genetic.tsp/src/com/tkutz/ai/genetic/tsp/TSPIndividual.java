package com.tkutz.ai.genetic.tsp;

import com.tkutz.ai.genetic.GA;
import com.tkutz.ai.genetic.Individual;
import com.tkutz.ai.tsp.Tour;

/**
 * Adapter class adapting a {@link Tour} as defined in the TSP to an {@link Individual} as used by an {@link GA}.
 * 
 * @author kutz
 *
 */
public class TSPIndividual extends Tour implements Individual {

	public TSPIndividual(boolean fillFromTsp) {
		super(fillFromTsp);
	}

	@Override
	public Individual create() {
		return new TSPIndividual(true);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	

}
