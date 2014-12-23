package com.tkutz.ai.tsp.app;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.tkutz.ai.tsp.City;

public class TSPCanvas extends Canvas {

	private final TSPPaintListener PAINT_LISTENER = new TSPPaintListener();
	
	public TSPCanvas(Composite parent, int style) {
		super(parent, style);

		this.addPaintListener(PAINT_LISTENER);
	}

	public void drawCities(ArrayList<City> cities) {
		PAINT_LISTENER.setCities(cities);
		PAINT_LISTENER.doDrawPath(false);
		this.redraw();
	}

	public void drawTour(ArrayList<City> cities) {
		PAINT_LISTENER.setCities(cities);
		PAINT_LISTENER.doDrawPath(true);
		this.redraw();
	}

	private class TSPPaintListener implements PaintListener {

		private ArrayList<City> cities;
		private boolean doDrawPath = false;

		void setCities(ArrayList<City> cities) {
			this.cities = cities;
		}
		
		void doDrawPath(boolean doDrawPath) {
			this.doDrawPath  = doDrawPath;
		}
		
		@Override
		public void paintControl(PaintEvent e) {
			if (cities == null) {
				return;
			}
			for (int i = 0; i<cities.size(); i++ ) {
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_DARK_BLUE));
				e.gc.setLineWidth(3);
				e.gc.drawOval(cities.get(i).getX()-1, cities.get(i).getY()-1, 2, 2);
				
				if (doDrawPath) {
					e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_DARK_RED));
					e.gc.setLineWidth(1);
					// connect last city with first one
					if (i==cities.size()-1) {
						e.gc.drawLine(cities.get(i).getX(), cities.get(i).getY(), cities.get(0).getX(), cities.get(0).getY());
					}
					else {
						e.gc.drawLine(cities.get(i).getX(), cities.get(i).getY(), cities.get(i+1).getX(), cities.get(i+1).getY());
					}
				}
			}
		}
	}
}

