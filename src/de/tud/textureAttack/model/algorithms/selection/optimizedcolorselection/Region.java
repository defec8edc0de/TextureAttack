package de.tud.textureAttack.model.algorithms.selection.optimizedcolorselection;

import java.util.LinkedList;

public class Region {

	private int pixelCount;
	private LinkedList<int[]> region;

	
	public Region(){
		this.pixelCount = 0;
		region = new LinkedList<int[]>();
	}
	
	
	/**
	 * Returns the pixel coordinate of the region pixel at position i coord =
	 * TRUE = X coord = FALSE = Y
	 * 
	 * @param coord
	 * @param i
	 * @return int
	 */
	public int getCoordOfIndex(boolean coord, int i) {
		try {
			int[] point = region.get(i);
			return point[coord ? 0 : 1];
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}

	public LinkedList<int[]> getRegion() {
		return region;
	}

	public void setRegion(LinkedList<int[]> region) {
		this.region = region;
	}

	public int getPixelCount() {
		return pixelCount;
	}

	public void setPixelCount(int i) {
		this.pixelCount = i;
	}
	
	public boolean add(int[] point){
		if (!region.contains(point)){
			boolean success = region.add(point);
			if (success) pixelCount++;
			return success;
		}
		else return false;
	}
	
	public String toString(){
		StringBuffer result = new StringBuffer();
		result.append(pixelCount+":\n");
		for (int i = 0; i < region.size();i++)
			result.append("X=").append(getCoordOfIndex(true, i)).append(" , ").append("Y=").append(getCoordOfIndex(false, i));
		return result.toString();
	}

}
