package com.tkutz.ai.tsp;

/**
 * Representation of a city in the TSP. Basically consisting of two coordinates
 * x and y as well as convenience methods for distance calculation.
 * 
 * @author Thomas Kutz
 *
 */
public class City {

	int x;
	int y;

	public City(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}
}