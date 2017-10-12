package ca.mcgill.ecse420.a1;

// Class for Question 2 where we will demonstrate a deadlock
public class Question2 {
	
	// 2 Strings which will represent elements we will be locking on
	String str1 = "1";
    String str2 = "2";
   
    // Thread 1
    Thread t1 = new Thread("Thread 1"){
        public void run(){
            while(true){
            	// Lock on '1'
                synchronized(str1){
                	System.out.println("Thread 1: Holding lock on '1'... Waiting on lock '2'");          	
                    // Lock on '2' while being already locked on 1
                	synchronized(str2){
                    	System.out.println("Thread 1: Holding lock on '1'and '2'");
                        System.out.println(str1 + str2);
                    }
                }
            }
        }
    };
    
    // Thread 2
    Thread t2 = new Thread("Thread 2"){
        public void run(){
            while(true){
            	// Lock on '2'
                synchronized(str2){
                	System.out.println("Thread 2: Holding lock on '2'... Waiting on lock '1'");
                	// Lock on '1' while being already locked on 2
                    synchronized(str1){
                    	System.out.println("Thread 2: Holding lock on '1'and '2'");
                        System.out.println(str2 + str1);
                    }
                }
            }
        }
    };
     
    public static void main(String a[]){
    	// Execute both threads
    	// We should hit deadlock once they are both waiting on each other to release lock
        Question2 deadlock = new Question2();
        deadlock.t1.start();
        deadlock.t2.start();
    }

}
