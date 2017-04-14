package com.tkutz.ai.genetic.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.tkutz.ai.genetic.Individual;
import com.tkutz.ai.genetic.Population;
import com.tkutz.ai.genetic.replace.Elitism;

public class ElitismTest {

	private Population<TestIndividual> oldPopulation;
	private Population<TestIndividual> newPopulation;

	class TestIndividual implements Individual {

		double fitness;

		public TestIndividual(double fitness) {
			this.fitness = fitness;
		}

		public TestIndividual() {
			this.fitness = new Random().nextDouble();
		}

		@Override
		public Individual create() {
			return this;
		}

		@Override
		public void initialize() {
		}

		@Override
		public double getFitness() {
			return fitness;
		}
	}

	@Before
	public void setup() {
		int populationSize = 10;
		oldPopulation = new Population<>(populationSize);
		for (int i = 0; i < populationSize; i++) {
			oldPopulation.setIndividual(i, new TestIndividual());
		}
		newPopulation = new Population<>(populationSize);
		for (int i = 0; i < populationSize; i++) {
			newPopulation.setIndividual(i, new TestIndividual());
		}
	}

	@Test
	public void testOneElite() {
		doTestElites(1);
	}

	@Test
	public void testThreeElite() {
		doTestElites(3);
	}
	
	@Test
	public void testAllElite() {
		doTestElites(10);
	}
	
	@Test
	public void testNoElite() {
		doTestElites(0);
	}

	private void doTestElites(int numberOfElites) {
		Elitism<TestIndividual> elitism = new Elitism<>(numberOfElites);
		
		Population<TestIndividual> mergedPopulation = new Population<>(oldPopulation);
		elitism.execute(mergedPopulation, newPopulation);
		
		List<TestIndividual> nFittest = oldPopulation.getNFittest(numberOfElites);

		// check all n-fittest of old population are retained in result
		List<Integer> eliteIdxs = new ArrayList<>(numberOfElites);
		for (TestIndividual fittest : nFittest) {
			int eliteIdx = mergedPopulation.getIndividuals().indexOf(fittest);
			assertTrue(eliteIdx > -1);
			eliteIdxs.add(eliteIdx);
		}
		// check all other in result are from new population
		for (int i = 0; i < mergedPopulation.size() && !eliteIdxs.contains(i); i++) {
			assertTrue(newPopulation.getIndividuals().contains(mergedPopulation.getIndividual(i)));
		}
	}
}
