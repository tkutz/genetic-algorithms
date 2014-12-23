package com.tkutz.ai.genetic;

import java.util.ArrayList;


/**
 * A population is a set of {@link Individual}s.
 * 
 * @author kutz
 *
 */
public class Population<T extends Individual> {

	// TODO list or better set, e.g. linkedHashSet?
	private ArrayList<T> individuals;

	public Population(int populationSize) {
		individuals = new ArrayList<T>(populationSize);
		// fake initialization just to ensure that our collection has enough elements
		for (int i=0; i<populationSize; i++) {
			individuals.add(null);
		}

		// TODO: do initialization outside where concrete individual type is known
//		if (initialise) {
//			for (int i = 0; i < size(); i++) {
//				T newIndividual = individualType.newInstance();
//				newIndividual.initialize();
//				setIndividual(i, newIndividual);
//			}
//		}
	}

	public void setIndividual(int index, T element) {
		individuals.set(index, element);
	}

	public T getIndividual(int index) {
		return individuals.get(index);
	}

	public T getFittest() {
		T fittest = individuals.get(0);

		for (int i = 1; i < size(); i++) {
			if (fittest.getFitness() <= getIndividual(i).getFitness()) {
				fittest = getIndividual(i);
			}
		}
		return fittest;
	}

	public int size() {
		return individuals.size();
	}
}