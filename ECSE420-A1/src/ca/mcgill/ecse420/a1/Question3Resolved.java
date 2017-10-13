package ca.mcgill.ecse420.a1;

public class Question3Resolved {

	public static void main(String[] args) throws Exception {

		//The number of philosophers N
		int numberPhilosophers = 5;

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
