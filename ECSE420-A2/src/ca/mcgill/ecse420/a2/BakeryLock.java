package ca.mcgill.ecse420.a2;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

// Bakery Lock algorithm based on pseudo-code from Chapter 2
public class BakeryLock implements Lock{

	AtomicBoolean[] flag;
	AtomicInteger [] label;
	int n;
	public BakeryLock (int n) {
		this.n = n;
		flag = new AtomicBoolean[n];
		label = new AtomicInteger[n];
		for (int i = 0; i < n; i++) {
			flag[i] = new AtomicBoolean(false); label[i] = new AtomicInteger(0);
		}
	}

	// Method that returns max value from all labels
	public int getMaxLabel(){
		int max = label[0].get();
		for (int i = 1; i < label.length; i++) {
			if (label[i].get() > max) {
				max = label[i].get();
			}
		}
		return max;


	}
	public void lock() {
		//Doorway Step
		int i = ThreadID.get();
		flag[i].set(true);
		label[i].set(getMaxLabel() + 1);
		//Waiting section
		for (int k = 0; k < n; k++) {
			while ((k != i) && flag[k].get() && ((label[k].get() < label[i].get()) 
					|| ((label[k].get() == label[i].get()) && k < i))) {
				//spin wait
			}
		}
	}
	public void unlock() {
		//Release lock
		flag[ThreadID.get()].set(false);
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
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}
}
