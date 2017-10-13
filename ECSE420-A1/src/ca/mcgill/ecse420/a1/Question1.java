package ca.mcgill.ecse420.a1;

import java.util.Random;

public class Question1 {

	public static void main(String[]args){
		int arraySize = 1000;
		int numberThreads = 3;
		
		//Initialize 2 grids and fill them up at random
		double [][]gridA = new double[arraySize][arraySize];
		double [][]gridB = new double[arraySize][arraySize];
		// Grid for final solution 
		double [][]gridC = new double[arraySize][arraySize];
		// Random filling of all elements inside matrix a and b
		for (int i = 0; i < gridA.length; i++) {
			for (int j = 0; j < gridA[i].length; j++) {
				Random rn = new Random();
				gridA[i][j] = (double)(rn.nextInt(9) + 1);
				gridB[i][j] = (double)(rn.nextInt(9) + 1);
			}
		}

		//printMatrix(gridA);
		//printMatrix(gridB);

		// Sequential Matrix Multiplication
		
		//long beginTime = System.currentTimeMillis();
		//gridC = sequentialMultiplyMatrix(gridA,gridB);
		//long endTime = System.currentTimeMillis();
		//System.out.println("Sequential Multiplication time : " + (endTime-beginTime));

		// Parallel Matrix Multiplication
		
		long beginTime = System.currentTimeMillis();
		gridC = parallelMultiplyMatrix(gridA,gridB, numberThreads);
		long endTime = System.currentTimeMillis();
		System.out.println("Parallel Multiplication time : " + (endTime-beginTime));
	}


	// Helper method to print Matrix
	static void printMatrix(double[][] grid) {
		for(int r=0; r<grid.length; r++) {
			for(int c=0; c<grid[r].length; c++)
				System.out.print(grid[r][c] + " ");
			System.out.println();
		}
	}


	// Sequential Matrix Multiplication method
	public static double[][] sequentialMultiplyMatrix(double[][] gridA, double[][] gridB){
		double [][] result = new double[gridA.length][gridB[0].length];
		// Check if invalid multiplication dimensions
		if (gridA[0].length != gridB.length) throw
		new RuntimeException("Illegal matrix dimensions.");
		// Multiply each of the elements sequentially
		for (int i = 0; i < gridA.length; i++) { 
			for (int j = 0; j < gridB[0].length; j++) { 
				for (int k = 0; k < gridA[0].length; k++) { 
					result[i][j] += gridA[i][k] * gridB[k][j];
				}
			}
		}
		return result;
	}

	// Parallel Matrix Multiplication method with extra thread parameter
	public static double[][] parallelMultiplyMatrix(double[][] gridA, double[][] gridB, int numThreads){
		if (gridA[0].length != gridB.length) throw new RuntimeException("Illegal matrix dimensions.");

		Thread threads[] = new Thread[numThreads];
		double[][]c = new double[gridA.length][gridB[0].length];

		// Check if thread number is bigger than rows of matrix A
		int rowsPerThread;
		if (numThreads>= gridA.length){
			rowsPerThread = 1;			
		}else {
			// If thread number smaller, we can separate row in chunks
			rowsPerThread = gridA.length/ numThreads;
		}
		// Go through each thread and set start row and end row
		for (int i = 0; i < numThreads; i++) {
			int sRow,eRow;
			if(i == numThreads -1 && ((i * rowsPerThread + rowsPerThread) < gridA.length)){			
				sRow =  i * rowsPerThread;	
				eRow = gridA.length;

			}
			else{
				sRow = i * rowsPerThread; 
				eRow = sRow + rowsPerThread;

			}

			System.out.println("Start Row:  " + sRow + "  End Row: " +eRow);
			// initialize task, create thread, start thread
			matrixMultiplier task = new matrixMultiplier
					(sRow, eRow, gridA, gridB, c, gridA[0].length);
			threads[i] = new Thread(task);
			threads[i].start();
		}

		// wait for all threads to finish
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return c;

	}
}


//Thread Matrix Multiplier
class matrixMultiplier extends Thread{
	int startRow;
	int endRow;
	double[][] gridA,gridB,gridC;
	int dimension;

	//Constructor
	matrixMultiplier(int sRow, int eRow, double[][] gridA, double[][] gridB,double[][] gridC, int dimension){
		this.startRow = sRow;
		this.endRow = eRow;
		this.gridA = gridA;
		this.gridB = gridB;
		this.gridC = gridC;
		this.dimension = dimension;

	}


	public void run() {
		// same logic as sequential
		while (!Thread.currentThread().isInterrupted()){
			for (int i = startRow; i < endRow; i ++){
				for (int j = 0; j < dimension; j++)
					for (int k = 0; k < dimension; k++)
						gridC[i][j] += (gridA[i][k] * gridB[k][j]);
			}
			Thread.currentThread().interrupt();
		}			
	}		

}

