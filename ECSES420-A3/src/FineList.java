import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



class Node {
	private Lock lock;
	int key;
	Node next;

	public Node(int key){
		this.key = key;
		next = null;
		this.lock = new ReentrantLock();
	}

	public void lock() {
		lock.lock();
	}

	public void unlock() {
		lock.unlock();
	}

}

public class FineList {

	private Node head; 

	public FineList() {
		this.head = new Node (Integer.MIN_VALUE);
		head.next= new Node(Integer.MAX_VALUE);
	}
	
	  public static void main(String[] args) {
		  // Testing method
		    FineList testList = new FineList();
		    //Generate random list
		    for(int i = 0; i < 20; i++) {
		      int random= (int) (Math.random() * 100);
		      testList.add(random);
		      System.out.print(random + " -> ");
		    }
		    System.out.println();
		    // Check if values from 0-99 are contained, return true if yes false if not
		    for(int i = 0; i < 100; i++) {
		      System.out.println("Does my list contain " + i + ": " + testList.contains(i));
		    }
		  }
	
	
	

	public boolean add(int key) {
		head.lock(); 
		Node pred = head;
		try {
			Node curr = pred.next;
			curr.lock();
			try {
				while (curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				if (curr.key == key) {
					return false;
				}
				Node  newNode = new Node (key);
				newNode.next= curr;
				pred.next= newNode;
				return true;
			} finally {
				curr.unlock();
			}
		} finally {
			pred.unlock();
		}
	}

	public boolean remove(int key) {
		Node pred = null, curr = null;	
		head.lock();
		try {
			pred = head;
			curr = pred.next;
			curr.lock();
			try {
				while (curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				if (curr.key == key) {
					pred.next=curr.next;
					return true;
				}
				return false;
			} finally {
				curr.unlock();
			}
		} finally {
			pred.unlock();
		}
	}

	public boolean contains(int key) {
		Node pred = null, curr = null;
		head.lock();
		try {
			pred = head;
			curr = pred.next;
			curr.lock();
			try {
				while (curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				return curr.key == key;
			} finally { curr.unlock(); }
		} finally { pred.unlock(); }
	}

}
