// Purpose: This file contains the solution to the problem of building a wall
// with bricks of different sizes without dynamic programming.
package projectBrick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class solutionFinal {
	int wallWidth;
	private List<List<Integer>> uniqueRows = new ArrayList<>();

	public List<List<Integer>> getUniqueRows() {
		return uniqueRows;
	}

	public void setUniqueRows(List<List<Integer>> uniqueRows) {
		this.uniqueRows = uniqueRows;
	}

	List<List<Integer>> possibleWall = new ArrayList<>();
	private List<Integer> currentRow = new ArrayList<>();
	int[] brickSizes;
	List<Boolean> hollowList = new ArrayList<>();

	public static void main(String[] args) {
		solutionFinal solution = new solutionFinal();
		int height = 10; // Example height
		int width = 10; // Example width
		solution.buildWall(height, width);
		solution.printWall();
	}

	public void buildWall(int height, int width) {
		this.wallWidth = width;
		this.brickSizes = new int[] { 1, 2, 3, 4, 6, 8, 10, 12 };
		// Generate all possible row configurations
		generateConfigurations(0, false);
		int numConfigs = uniqueRows.size();
		List<Integer>[] graph = new List[numConfigs];
		Arrays.setAll(graph, i -> new ArrayList<>());

		// Build the adjacency list for the configurations graph
		for (int i = 0; i < numConfigs; i++) {
			for (int j = 0; j < numConfigs; j++) {
				if (i != j && canPlaceRows(uniqueRows.get(i), uniqueRows.get(j))) {
					graph[i].add(j);
				}
			}
		}

		// Store one possible wall configuration using DFS
		boolean[] visited = new boolean[numConfigs];
		List<Integer> path = new ArrayList<>();
		for (int i = 0; i < numConfigs; i++) {
			if (dfs(graph, visited, path, i, height)) {
				break;
			}
		}
	}

	// DFS to find one valid wall configuration
	private boolean dfs(List<Integer>[] graph, boolean[] visited, List<Integer> path, int current, int height) {
		if (path.size() == height) {
			if (hollowList.get(path.get(path.size() - 1)) == true) {
				return false;
			}
			for (int idx : path) {
				possibleWall.add(uniqueRows.get(idx));
				// System.out.println(idx);
				// System.out.println(possibleWall.get(possibleWall.size()-1));
			}
			return true;
		}
		if (path.size() == 0 && hollowList.get(current) == true) {
			return false;
		}
		visited[current] = true;

		path.add(current);

		for (int neighbor : graph[current]) {
			if (!visited[neighbor]) {
				if (dfs(graph, visited, path, neighbor, height)) {
					return true;
				}
			}
		}

		path.remove(path.size() - 1);
		visited[current] = false;
		return false;
	}

	// Print the wall configuration
	public void printWall() {
		for (int i = possibleWall.size() - 1; i >= 0; i--) {
			StringBuilder layer = new StringBuilder();
			boolean evenBrick = false;
			for (int brickSize : possibleWall.get(i)) {
				if (brickSize < 0) {
					brickSize = -brickSize;
					for (int j = 0; j < brickSize; j++) {
						layer.append("H");
					}
					continue;
				}
				if (i % 2 == 0) {
					for (int j = 0; j < brickSize; j++) {
						layer.append(evenBrick == true ? "." : "X");
					}
				} else {
					for (int j = 0; j < brickSize; j++) {
						layer.append(evenBrick == true ? "|" : "-");
					}
				}

				evenBrick = !evenBrick;
			}
			System.out.println(layer.toString());
		}
	}

	// Checks if two rows can be placed on top of each other
	boolean canPlaceRows(List<Integer> topRow, List<Integer> bottomRow) {
		int sumTop = topRow.get(0);
		int sumBottom = bottomRow.get(0);
		int i = 1, j = 1;
		while (i < topRow.size() && j < bottomRow.size()) {
			if (sumTop == sumBottom) {
				return false; // Cracks align
			}
			if (sumTop < sumBottom) {
				int topRowValue = topRow.get(i++);
				if (topRowValue < 0) {
					topRowValue = -topRowValue;
				}
				sumTop += topRowValue;
			} else {
				int botttomRowValue = bottomRow.get(j++);
				if (botttomRowValue < 0) {
					botttomRowValue = -botttomRowValue;
				}
				sumBottom += botttomRowValue;
			}
		}

		for (int k = 0; k < bottomRow.size(); k++) {
			if (bottomRow.get(k) < 0) { // Hole in the bottom row
				int holeStart = 0;
				int holeEnd = 0;
				int brickStart = 0;
				int brickEnd = 0;
				int topRowBrick = 0;
				int topRowHoleStart = 0;
				int topRowHoleEnd = 0;
				for (int m = 0; m < k; m++) {
					holeStart += bottomRow.get(m);
				}
				int holeLength = -bottomRow.get(k);
				holeEnd = holeStart + holeLength;
				for (int n = 0; n < topRow.size(); n++) {
					topRowBrick = topRow.get(n);
					if (topRowBrick < 0) {
						topRowBrick = -topRowBrick;
						topRowHoleStart = brickStart;
						topRowHoleEnd = topRowHoleStart + topRowBrick;
						if ((topRowHoleStart >= holeStart && topRowHoleStart <= holeEnd)
								|| (topRowHoleEnd >= holeStart && topRowHoleEnd <= holeEnd)
								|| (topRowHoleStart <= holeStart && topRowHoleEnd >= holeEnd)) {
							return false;
						}
					}
					brickStart += topRowBrick;
					if (brickStart > holeStart) {
						brickEnd = brickStart;
						brickStart -= topRowBrick;
						if (brickEnd < holeEnd || brickEnd - brickStart < holeLength * 2) {
							return false;
						}
						break;
					}
				}

			}
		}
		return true;
	}

	// Generate all possible row configurations
	void generateConfigurations(int currentWidth, boolean hasHole) {
		if (currentWidth > wallWidth) {
			return; // Exceeds width of wall, invalid configuration
		}
		if (currentWidth == wallWidth) {
			// Found a valid row configuration
			uniqueRows.add(new ArrayList<>(currentRow));
			if (hasHole) {
				hollowList.add(true);
			} else {
				hollowList.add(false);
			}
			return;
		}
		for (int size : brickSizes) {
			if (!hasHole && currentWidth > 0 && ((currentWidth + size) < (wallWidth - 1))) {
				int holeSize = -size;
				currentRow.add(holeSize);
				generateConfigurations(currentWidth + size, true);
				currentRow.remove(currentRow.size() - 1); // Represent hole with same size
			}
			currentRow.add(size);
			generateConfigurations(currentWidth + size, hasHole); // Continue to add bricks
			currentRow.remove(currentRow.size() - 1); // Backtrack to try other bricks
		}

	}

	public int getWallWidth() {
		return wallWidth;
	}

	public void setWallWidth(int wallWidth) {
		this.wallWidth = wallWidth;
	}

	public int[] getBrickSizes() {
		return brickSizes;
	}

	public void setBrickSizes(int[] brickSizes) {
		this.brickSizes = brickSizes;
	}

}