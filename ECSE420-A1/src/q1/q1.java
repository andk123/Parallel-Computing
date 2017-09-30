package q1;


import java.util.Random;

public class q1 {
	
	public static void main(String[]args){
		// Initialize 2 grids and fill them up at random
		double [][]gridA = new double[5][5];
		double [][]gridB = new double[5][5];
		// Grid for final solution 
		double [][]gridC = new double[5][5];
		// Random filling of all elements inside matrix a and ab
		for (int i = 0; i < gridA.length; i++) {
		    for (int j = 0; j < gridA[i].length; j++) {
		    	Random rn = new Random();
		    	gridA[i][j] = (double)(rn.nextInt(9) + 1);
		    	gridB[i][j] = (double)(rn.nextInt(9) + 1);
		    }
		}
		
		printMatrix(gridA);
		System.out.println();
		printMatrix(gridB);
		// Sequential Matrix Multiplication
		gridC = sequentialMultiplyMatrix(gridA,gridB);
		System.out.println();
		printMatrix(gridC);
		// Parallel Matrix Multiplication
		gridC = parallelMultiplyMatrix(gridA,gridB,3);
		System.out.println();
		printMatrix(gridC);
	
		
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
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b){
		double [][] result = new double[a.length][b[0].length];
		// Check if invalid multiplication dimensions
		if (a[0].length != b.length) throw
		new RuntimeException("Illegal matrix dimensions.");
		// Multiply each of the elements sequentially
		for (int i = 0; i < a.length; i++) { 
		    for (int j = 0; j < b[0].length; j++) { 
		        for (int k = 0; k < a[0].length; k++) { 
		            result[i][j] += a[i][k] * b[k][j];
		        }
		    }
		}
		return result;
	}
	
	// Parallel Matrix Multiplication method with extra thread parameter
	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b, int numThreads){
		if (a[0].length != b.length) throw
		new RuntimeException("Illegal matrix dimensions.");
		Thread threads[] = new Thread[numThreads];
		double[][]c = new double[a.length][b[0].length];
		// Check if thread number is bigger than rows of matrix A
		int rowsPerThread;
		if (numThreads>= a.length){
			rowsPerThread = 1;			
		}else {
			// If thread number smaller, we can separate row in chunks
			rowsPerThread = a.length/ numThreads;
		}
		// Go through each thread and set start row and end row
		for (int i = 0; i < numThreads; i++) {
			int sRow,eRow;
			if(i == numThreads -1 && ((i * rowsPerThread + rowsPerThread) < a.length)){			
				sRow =  i * rowsPerThread;	
				eRow = a.length;
		
			}else{
				sRow = i * rowsPerThread; 
				eRow = sRow + rowsPerThread;
				
			}
	
			System.out.println("Start Row:  " + sRow + "  End Row: " +eRow);
			// initialize task, create thread, start thread
			matrixMultiplier task = new matrixMultiplier
			(sRow, eRow, a, b, c, a[0].length);
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
	double[][] a,b,c;
	int dim;

	//Constructor
matrixMultiplier(int sRow, int eRow, double[][] dA, double[][] dB,double[][] dC, int dim){
	this.startRow = sRow;
	this.endRow = eRow;
	this.a = dA;
	this.b = dB;
	this.c = dC;
	this.dim = dim;

	}
	
	
	public void run() {
		// same logic as sequential
		while (!Thread.currentThread().isInterrupted()){
			for (int i = startRow; i < endRow; i ++){
				for (int j = 0; j < dim; j++)
					for (int k = 0; k < dim; k++)
					c[i][j] += (a[i][k] * b[k][j]);
				}
			Thread.currentThread().interrupt();
			}			
		}		

}

