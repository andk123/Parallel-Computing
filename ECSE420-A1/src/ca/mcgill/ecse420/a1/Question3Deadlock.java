package ca.mcgill.ecse420.a1;

public class Question3Deadlock {

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
			Philosopher philosopher = new Philosopher(name, firstChopstick, secondChopstick);
			Thread philosopherThread = new Thread(philosopher);
			philosopherThread.start();
		}
	}
}


class Philosopher implements Runnable {

	private String name;
	private String leftChopStick;
	private String rightChopStick;

	//Assign the corresponding chopstick to the left and right of each philosopher
	public Philosopher(String name, String leftChopStick, String rightChopStick) {
		this.name = name;
		this.leftChopStick = leftChopStick;
		this.rightChopStick = rightChopStick;
	}


	@Override
	public void run() {
		//The N philosophers are concurrently going to think and eat for a indefinite amount of time
		while (true) {

			//The philosopher is currently thinking
			action("Thinking");

			//The philosopher acquired his first ChopStick
			synchronized (leftChopStick) {
				action("Taking left chopstick");

				//The philosopher acquired his second ChopStick and eats
				synchronized (rightChopStick) {
					action("Taking right chopstick"); 
					action("Eating");
				}

				//Once the philosopher finishes eating, he puts down both chopSticks marked by the end of both synchronized statement
				action("Puting chopsticks down");
			}
		}
	}

	//Print the action done by the philosopher
	private void action(String action) {
		
		System.out.println(name + " is " + action);
		
		try {
			Thread.sleep(((long)(Math.random() * 150)));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}


}