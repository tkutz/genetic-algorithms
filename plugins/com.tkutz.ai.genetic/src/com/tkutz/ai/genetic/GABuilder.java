package com.tkutz.ai.genetic;

import com.tkutz.ai.genetic.crossover.CrossOver;
import com.tkutz.ai.genetic.mutation.Mutation;
import com.tkutz.ai.genetic.replace.Replacement;
import com.tkutz.ai.genetic.selection.Selection;

public class GABuilder {

	private static final String NL = System.getProperty("line.separator");
	
	protected Mutation<Individual> mutation;
	protected Selection<Individual> selection;
	protected Replacement<Individual> replacement;
	protected CrossOver<Individual> crossOver;

	public GABuilder setMutation(final Mutation<Individual> mutation) {
		this.mutation = mutation;
		return this;
	}

	public GABuilder setSelection(final Selection<Individual> selection) {
		this.selection = selection;
		return this;
	}

	public GABuilder setReplacement(final Replacement<Individual> replacement) {
		this.replacement = replacement;
		return this;
	}

	public GABuilder setCrossOver(final CrossOver<Individual> crossOver) {
		this.crossOver = crossOver;
		return this;
	}
	
	public GA<? extends Individual> build() {
		return new GA(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("GA Configuration: " + NL);
		sb.append("- Selection: " + selection.toString() + NL);
		sb.append("- Crossover: " + crossOver.toString() + NL);
		sb.append("- Mutation: " + mutation.toString() + NL);
		sb.append("- Replacement: " + replacement.toString() + NL);
		return sb.toString();
	}
}
