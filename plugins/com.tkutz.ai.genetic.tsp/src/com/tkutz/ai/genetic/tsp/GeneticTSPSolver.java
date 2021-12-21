package com.tkutz.ai.genetic.tsp;

import com.tkutz.ai.genetic.GA;
import com.tkutz.ai.genetic.Population;
import com.tkutz.ai.genetic.crossover.CrossOver;
import com.tkutz.ai.genetic.mutation.Mutation;
import com.tkutz.ai.genetic.replace.Replacement;
import com.tkutz.ai.genetic.selection.Selection;
import com.tkutz.ai.genetic.tsp.operators.SCX;

public class GeneticTSPSolver extends GA<TSPIndividual> {

	private final GAOptions options;
	
	public GeneticTSPSolver(final GAOptions options) {
		this.options = options;
	}
	
	public GeneticTSPSolver() {
		this.options = new GAOptions();
	}

	@Override
	protected Mutation<TSPIndividual> getMutation() {
		return options.mutation;
	}

	@Override
	protected CrossOver<TSPIndividual> getCrossOver() {
		return new SCX(options.do2Opt);
	}

	@Override
	protected Selection<TSPIndividual> getSelection() {
		return options.selection;
	}

	@Override
	protected Replacement<TSPIndividual> getReplacement() {
		return options.replacement;
	}

	@Override
	public void initialize(Population<TSPIndividual> population) {
		for (int i = 0; i < population.size(); i++) {
			population.setIndividual(i, new TSPIndividual(true));
		}
	}
}
