package com.tkutz.ai.genetic.tsp.operators;

import com.tkutz.ai.genetic.mutation.Mutation;
import com.tkutz.ai.genetic.tsp.TSPIndividual;
import com.tkutz.ai.tsp.TourUtils;

/**
 * Selects two random cities of a given tour and reverses the path between them.
 * This is done with a probability given by {@link #mutationRate}.
 *
 */
public class EdgeInversion implements Mutation<TSPIndividual> {

	private double mutationRate;

	public EdgeInversion(double mutationRate) {
		this.mutationRate = mutationRate;
	}
	
	@Override
	public void execute(TSPIndividual tour) {
		if (Math.random() < mutationRate) {
			int pos1 = (int) (tour.getSize() * Math.random());
			int pos2 = pos1;
			while (pos2==pos1) {
				pos2 = (int) (tour.getSize() * Math.random());
			}
			TourUtils.reverseEdges(tour, pos1, pos2);
		}
	}
	
	@Override
	public String toString() {
		return "Edge Inversion ("+mutationRate+")";
	}

}
