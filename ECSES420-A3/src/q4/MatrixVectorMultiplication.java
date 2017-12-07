package q4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MatrixVectorMultiplication {
	static ExecutorService executor = Executors.newCachedThreadPool();
	

	public static void main(String[]args) throws InterruptedException, ExecutionException{
		int arraySize = 100;

		
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

		printMatrix(matrix);
		System.out.println();
		printVector(vector);
		
		System.out.println("TEST");
		printVector(sequentialMultiplication(matrix,vector));
		System.out.println("TEST");
		printVector(parallelMultiplication(matrix,vector));
		executor.shutdownNow();


	}


	// Helper method to print Matrix
	static void printMatrix(double[][] grid) {
		for(int r=0; r<grid.length; r++) {
			for(int c=0; c<grid[r].length; c++)
				System.out.print(grid[r][c] + " ");
			System.out.println();
		}
	}
	
	// Helper method to print vector
	static void printVector(double[] vector) {
		for(int c=0; c<vector.length; c++){
				System.out.print(vector[c] + " ");
			}
			
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

	public static double[] parallelMultiplication(double[][] matrix,double[] vector) throws InterruptedException, ExecutionException {
		int n = vector.length;
		List<Future<Double>> finalValues = new ArrayList<Future<Double>>();
		// Loop through Matrix Rows
		for (int i = 0; i < n; i++) {
			List<Future<Double>> rowMultiplications= new ArrayList<Future<Double>>();
			// Loop through column elements
			for (int j = 0; j < vector.length; j++) {
				//Parallel Multiplication
				Future<Double> multiplication = executor.submit(new MultiplicationTask(matrix[i][j],vector[j]));
			      try {
			    	  multiplication.get();

			        } catch (Exception e) {
			            e.printStackTrace();
			        }
				rowMultiplications.add(multiplication);
			}
			//Recursive-Parallel Add of row values
			finalValues.add(listSummer(rowMultiplications)); 
		}

		return createFinalVector(finalValues);

	}

	//Sums value of the multiplied list items
	public static Future<Double> listSummer(List<Future<Double>> rowList) {
		//Base Case
		if (rowList.size() == 1) { 
			return rowList.get(0);
		} else {
			// Get middle point
			int middle = rowList.size() / 2;
			List<Future<Double>> firstHalf = rowList.subList(0, middle);
			List<Future<Double>> lastHalf = rowList.subList(middle, rowList.size());
			//Recursive addition call
			Future<Double> recursiveAddition = executor
					.submit(new AdditionTask(listSummer(firstHalf), listSummer(lastHalf)));
		      try {
		    	  recursiveAddition.get();	    

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			return recursiveAddition;
		}
	}


// Method to create final vector from list values
public static double [] createFinalVector(List<Future<Double>> finalValues) throws InterruptedException, ExecutionException{
	double[] result = new double[finalValues.size()];
	for (int i = 0; i < finalValues.size(); i++) {
		result[i] = finalValues.get(i).get(); // put it in result. Blocking
	}
	return result;
}

}


class MultiplicationTask implements Callable<Double>{

	Double matrixValue;
	Double vectorValue;


	public MultiplicationTask(double matrixValue, double vectorValue) {
		this.matrixValue = matrixValue;
		this.vectorValue = vectorValue;
	}

	public Double call() throws Exception {
		double multiplicationResult = this.matrixValue*this.vectorValue;
		return multiplicationResult;
	}


}

class AdditionTask implements Callable<Double>{

	Future<Double> toAdd1;
	Future<Double> toAdd2;


	public AdditionTask(Future<Double> future, Future<Double> future2) {
		this.toAdd1 = future;
		this.toAdd2 = future2;
	}

	public Double call() throws Exception {
		double additionResult = this.toAdd1.get() +this.toAdd2.get();
		return additionResult;
	}


}