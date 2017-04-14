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
		Population<T> mergedPopulation = new Population<>(oldPopulation);
		mergedPopulation.setAllIndividuals(0, oldPopulation.getNFittest(numberOfElites));
		for (int i = numberOfElites; i < mergedPopulation.size(); i++) {
			if (i < newPopulation.size()) {
				mergedPopulation.setIndividual(i, newPopulation.getIndividual(i));
			}
		}
		return mergedPopulation;
	}
	
	@Override
	public String toString() {
		return numberOfElites+"-Elitism";
	}

}
