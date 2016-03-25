package com.tkutz.ai.genetic.tsp.operators;

import com.tkutz.ai.genetic.crossover.CrossOver;
import com.tkutz.ai.genetic.tsp.TSPIndividual;

public class TwoPointCrossOver implements CrossOver<TSPIndividual> {

	@Override
	public TSPIndividual execute(TSPIndividual parent1, TSPIndividual parent2) {
		// Create new child tour
		TSPIndividual child = new TSPIndividual(false);

		// Get start and end sub tour positions for parent1's tour
		int startPos = (int) (Math.random() * parent1.getSize());
		int endPos = (int) (Math.random() * parent1.getSize());

		// Loop and add the sub tour from parent1 to our child
		for (int i = 0; i < child.getSize(); i++) {
			// If our start position is less than the end position
			if (startPos < endPos && i > startPos && i < endPos) {
				child.setCity(i, parent1.getCity(i));
			} // If our start position is larger
			else if (startPos > endPos) {
				if (!(i < startPos && i > endPos)) {
					child.setCity(i, parent1.getCity(i));
				}
			}
		}

		// Loop through parent2's city tour
		for (int i = 0; i < parent2.getSize(); i++) {
			// If child doesn't have the city add it
			if (!child.contains(parent2.getCity(i))) {
				// Loop to find a spare position in the child's tour
				for (int ii = 0; ii < child.getSize(); ii++) {
					// Spare position found, add city
					if (child.getCity(ii) == null) {
						child.setCity(ii, parent2.getCity(i));
						break;
					}
				}
			}
		}
		return child;
	}


}
