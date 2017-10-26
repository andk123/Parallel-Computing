import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

 // Filter Lock algorithm based on pseudo-code from Chapter 2
public class FilterLock implements Lock {
	int[] level;
	int[] victim;
	int n;
	public FilterLock(int n) {
		this.n = n;
		level = new int[n];
		victim = new int[n]; 
		for (int i = 0; i < n; i++) {
			level[i] = 0;
		}
	}
	public void lock() {
		int me = ThreadID.get();
		for (int i = 1; i < n; i++) { // Iterate through levels
			level[me] = i;
			// Set itself as victim
			victim[i] = me;
            for (int k = 0; k < n; k++) {
                while ((k != me) && (level[k] >= i && victim[i] == me)) {
                    //spin wait
                }
            }
			
		}
	}
	public void unlock() {
		//release lock
		int me = ThreadID.get();
		level[me] = 0;
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
