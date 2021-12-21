package com.tkutz.ai.genetic;

import com.tkutz.ai.genetic.crossover.CrossOver;
import com.tkutz.ai.genetic.mutation.Mutation;
import com.tkutz.ai.genetic.replace.Replacement;
import com.tkutz.ai.genetic.selection.Selection;

public abstract class GA<T extends Individual> {

	protected abstract Selection<T> getSelection();
	protected abstract CrossOver<T> getCrossOver();
	protected abstract Mutation<T> getMutation();
	protected abstract Replacement<T> getReplacement();
	
	public abstract void initialize(Population<T> population);
	
	/**
	 * Calculates the next generation from the current one. Thereby, the given population will be replaced
	 * by the next generation.
	 * 
	 * @param oldPopulation current generation
	 */
	public void evolve(Population<T> oldPopulation) {
		Population<T> newPopulation = new Population<T>(oldPopulation.size());

		for (int i = 0; i < newPopulation.size(); i++) {
			// selection
			T parent1 = getSelection().execute(oldPopulation);
			T parent2 = getSelection().execute(oldPopulation);
			// crossover
			T offspring = getCrossOver().execute(parent1, parent2);
			// mutation
			getMutation().execute(offspring);
			
			newPopulation.setIndividual(i, offspring);
		}
		// replacement
		getReplacement().execute(oldPopulation, newPopulation);
	}
}