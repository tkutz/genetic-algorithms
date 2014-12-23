package com.tkutz.ai.tsp.test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tkutz.ai.genetic.GeneticAlgorithm;
import com.tkutz.ai.genetic.Population;
import com.tkutz.ai.genetic.tsp.GAOptions;
import com.tkutz.ai.genetic.tsp.GeneticTSPSolver;
import com.tkutz.ai.genetic.tsp.TSPIndividual;
import com.tkutz.ai.genetic.tsp.operators.EdgeInversion;
import com.tkutz.ai.genetic.tsp.operators.SwapCities;
import com.tkutz.ai.tsp.TSP;
import com.tkutz.ai.tsp.TSPLoader;
import com.tkutz.ai.tsp.Tour;

public class TSPTest {

	private static final int TERMINATION_WINDOW = 30;
	private static final String NL = System.getProperty("line.separator");
	private static final int EXEC_COUNT = 10;

	private static PrintWriter resultWriter;
	
	@BeforeClass
	public static void initFileStream() {
		try {
			resultWriter = new PrintWriter(new FileWriter("tspResults_"+System.currentTimeMillis()+".log"), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void closeFileStream() {
		resultWriter.close();
	}
	
	@Test
	public void testAtt48() {
		TSPSampleInitializer.initAtt48();
		GAOptions options = new GAOptions()
			.enable2Opt()
			.setMutation(new SwapCities(0.015));
		runGA("att48", 120, options);
		
		options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.10));
		runGA("att48", 120, options);
		
		options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.50));
		runGA("att48", 120, options);
	}

	@Test
	public void testKroD100() {
		TSPSampleInitializer.initKroD100();
		GAOptions options = new GAOptions()
			.enable2Opt()
			.setMutation(new SwapCities(0.015));
		runGA("kroD100", 120, options);
		
		options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.10));
		runGA("kroD100", 120, options);
		
		options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.50));
		runGA("kroD100", 120, options);
	}
	
	@Test
	public void testPr76() {
		TSPSampleInitializer.initPr76();
		GAOptions options = new GAOptions()
			.enable2Opt()
			.setMutation(new SwapCities(0.015));
		runGA("pr76", 120, options);
		
		options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.10));
		runGA("pr76", 120, options);
		
		options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.50));
		runGA("pr76", 120, options);
	}
	
	@Test
	public void testTsp225() {
		TSPLoader.loadFromFile("datasets/tsp225.tsp", "datasets/tsp225.opt.tour");
		GAOptions options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.5));
		runGA("tsp225", 120, options);
	}
	
	@Test
	public void testXqf131() {
		TSPLoader.loadFromFile("datasets/xqf131.tsp");
		GAOptions options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.6));
		runGA("xqf131", 150, options);
	}
	
	
	protected void runGA(String tspName, final int popSize, GAOptions options) {
		double bestCost = Double.MAX_VALUE;
		double avgCost = 0;
		double worstCost = 0;
		int avgGenCount = 0;
		long avgTime = 0;
		
		GeneticAlgorithm<TSPIndividual> ga = new GeneticTSPSolver(options);
		
		for (int ex = 0; ex < EXEC_COUNT; ex++) {
			
			System.out.println("============== EXECUTION "+(ex+1)+ " =================");
			
			// time measuring
			long startTime = System.currentTimeMillis();
			
			Population<TSPIndividual> pop = new Population<TSPIndividual>(popSize);
			ga.initialize(pop);
			
			boolean hasImproved = true;
			int genCount = 0;
			double lastFittestValue = pop.getFittest().getCost();
			System.out.println("- Generation "+genCount+": Best Cost = "+lastFittestValue);
			while(hasImproved) {
				
				ga.evolve(pop);
				
				genCount++;
				if ((genCount % TERMINATION_WINDOW) == 0) {
					// check if solution improved over last 100 generations
					double currentFittestValue = pop.getFittest().getCost();
					if (lastFittestValue <= currentFittestValue) {
						hasImproved = false;
					}
					else {
						lastFittestValue = currentFittestValue;
					}
					System.out.println("- Generation "+genCount+": Best Cost = "+currentFittestValue);
				}
				
			}
			long endTime = System.currentTimeMillis();
			avgTime = (avgTime * ex + (endTime-startTime)) / (ex + 1);
			
			Tour bestTourExecution = pop.getFittest();
			double bestCostExecution = bestTourExecution.getCost();
			if (bestCostExecution < bestCost) {
				bestCost = bestCostExecution;
			}
			if (bestCostExecution > worstCost) {
				worstCost = bestCostExecution;
			}
			avgCost = (avgCost * ex + bestCostExecution) / (ex + 1);
			avgGenCount = (avgGenCount * ex + genCount) / (ex +1);
		}
		double optimalCost = 0;
		if (TSP.getSolution() != null) {
			Tour optimalTour = new Tour(false);
			optimalTour.getCities().clear();
			optimalTour.getCities().addAll(TSP.getSolution());
			optimalCost = optimalTour.getCost();
		}
		
		StringBuilder resultBuilder = new StringBuilder();
		resultBuilder.append("Solving TSP '"+tspName+"' using SCX:" + NL);
		resultBuilder.append(options.toString()+NL);
		resultBuilder.append("- population size: \t"+popSize + NL);
		resultBuilder.append(NL);
		resultBuilder.append("Optimal Cost:\t\t" + optimalCost + NL);
		resultBuilder.append("Best Cost:\t\t\t" + bestCost + " (+"+String.format("%.2f", ((double)bestCost/optimalCost -1)*100)+"%)" + NL);
		resultBuilder.append("Worst Cost:\t\t\t" + worstCost + " (+"+String.format("%.2f", ((double)worstCost/optimalCost -1)*100)+"%)"  + NL);
		resultBuilder.append("Average Cost:\t\t" + avgCost + " (+"+String.format("%.2f", ((double)avgCost/optimalCost -1)*100)+"%)"  + NL);
		resultBuilder.append(NL);
		resultBuilder.append("Avg #generations:\t"+avgGenCount + NL);
		resultBuilder.append("Avg Time:\t\t\t"+avgTime+"ms" + NL);
		resultBuilder.append("--------------------------------------------------" + NL+NL);
		System.out.println(resultBuilder.toString());
		
		resultWriter.append(resultBuilder.toString());
	}

}
