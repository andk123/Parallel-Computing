package ca.mcgill.ecse420.a3;
import java.util.Random;
import java.util.concurrent.*;

public class MatrixVectorMultiplication {
	static int numberThread = 4;
	static ExecutorService exec = Executors.newFixedThreadPool(numberThread);
    public static void main (String []args) throws Exception{
    	
        int arraySize = 2000;	
        
		//Initialize 2 grids and fill them up at random
		double [][]matrix = new double[arraySize][arraySize];
		double []vector = new double[arraySize];

		// Random filling of all elements inside matrix a and b
		for (int i = 0; i < matrix.length; i++) {
				Random rn = new Random();
				vector[i] = (double)(rn.nextInt(9) + 1);
				//vector[i] = 1;
			for (int j = 0; j < matrix[i].length; j++) {
				rn = new Random();
				matrix[i][j] = (double)(rn.nextInt(9) + 1);
			}
		}
		
		System.out.println("TEST Sequential");
		long beginTime = System.currentTimeMillis();
		sequentialMultiplication(matrix,vector);
		//printVector(sequentialMultiplication(matrix,vector));
		long endTime = System.currentTimeMillis();
		System.out.println("Sequential Multiplication time : " + (endTime-beginTime));
		
		System.out.println();
		System.out.println("TEST Parallel");
		beginTime = System.currentTimeMillis();
		parallelMultiplication(matrix,vector);
		//printVector(parallelMultiplication(matrix,vector));
		endTime = System.currentTimeMillis();
		System.out.println("Parallel Multiplication time : " + (endTime-beginTime));
		
		
		exec.shutdownNow();

    }
    
	// Helper method to print Matrix
	static void printMatrix(double[][] grid) {
		for(int r=0; r<grid.length; r++) {
			for(int c=0; c<grid[r].length; c++)
				System.out.print(grid[r][c] + " ");
			System.out.println();
		}
		System.out.println();
	}

	// Helper method to print vector
	static void printVector(double[] vector) {
		for(int c=0; c<vector.length; c++){
				System.out.print(vector[c] + " ");
			}
			System.out.println();
	}


	public static double[] sequentialMultiplication(double[][] matrix,double[] vector) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = 0;
			for (int j = 0; j < n; j++) {
				result[i] += matrix[i][j] * vector[j];
			}
		}
		return result;
	}


    public static double [] parallelMultiplication(double[][] matrix,double[] vector) throws Exception{
        // New result Vector
    	double [] result = new double [matrix.length];
    	// Loop through rows and initialize threads to do multiplication jobs
        for (int i = 0 ; i < matrix.length; i ++){
            exec.execute(new MultiplyRow(matrix,vector, result, i));
        }
        exec.shutdown();
        return result;
    }



}

class MultiplyRow implements Runnable{
    private double [][] matrix;
    private double [] vector, result;
    private int i;

    public MultiplyRow (double [][] matrix, double [] vector, double [] result, int i){
        this.matrix = matrix;
        this.vector = vector;
        this.result = result;
        this.i = i;
    }

    public void run (){
        result[i] = recursiveAddition(matrix[i],vector,0,vector.length-1);
    }

    public double recursiveAddition (double[] matrixRow, double vector[], int left, int right){
        //Base case
    	if (left == right){
    		return matrixRow[left]*vector[right];
    	}else{
    		// Get halfpoint
    		int split = (int) Math.floor((left+right)/2);
    		//Split into 2 recursive calls
            double leftSum = recursiveAddition(matrixRow,vector,left,split);
            double rightSum = recursiveAddition(matrixRow,vector,split+1,right);
            // Sum
            return leftSum + rightSum; 		
    		
    	}


    }

}

