package com.tkutz.ai.genetic.crossover;

import com.tkutz.ai.genetic.Individual;

public interface CrossOver<T extends Individual> {

	T execute(T parent1, T parent2);
}
