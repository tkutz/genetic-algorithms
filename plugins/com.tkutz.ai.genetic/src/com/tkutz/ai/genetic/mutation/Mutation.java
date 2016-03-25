package com.tkutz.ai.genetic.mutation;

import com.tkutz.ai.genetic.Individual;

public interface Mutation<T extends Individual> {

	void execute(T individual);
}
