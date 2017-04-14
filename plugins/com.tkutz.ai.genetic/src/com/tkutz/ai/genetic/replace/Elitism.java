package com.tkutz.ai.genetic.replace;

import java.util.List;

import com.tkutz.ai.genetic.Individual;
import com.tkutz.ai.genetic.Population;

public class Elitism<T extends Individual> implements Replacement<T> {

	private final int numberOfElites;

	public Elitism(int numberOfElites) {
		this.numberOfElites = numberOfElites;
	}
	
	public Elitism() {
		this(1);
	}
	
	@Override
	public void execute(Population<T> oldPopulation, Population<T> newPopulation) {
		List<T> elites = oldPopulation.getNFittest(numberOfElites);
		oldPopulation.setAllIndividuals(0, elites);
		for (int i = numberOfElites; i < oldPopulation.size(); i++) {
			if (i < newPopulation.size()) {
				oldPopulation.setIndividual(i, newPopulation.getIndividual(i));
			}
		}
	}
	
	@Override
	public String toString() {
		return numberOfElites+"-Elitism";
	}

}
