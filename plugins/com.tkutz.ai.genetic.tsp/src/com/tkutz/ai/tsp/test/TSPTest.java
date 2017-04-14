package com.tkutz.ai.tsp.test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	private static final int EXEC_COUNT = 8; // 8 core processor

	private static PrintWriter resultWriter;
	
	@BeforeClass
	public static void initFileStream() {
		try {
			resultWriter = new PrintWriter(new FileWriter("tspResults_"+LocalDateTime.now()+".log"), true);
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
		TSPLoader.loadFromFile("datasets/att48.tsp", "datasets/att48.opt.tour");
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
		TSPLoader.loadFromFile("datasets/kroD100.tsp", "datasets/kroD100.opt.tour");
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
	public void testTsp225() {
		TSPLoader.loadFromFile("datasets/tsp225.tsp", "datasets/tsp225.opt.tour");
		GAOptions options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.5));
		runGA("tsp225", 120, options);
	}
	
	
	@Test
	public void testPr76() {
		TSPLoader.loadFromFile("datasets/pr76.tsp", "datasets/pr76.opt.tour");
		GAOptions options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.5));
		runGA("pr76", 120, options);
	}
	
	@Test
	public void testCh150() {
		TSPLoader.loadFromFile("datasets/ch150.tsp", "datasets/ch150.opt.tour");
		GAOptions options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.5));
		runGA("ch150", 120, options);
	}
	
	@Test
	public void testXqf131() {
		TSPLoader.loadFromFile("datasets/xqf131.tsp");
		GAOptions options = new GAOptions()
			.enable2Opt()
			.setMutation(new EdgeInversion(0.6));
		runGA("xqf131", 150, options);
	}
	
	private static class ExecutionResult {
		private final long time;
		private final double value;
		private final String info;
		private int genCount;
		
		public ExecutionResult(long time, double value, int genCount, String info) {
			this.time = time;
			this.value = value;
			this.genCount = genCount;
			this.info = info;
		}
		
		public long getTime() {
			return time;
		}
		
		public double getValue() {
			return value;
		}
		
		public String getInfo() {
			return info;
		}
		
		public int getGenCount() {
			return genCount;
		}
	}
	
	protected void runGA(String tspName, final int popSize, GAOptions options) {
		double bestCost = Double.MAX_VALUE;
		double avgCost = 0;
		double worstCost = 0;
		int avgGenCount = 0;
		long avgTime = 0;
		
		GeneticAlgorithm<TSPIndividual> ga = new GeneticTSPSolver(options);
		
		ExecutorService executor = Executors.newFixedThreadPool(EXEC_COUNT);
		Callable<ExecutionResult> executionTask = () -> {
			return execute(ga, popSize);
		};
		List<Future<ExecutionResult>> results = new ArrayList<>();
		
		for (int ex = 0; ex < EXEC_COUNT; ex++) {
			Future<ExecutionResult> future = executor.submit(executionTask);
			results.add(future);
		}
		int index = 0;
		for (Future<ExecutionResult> future : results) {
			try {
				ExecutionResult result = future.get();
				System.out.println("============== EXECUTION "+(++index)+ " =================");
				System.out.println(result.getInfo());
				avgTime = (avgTime * index + result.getTime()) / (index + 1);
				
				if (result.getValue() < bestCost) {
					bestCost = result.getValue();
				}
				if (result.getValue() > worstCost) {
					worstCost = result.getValue();
				}
				avgCost = (avgCost * index + result.getValue()) / (index + 1);
				avgGenCount = (avgGenCount * index + result.getGenCount()) / (index +1);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		executor.shutdown();
		
		double optimalCost = 0;
		if (TSP.getSolution() != null) {
			Tour optimalTour = new Tour(false);
			optimalTour.getCities().clear();
			optimalTour.getCities().addAll(TSP.getSolution());
			optimalCost = optimalTour.getCost();
		}
		
		StringBuilder resultBuilder = new StringBuilder();
		resultBuilder.append(NL);
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
	
	protected ExecutionResult execute(GeneticAlgorithm<TSPIndividual> ga, int popSize) {
		StringBuilder info = new StringBuilder();
		// time measuring
		long startTime = System.currentTimeMillis();
		
		Population<TSPIndividual> pop = new Population<TSPIndividual>(popSize);
		ga.initialize(pop);
		
		boolean hasImproved = true;
		int genCount = 0;
		double lastFittestValue = pop.getFittest().getCost();
		info.append("- Generation "+genCount+": Best Cost = "+lastFittestValue+NL);
		while(hasImproved) {
			
			ga.evolve(pop);
			
			genCount++;
			if ((genCount % TERMINATION_WINDOW) == 0) {
				// check if solution improved over last 30 generations
				double currentFittestValue = pop.getFittest().getCost();
				if (lastFittestValue <= currentFittestValue) {
					hasImproved = false;
				}
				else {
					lastFittestValue = currentFittestValue;
				}
				info.append("- Generation "+genCount+": Best Cost = "+currentFittestValue+NL);
			}
			
		}
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		Tour bestTourExecution = pop.getFittest();
		double bestCostExecution = bestTourExecution.getCost();
		
		return new ExecutionResult(executionTime, bestCostExecution, genCount, info.toString());
	}
	
	

}
