
// Bakery Lock algorithm based on pseudo-code from Chapter 2
public class BakeryLock {

	boolean[] flag;
	int [] label;
	int n;
	public BakeryLock (int n) {
		this.n = n;
		flag = new boolean[n];
		label = new int[n];
		for (int i = 0; i < n; i++) {
			flag[i] = false; label[i] = 0;
		}
	}

	// Method that returns max value from all labels
	public int getMaxLabel(){
		int max = label[0];
		for (int i = 1; i < label.length; i++) {
			if (label[i] > max) {
				max = label[i];
			}
		}
		return max;


	}
	public void lock() {
		//Doorway Step
		int i = ThreadID.get();
		flag[i] = true;
		label[i] = getMaxLabel() + 1;
		//Waiting section
		for (int k = 0; k < n; k++) {
			while ((k != i) && flag[k] && ((label[k] < label[i]) || ((label[k] == label[i]) && k < i))) {
				//spin wait
			}
		}
	}
	public void unlock() {
		//Release lock
		flag[ThreadID.get()] = false;
	}
}
