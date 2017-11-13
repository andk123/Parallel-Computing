package ca.mcgill.ecse420.a2;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

 // Filter Lock algorithm based on pseudo-code from Chapter 2 of the manual The Art of Multiprocessor Programming at page 29.
public class FilterLock implements Lock {
	AtomicInteger[] level;
	AtomicInteger[] victim;
	int n;
	public FilterLock(int n) {
		this.n = n;
		level = new AtomicInteger[n];
		victim = new AtomicInteger[n]; 
		for (int i = 0; i < n; i++) {
			level[i] = new AtomicInteger(0);
			victim[i] = new AtomicInteger(0);
		}
	}
	
	@Override
	public void lock() {
		int me = ThreadID.get();
		for (int i = 1; i < n; i++) { // Iterate through levels
			level[me].set(i);
			// Set itself as victim
			victim[i].set(me);
            for (int k = 0; k < n; k++) {
                while ((k != me) && (level[k].get() >= i && victim[i].get() == me)) {
                    //spin wait
                }
            }
			
		}
	}
	
	@Override
	public void unlock() {
		//release lock
		int me = ThreadID.get();
		level[me].set(0);
	}
	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}
	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}
}
