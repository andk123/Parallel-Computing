package ca.mcgill.ecse420.a2;
import java.util.concurrent.locks.Lock;

public class LockTest{

	//The counter our threads will use to increment
	public volatile static int count = 0;
	//The number of time each thread will increment the count
	public volatile static int numberLoops = 100;
	
	public static void main(String[] args) throws Exception {

		//The number of threads N
		int numberThreads = 4;
		
		/*
		 * The code that can be changed depending on the test. You can change the number of threads used, and also switch between
		 * the FilterLock and the BakeryLock by commenting or uncommenting the following lines
		 */

//		Lock lock = new FilterLock(numberThreads);
		Lock lock = new BakeryLock(numberThreads);

		//Create each counter threads
		for (int i = 0; i < numberThreads; i++) {

			//Create the counter runnable with the corresponding lock (shared between all threads) and launch the threads.
			Counter counter = new Counter(lock);
			Thread thread = new Thread(counter);
			thread.start();
		}
	}



	public static class Counter implements Runnable {

		Lock lock;

		public Counter(Lock lock) {
			this.lock = lock;
		}


		@Override
		public void run() {
			//The N threads are starting to count

			action("Starting to count");

			for (int i = 0; i < numberLoops; i++){

				//The thread is acquiring the lock
				lock.lock();
				try{
					
					//The thread is incrementing the count
					action("Counting");
					count++;
					System.out.println(count);
				}
				finally{
					//Releasing the lock
					lock.unlock();
				}
			}
			//Once we are out of the loop, we are done counting. The counter should be equal to numberLoops * numberThreads
			action("finished counting !");
		}

		//Print the action done by the thread
		private void action(String action) {

			System.out.println(Thread.currentThread().getName() + " is " + action);

			try {
				Thread.sleep(((long)(Math.random() * 10)));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

	}
}
