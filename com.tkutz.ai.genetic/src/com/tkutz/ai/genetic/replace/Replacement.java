package com.tkutz.ai.genetic.replace;

import com.tkutz.ai.genetic.Individual;
import com.tkutz.ai.genetic.Population;

public interface Replacement<T extends Individual> {

	Population<T> execute(Population<T> oldPopulation, Population<T> newPopulation);
}
