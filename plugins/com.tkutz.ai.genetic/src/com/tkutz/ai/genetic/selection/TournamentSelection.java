package com.tkutz.ai.genetic.selection;

import java.util.Random;

import com.tkutz.ai.genetic.Individual;
import com.tkutz.ai.genetic.Population;

public class TournamentSelection<T extends Individual> implements Selection<T> {

	private int tournamentSize;

	public TournamentSelection(int tournamentSize) {
		this.tournamentSize = tournamentSize;
	}
	
	@Override
	public T execute(Population<T> population) {
		Population<T> tournament = new Population<T>(tournamentSize);
		for (int i = 0; i < tournamentSize; i++) {
			int randomId = (new Random()).nextInt(population.size());
			tournament.setIndividual(i, population.getIndividual(randomId));
		}
		return tournament.getFittest();
	}

	@Override
	public String toString() {
		return "Deterministic "+tournamentSize+"-Tournament";
	}
}
