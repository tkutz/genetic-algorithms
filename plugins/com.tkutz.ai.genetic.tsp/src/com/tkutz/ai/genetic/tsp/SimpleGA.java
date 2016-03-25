package com.tkutz.ai.genetic.tsp;

import com.tkutz.ai.genetic.GeneticAlgorithm;
import com.tkutz.ai.genetic.Population;
import com.tkutz.ai.genetic.crossover.CrossOver;
import com.tkutz.ai.genetic.mutation.Mutation;
import com.tkutz.ai.genetic.replace.Elitism;
import com.tkutz.ai.genetic.replace.Replacement;
import com.tkutz.ai.genetic.selection.Selection;
import com.tkutz.ai.genetic.selection.TournamentSelection;
import com.tkutz.ai.genetic.tsp.operators.SwapCities;
import com.tkutz.ai.genetic.tsp.operators.TwoPointCrossOver;

public class SimpleGA extends GeneticAlgorithm<TSPIndividual> {

	@Override
	protected Mutation<TSPIndividual> getMutation() {
		return new SwapCities(0.015);
	}

	@Override
	protected CrossOver<TSPIndividual> getCrossOver() {
		return new TwoPointCrossOver();
	}

	@Override
	protected Selection<TSPIndividual> getSelection() {
		return new TournamentSelection<TSPIndividual>(5);
	}

	@Override
	protected Replacement<TSPIndividual> getReplacement() {
		return new Elitism<TSPIndividual>();
	}

	@Override
	public void initialize(Population<TSPIndividual> population) {
		for (int i = 0; i<population.size(); i++) {
			population.setIndividual(i, new TSPIndividual(true));
		}
	}

}
