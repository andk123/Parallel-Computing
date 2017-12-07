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
		    //Add values to list
		    System.out.println("Adding 2 to list");
		    testList.add(2);
		    System.out.println("Adding 6 to list");
		    testList.add(6);
		    System.out.println("Adding 10 to list");
		    testList.add(10);
		    System.out.println("Adding 13 to list");
		    testList.add(13);
		    
		    System.out.println("Does my list contain 4 "  + ": " + testList.contains(4));
		    System.out.println("Does my list contain 6 "  + ": " + testList.contains(6));
		    System.out.println("Does my list contain 3 "  + ": " + testList.contains(3));
		    System.out.println("Does my list contain 2 "  + ": " + testList.contains(2));
		    System.out.println("Does my list contain 13 "  + ": " + testList.contains(13));

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
