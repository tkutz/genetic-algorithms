package com.tkutz.ai.genetic.tsp;

import com.tkutz.ai.genetic.mutation.Mutation;
import com.tkutz.ai.genetic.replace.Elitism;
import com.tkutz.ai.genetic.replace.Replacement;
import com.tkutz.ai.genetic.selection.Selection;
import com.tkutz.ai.genetic.selection.TournamentSelection;
import com.tkutz.ai.genetic.tsp.operators.SwapCities;

public class GAOptions {

	private static final String NL = System.getProperty("line.separator");

	/** Default: 2-opt is disabled */
	protected boolean do2Opt = false;
	
	/** Default: SwapMutation with 10% mutation rate */
	protected Mutation<TSPIndividual> mutation = new SwapCities(0.1);

	/** Default: Tournament selection with tournament size 3 */
	protected Selection<TSPIndividual> selection = new TournamentSelection<TSPIndividual>(3);

	/** Default: Elitism with 1 elite */
	protected Replacement<TSPIndividual> replacement = new Elitism<TSPIndividual>(1);

	public GAOptions enable2Opt() {
		do2Opt = true;
		return this;
	}

	public GAOptions disable2Opt() {
		do2Opt = false;
		return this;
	}
	
	public GAOptions setMutation(final Mutation<TSPIndividual> mutation) {
		this.mutation = mutation;
		return this;
	}
	
	public GAOptions setSelection(final Selection<TSPIndividual> selection) {
		this.selection = selection;
		return this;
	}
	
	public GAOptions setReplacement(final Replacement<TSPIndividual> replacement) {
		this.replacement = replacement;
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Options: "+NL);
		sb.append("- 2-opt: "+(do2Opt?"enabled":"disabled")+NL);
		sb.append("- Selection: "+selection.toString()+NL);
		sb.append("- Mutation: "+mutation.toString()+NL);
		sb.append("- Replacement: "+replacement.toString()+NL);
		return sb.toString();
	}
	
}