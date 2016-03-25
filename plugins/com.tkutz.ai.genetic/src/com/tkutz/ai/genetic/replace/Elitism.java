package com.tkutz.ai.genetic.replace;

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
	public Population<T> execute(Population<T> oldPopulation, Population<T> newPopulation) {
		oldPopulation.setIndividual(0, oldPopulation.getFittest());
		for (int i = numberOfElites; i < oldPopulation.size(); i++) {
			if (i < newPopulation.size()) {
				oldPopulation.setIndividual(i, newPopulation.getIndividual(i));
			}
		}
		return oldPopulation;
	}
	
	@Override
	public String toString() {
		return numberOfElites+"-Elitism";
	}

}
