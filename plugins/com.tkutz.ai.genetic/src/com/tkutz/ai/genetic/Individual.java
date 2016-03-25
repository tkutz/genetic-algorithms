package com.tkutz.ai.genetic;

public interface Individual {

	/**
	 * Factory method to retrieve a new individual.
	 * 
	 * @return
	 */
	Individual create();

	/**
	 * Initializes this individual with problem specific values, so that it gets
	 * a valid solution for the problem.
	 */
	void initialize();

	/**
	 * Returns the fitness value of this individual.
	 * @return
	 */
	double getFitness();
}
