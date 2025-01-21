package projectBrick;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class solutionFinalTest {

	@Test
	public void testBuildWall() {
		solutionFinal solution = new solutionFinal();
		int height = 10;
		int width = 10;
		solution.buildLegoWall(height, width);
		assertNotNull(solution.possibleWall);
		assertEquals(height, solution.possibleWall.size());
	}

	// Since printWall prints to console, we can't assert its output directly
    // This test ensures no exceptions are thrown during execution
	@Test
	public void testPrintWall() {
		solutionFinal solution = new solutionFinal();
		int height = 10;
		int width = 10;
		solution.buildLegoWall(height, width);
		solution.printWall();
	}

	@Test
	public void testCanPlaceRows() {
		solutionFinal solution = new solutionFinal();
		List<Integer> topRow = Arrays.asList(2, 3, 5);
		List<Integer> bottomRow = Arrays.asList(1, 3, 6);
		assertTrue(solution.canPlaceRows(topRow, bottomRow));

		topRow = Arrays.asList(2, 3, 5);
		bottomRow = Arrays.asList(2, 3, 5);
		assertFalse(solution.canPlaceRows(topRow, bottomRow));
	}

	@Test
	public void testGenerateConfigurations() {
		solutionFinal solution = new solutionFinal();
		solution.wallWidth = 10;
		solution.brickSizes = new int[] { 1, 2, 3, 4, 6, 8, 10, 12 };
		solution.generateRows(0, false);
		assertFalse(solution.getUniqueRows().isEmpty());
	}
}