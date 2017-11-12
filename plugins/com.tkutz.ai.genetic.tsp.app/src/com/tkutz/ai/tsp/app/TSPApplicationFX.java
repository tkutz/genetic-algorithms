package com.tkutz.ai.tsp.app;

import java.io.File;
import java.util.ArrayList;

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

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TSPApplicationFX extends Application {

	private static final String NL = System.getProperty("line.separator");
	private static final String TITLE = "TSP Solver";
	private static final int PROBLEM_WIDTH = 1000;
	private static final int PROBLEM_HEIGHT = 700;
	private static final int CONTROL_PANE_WIDTH = 250;
	private Stage primaryStage;
	private TextField cityCountText;
	private TSPCanvasFX canvas;
	private TextField populationCount;
	private TextField generationCount;
	private TextField executionCount;
	private TextArea resultText;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		try {
			Scene scene = buildScene();
			primaryStage.setScene(scene);
			primaryStage.setTitle(TITLE);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Scene buildScene() {
		BorderPane root = new BorderPane();
		root.setLeft(createCanvas());
		root.setRight(createControls());

		Scene scene = new Scene(root, PROBLEM_WIDTH + CONTROL_PANE_WIDTH, PROBLEM_HEIGHT + 50);
		return scene;
	}

	private Node createCanvas() {
		StackPane pane = new StackPane();
		canvas = new TSPCanvasFX(PROBLEM_WIDTH, PROBLEM_HEIGHT);
		pane.setStyle("-fx-background-color: white");
		pane.getChildren().add(canvas);
		return pane;
	}

	private Node createControls() {
		VBox vBox = new VBox();
		vBox.setPrefWidth(CONTROL_PANE_WIDTH);
		vBox.getChildren().add(createBuilderControls());
		vBox.getChildren().add(new Separator());
		vBox.getChildren().add(createSolverControls());
		return vBox;
	}

	private Node createBuilderControls() {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		Button buildBtn = new Button("Build");
		buildBtn.setMaxWidth(Integer.MAX_VALUE);
		buildBtn.setOnAction(e -> buildTSP());
		grid.add(buildBtn, 0, 0);

		Button loadBtn = new Button("Load...");
		loadBtn.setMaxWidth(Integer.MAX_VALUE);
		loadBtn.setOnAction(e -> openFileDialog());
		grid.add(loadBtn, 1, 0);

		Label l = new Label("No. Cities:");
		l.setPrefWidth(Integer.MAX_VALUE);
		grid.add(l, 0, 1);

		cityCountText = new TextField("30");
		cityCountText.setPrefWidth(Integer.MAX_VALUE);
		grid.add(cityCountText, 1, 1);

		return grid;
	}

	private Node createSolverControls() {
		GridPane grid = new GridPane();
		grid.setHgap(10); // horizontal gaps between cells
		grid.setVgap(10); // vertical gaps between cells
		grid.setPadding(new Insets(10, 10, 10, 10));

		Button solveBtn = new Button("Solve TSP via GA");
		solveBtn.setMaxWidth(Integer.MAX_VALUE);
		solveBtn.setOnAction(e -> solveTSP());
		grid.add(solveBtn, 0, 0, 2, 1);

		Label l2 = new Label("Population: ");
		l2.setPrefWidth(Integer.MAX_VALUE);
		populationCount = new TextField("150");
		populationCount.setPrefWidth(Integer.MAX_VALUE);
		grid.addRow(1, l2, populationCount);

		Label l3 = new Label("Generations: ");
		l3.setPrefWidth(Integer.MAX_VALUE);
		generationCount = new TextField("100");
		generationCount.setPrefWidth(Integer.MAX_VALUE);
		grid.addRow(2, l3, generationCount);

		Label l4 = new Label("Executions: ");
		l4.setPrefWidth(Integer.MAX_VALUE);
		executionCount = new TextField("1");
		executionCount.setPrefWidth(Integer.MAX_VALUE);
		grid.addRow(3, l4, executionCount);

		Label l5 = new Label("Results: ");
		grid.add(l5, 0, 4, 2, 1);
		resultText = new TextArea();
		resultText.setPromptText("Solve TSP via GA");
		resultText.setPrefRowCount(4);
		resultText.setEditable(false);
		grid.add(resultText, 0, 5, 2, 1);

		return grid;
	}
	
	private void openFileDialog() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open TSP Model");
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			TSPLoader.loadFromFile(file.getAbsolutePath());
			canvas.drawCities(new ArrayList<City>(TSP.getCities()));
		}
	}

	private void buildTSP() {
		int cityCount = Integer.parseInt(cityCountText.getText().trim());
		TSP.create(cityCount, PROBLEM_WIDTH, PROBLEM_HEIGHT);
		canvas.drawCities(new ArrayList<City>(TSP.getCities()));
	}

	private void solveTSP() {
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
		GAOptions options = new GAOptions().enable2Opt().setMutation(new EdgeInversion(0.50));
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
		resultBuilder.append("Best Cost:\t\t" + bestCost + NL);
		resultBuilder.append("Worst Cost:\t\t" + worstCost + NL);
		resultBuilder.append("Avarage Cost:\t\t" + avgCost + NL);
		canvas.drawTour(bestTour.getCities());
		resultText.setText(resultBuilder.toString());
	}

}
