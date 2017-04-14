package com.tkutz.ai.genetic.selection;

import java.util.Random;
import java.util.stream.Stream;

import com.tkutz.ai.genetic.Individual;
import com.tkutz.ai.genetic.Population;

public class TournamentSelection<T extends Individual> implements Selection<T> {

	private int tournamentSize;

	public TournamentSelection(int tournamentSize) {
		this.tournamentSize = tournamentSize;
	}

	@Override
	public T execute(Population<T> population) {
		return getTournamentCompetitors(population).reduce(population.fitnessAccumulator).orElse(null);
	}
	
	protected Stream<T> getTournamentCompetitors(Population<T> population) {
		return new Random().ints(tournamentSize, 0, population.size()).mapToObj(n -> population.getIndividual(n));
	}

	@Override
	public String toString() {
		return "Deterministic " + tournamentSize + "-Tournament";
	}
}
