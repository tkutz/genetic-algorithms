package com.tkutz.ai.tsp.app;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tkutz.ai.genetic.GeneticAlgorithm;
import com.tkutz.ai.genetic.Population;
import com.tkutz.ai.genetic.tsp.GAOptions;
import com.tkutz.ai.genetic.tsp.GeneticTSPSolver;
import com.tkutz.ai.genetic.tsp.TSPIndividual;
import com.tkutz.ai.genetic.tsp.operators.EdgeInversion;
import com.tkutz.ai.tsp.City;
import com.tkutz.ai.tsp.TSP;
import com.tkutz.ai.tsp.TSPLoader;
import com.tkutz.ai.tsp.Tour;

public class TSPApplication {

	private static final String NL = System.getProperty("line.separator");
	private static final int PROBLEM_WIDTH = 1000;
	private static final int PROBLEM_HEIGHT = 700;
	private static Text resultText;
	private static Text cityCountText;
	private static TSPCanvas canvas;
	private static Text generationCount;
	private static Text executionCount;
	private static Text populationCount;

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setMinimumSize(PROBLEM_WIDTH + 250, PROBLEM_HEIGHT + 50);
		shell.setText("TSP Solver");
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);

		canvas = new TSPCanvas(shell, SWT.BORDER);
		GridData layoutData = new GridData(PROBLEM_WIDTH, PROBLEM_HEIGHT);
		layoutData.horizontalAlignment = SWT.BEGINNING;
		layoutData.verticalAlignment = SWT.BEGINNING;
		canvas.setLayoutData(layoutData);

		Composite controls = new Composite(shell, SWT.BORDER);
		controls.setSize(250, PROBLEM_HEIGHT);
		controls.setLayout(new GridLayout(2, false));
		GridData controlsLayoutData = new GridData(SWT.RIGHT, SWT.FILL, false, true);
		controlsLayoutData.widthHint = 250;
		controlsLayoutData.heightHint = PROBLEM_HEIGHT;
		controls.setLayoutData(controlsLayoutData);

		createBuilderControls(controls);
		createSolverControls(controls);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void createSolverControls(Composite controls) {

		Label separator = new Label(controls, SWT.HORIZONTAL | SWT.SEPARATOR);
		GridData sepLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		sepLayoutData.horizontalSpan = 2;
		separator.setLayoutData(sepLayoutData);

		Button solveBtn = new Button(controls, SWT.PUSH);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		layoutData.horizontalSpan = 2;
		solveBtn.setLayoutData(layoutData);
		solveBtn.setText("Solve TSP via GA");

		solveBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuilder resultBuilder = new StringBuilder();
				// Initialize population
				final int GEN_COUNT = Integer.parseInt(generationCount.getText());
				final int EXEC_COUNT = Integer.parseInt(executionCount.getText());
				final int POP_COUNT = Integer.parseInt(populationCount.getText());
				// Run genetic algorithm for specified number of executions
				double bestCost = Integer.MAX_VALUE;
				Tour bestTour = null;
				double avgCost = 0;
				double worstCost = 0;
				GAOptions options = new GAOptions()
										.enable2Opt()
										.setMutation(new EdgeInversion(0.50));
				GeneticAlgorithm<TSPIndividual> ga = new GeneticTSPSolver(options);
				for (int ex = 0; ex < EXEC_COUNT; ex++) {
					Population<TSPIndividual> pop = new Population<TSPIndividual>(POP_COUNT);
					ga.initialize(pop);
					for (int i = 0; i < GEN_COUNT; i++) {
						ga.evolve(pop);
					}
					Tour bestTourExecution = pop.getFittest();
					double bestCostExecution = bestTourExecution.getCost();
					if (bestCostExecution < bestCost) {
						bestCost = bestCostExecution;
						bestTour = bestTourExecution;
					}
					if (bestCostExecution > worstCost) {
						worstCost = bestCostExecution;
					}
					avgCost = (avgCost * ex + bestCostExecution) / (ex + 1);
				}
				resultBuilder.append("Best Cost:		" + bestCost + NL);
				resultBuilder.append("Worst Cost:		" + worstCost + NL);
				resultBuilder.append("Avarage Cost:	" + avgCost + NL);
				canvas.drawTour(bestTour.getCities());
				resultText.setText(resultBuilder.toString());
			}
		});

		GridData textLayoutData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		textLayoutData.widthHint = 80;

		Label l2 = new Label(controls, SWT.NONE);
		l2.setText("Population: ");
		l2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		populationCount = new Text(controls, SWT.NONE);
		populationCount.setLayoutData(textLayoutData);
		populationCount.setText("150");
		
		Label l3 = new Label(controls, SWT.NONE);
		l3.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		l3.setText("Generations: ");
		generationCount = new Text(controls, SWT.NONE);
		generationCount.setLayoutData(textLayoutData);
		generationCount.setText("100");

		Label l4 = new Label(controls, SWT.NONE);
		l4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		l4.setText("Executions: ");
		executionCount = new Text(controls, SWT.NONE);
		executionCount.setLayoutData(textLayoutData);
		executionCount.setText("1");

		Label l5 = new Label(controls, SWT.NONE);
		l5.setText("Results: ");
		GridData l5LayoutData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
		l5LayoutData.horizontalSpan = 2;
		l5.setLayoutData(l5LayoutData);

		resultText = new Text(controls, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
		GridData resultTextLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		resultTextLayoutData.horizontalSpan = 2;
		resultText.setLayoutData(resultTextLayoutData);
		resultText.setText("Press 'Solve TSP via GA'!");
		resultText.pack();
	}

	private static void createBuilderControls(final Composite controls) {
		Button buildBtn = new Button(controls, SWT.PUSH);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		buildBtn.setLayoutData(layoutData);
		buildBtn.setText("Build");

		buildBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int cityCount = Integer.parseInt(cityCountText.getText().trim());
				TSP.create(cityCount, PROBLEM_WIDTH, PROBLEM_HEIGHT);
				canvas.drawCities(new ArrayList<City>(TSP.getCities()));
			}
		});

		Button loadBtn = new Button(controls, SWT.PUSH);
		GridData layoutData2 = new GridData(SWT.FILL, SWT.CENTER, true, false);
		loadBtn.setLayoutData(layoutData2);
		loadBtn.setText("Load...");

		loadBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(controls.getShell(), SWT.NONE);
				dialog.open();
				String fileName = dialog.getFileName();
				String filterPath = dialog.getFilterPath();
				try {
					loadTSP(filterPath+"/"+fileName);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}


		});

		Label l1 = new Label(controls, SWT.NONE);
		l1.setText("Number of Cities: ");
		cityCountText = new Text(controls, SWT.NONE);
		cityCountText.setText("30");
		GridData textLayoutData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		textLayoutData.widthHint = 80;
		cityCountText.setLayoutData(textLayoutData);

	}
	
	private static void loadTSP(String fileName) throws IOException {
		TSPLoader.loadFromFile(fileName);
		canvas.drawCities(new ArrayList<City>(TSP.getCities()));
	}

}
