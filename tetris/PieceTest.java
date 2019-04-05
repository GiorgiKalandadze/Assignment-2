package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	private Piece stick1, stick2;
	private Piece l1, l12, l13, l14;
	private Piece l2, l22, l23, l24;
	private Piece s1, s12, s13, s14;
	private Piece s2, s22, s23, s24;
	private Piece square1, square2;
	
	@Before
	public void setUp() throws Exception {
		//Stick
		stick1 = new Piece(Piece.STICK_STR);
		stick2 = stick1.computeNextRotation();
		
		//L1
		l1 = new Piece(Piece.L1_STR);
		l12 = l1.computeNextRotation();
		l13 = l12.computeNextRotation();
		l14 = l13.computeNextRotation();
		
		//L2
		l2 = new Piece(Piece.L2_STR);
		l22 = l2.computeNextRotation();
		l23 = l22.computeNextRotation();
		l24 = l23.computeNextRotation();
		
		//S1
		s1 = new Piece(Piece.S1_STR);
		s12 = s1.computeNextRotation();
		
		//S2
		s2 = new Piece(Piece.S2_STR);
		s22 = s2.computeNextRotation();
		
		//Square
		square1 = new Piece(Piece.SQUARE_STR);
		square2 = square1.computeNextRotation();
		
		//Pyramid
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	
	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}
	
	@Test
	public void testWidth() {
		testStickWidth();
		testL1Width();
		testL2Width();
		testS1Width();
		testS2Width();
		testSquareWidth();
		testPyramidWidth();
	}
	
	private void testPyramidWidth() {
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr3.getWidth());
		assertEquals(2, pyr4.getWidth());
		
	}

	private void testSquareWidth() {
		assertEquals(2, square1.getWidth());
		assertEquals(2, square2.getWidth());
		
	}

	private void testS2Width() {
		assertEquals(3, s2.getWidth());
		assertEquals(2, s22.getWidth());
	}

	private void testS1Width() {
		//S1
		assertEquals(3, s1.getWidth());
		assertEquals(2, s12.getWidth());	
	}

	private void testL2Width() {
		//L2
		assertEquals(2, l2.getWidth());
		//L2 Rotation 1
		assertEquals(3, l22.getWidth());
		//L2 Rotation 2
		assertEquals(2, l23.getWidth());
		//L2 Rotation 3
		assertEquals(3, l24.getWidth());
	}

	private void testStickWidth() {
		//Stick
		assertEquals(1, stick1.getWidth());
		//Stick Rotation 1
		assertEquals(4, stick2.getWidth());
	}
	
	private void testL1Width() {
		//L1
		assertEquals(2, l1.getWidth());
		//L1 Rotation 1
		assertEquals(3, l12.getWidth());
		//L1 Rotation 2
		assertEquals(2, l13.getWidth());
		//L1 Rotation 3
		assertEquals(3, l14.getWidth());
	}

	@Test
	public void testHeight() {
		testStickHeight();
		testL1Height();
		testL2Height();
		testS1Height();
		testS2Height();
		testSquareHeight();
		testPyramidHeight();
	}
	
	private void testPyramidHeight() {
		assertEquals(2, pyr1.getHeight());
		assertEquals(3, pyr2.getHeight());
		assertEquals(2, pyr3.getHeight());
		assertEquals(3, pyr4.getHeight());
		
	}

	private void testSquareHeight() {
		assertEquals(2, square1.getHeight());
		assertEquals(2, square2.getHeight());	
		
	}

	private void testS2Height() {
		assertEquals(2, s2.getHeight());
		assertEquals(3, s22.getHeight());	
		
	}

	private void testL2Height() {
		assertEquals(3, l2.getHeight());
		//L1 Rotation 1
		assertEquals(2, l22.getHeight());
		//L1 Rotation 2
		assertEquals(3, l23.getHeight());
		//L1 Rotation 3
		assertEquals(2, l24.getHeight());
		
	}

	private void testS1Height() {
		assertEquals(2, s1.getHeight());
		assertEquals(3, s12.getHeight());	
	}

	private void testL1Height() {
		assertEquals(3, l1.getHeight());
		//L1 Rotation 1
		assertEquals(2, l12.getHeight());
		//L1 Rotation 2
		assertEquals(3, l13.getHeight());
		//L1 Rotation 3
		assertEquals(2, l14.getHeight());
		
	}

	private void testStickHeight() {
		assertEquals(4, stick1.getHeight());
		//Stick Rotation 1
		assertEquals(1, stick2.getHeight());
	}

	
	@Test
	public void testSkirt() {
		testStickSkirt();
		testL1Skirt();
	}
	
	private void testL1Skirt() {
		
		
	}

	private void testStickSkirt() {
		int a[] = stick1.getSkirt();
		for(int i = 0; i < a.length; i++) {
			assertEquals(0, a[i]);
		}
		
		//Skirt rotation
		int b[] = stick2.getSkirt();
		for(int i = 0; i < b.length; i++) {
			assertEquals(0, b[i]);
		}
		
	}

	@Test
	public void testFastRotation() {
		testStickRotation();
	}
	
	private void testStickRotation() {
		
		
	}

	@Test
	public void testEquals() {
		
	}
	
}
