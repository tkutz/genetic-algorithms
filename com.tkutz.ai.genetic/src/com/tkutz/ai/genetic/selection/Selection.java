package com.tkutz.ai.genetic.selection;

import com.tkutz.ai.genetic.Individual;
import com.tkutz.ai.genetic.Population;

public interface Selection<T extends Individual> {

	T execute(Population<T> population);
}
