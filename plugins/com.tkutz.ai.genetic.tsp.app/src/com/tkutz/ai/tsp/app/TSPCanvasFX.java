package com.tkutz.ai.tsp.app;

import java.util.ArrayList;

import com.tkutz.ai.tsp.City;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TSPCanvasFX extends Canvas {

	public TSPCanvasFX(int width, int height) {
		super(width, height);
	}
	
	public void drawCities(ArrayList<City> cities) {
		doDraw(cities, false);
	}
	
	public void drawTour(ArrayList<City> cities) {
		doDraw(cities, true);
	}

	private void doDraw(ArrayList<City> cities, boolean connectCities) {
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, getWidth(), getHeight());
		for (int i = 0; i<cities.size(); i++ ) {
			City city = cities.get(i);

			gc.setFill(Color.LAWNGREEN);
			gc.fillOval(city.getX()-4, city.getY()-4, 8, 8);
			
			if (connectCities) {
				gc.setStroke(Color.DARKRED);
				gc.setLineWidth(1);
				// connect last city with first one
				if (i==cities.size()-1) {
					gc.strokeLine(city.getX(), city.getY(), cities.get(0).getX(), cities.get(0).getY());
				}
				else {
					gc.strokeLine(city.getX(), city.getY(), cities.get(i+1).getX(), cities.get(i+1).getY());
				}
			}
		}
	}

}
