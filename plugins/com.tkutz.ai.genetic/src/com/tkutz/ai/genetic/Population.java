package com.tkutz.ai.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;


/**
 * A population is a set of {@link Individual}s.
 * 
 * @author kutz
 *
 */
public class Population<T extends Individual> {

	private ArrayList<T> individuals;

	public Population(int populationSize) {
		individuals = new ArrayList<T>(populationSize);
		// fake initialization just to ensure that our collection has enough elements
		for (int i=0; i<populationSize; i++) {
			individuals.add(null);
		}
	}
	
	public Population(Population<T> copy) {
		individuals = new ArrayList<T>(copy.getIndividuals());
	}

	public void setIndividual(int index, T element) {
		individuals.set(index, element);
	}
	
	public void setAllIndividuals(int index, List<T> elements) {
		individuals.addAll(index, elements);
	}

	public T getIndividual(int index) {
		return individuals.get(index);
	}

	public T getFittest() {
		return individuals.stream().reduce(fitnessAccumulator).orElse(null);
	}

	public List<T> getNFittest(int n) {
		return individuals.stream().sorted(fitnessComparator).limit(n).collect(Collectors.toList());
	}

	public int size() {
		return individuals.size();
	}
	
	public List<T> getIndividuals() {
		return Collections.unmodifiableList(individuals);
	}
	
	
	public BinaryOperator<T> fitnessAccumulator = (e1, e2) -> e1.getFitness() > e2.getFitness() ? e1 : e2;
	
	/**
	 * Comparator putting the most fit individual first
	 */
	public Comparator<T> fitnessComparator = (e1, e2) -> {
		if (e1.getFitness() > e2.getFitness())
			return -1;
		if (e1.getFitness() < e2.getFitness())
			return 1;
		else
			return 0;
	};
}