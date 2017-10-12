package ca.mcgill.ecse420.a1;

import java.util.Scanner;

public class Question3Final {

	public static void main(String[] args) throws Exception {

		//The number of philosophers N
		int numberPhilosophers = 0;
		
		//Enter the number of philosophers
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		boolean isValid = false;
		while(!isValid){
			try{
			System.out.println("Enter the number of philosophers: ");
			isValid = true;
			String input = reader.nextLine(); // Scans the next token of the input
			numberPhilosophers = Integer.parseInt(input); //Make sure it is an integer
			}
			catch (Exception e) {
				isValid = false;
				System.out.println("Enter a valid number");
			}
		}
		//once finished
		reader.close(); 

		//We have the same number of chopsticks than that of philosophers
		String[] chopSticks = new String[numberPhilosophers];

		//Create an instance of each chopstick with its number
		for (int i = 0; i < numberPhilosophers; i++) {

			chopSticks[i] = "Chopstick" + i;
		}

		//Create each philosopher threads
		for (int i = 0; i < numberPhilosophers; i++) {

			String name = "Philosopher" + (i+1);

			//The chopsticks are a shared object between 2 philosophers. So each chopstick must be identified to a philosopher
			String firstChopstick = chopSticks[i];
			String secondChopstick = chopSticks[(i+1) % numberPhilosophers];

			//Create the philosopher runnable with the corresponding chopsticks and launch the threads.
			Philosopher philosopher;
			
			//Break the possible circular wait. One of the essential condition for a deadlock to ensure that no deadlock occurs.
			if (i == 0 ) {
				philosopher = new Philosopher(name, secondChopstick, firstChopstick);
			}
			else {
			//Create the philosopher runnable with the corresponding chopsticks and launch the threads.
			philosopher = new Philosopher(name, firstChopstick, secondChopstick);
			}
			
			Thread philosopherThread = new Thread(philosopher);
			philosopherThread.start();
		}
	}
}
