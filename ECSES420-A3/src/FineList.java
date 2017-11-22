import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



class Node <T> implements Lock
{
	 T data;
	 int key;
	 Node <T> next;

	public Node(T item)
	{
		this.data = item;
		this.next = null;
	}




	public void lock() {
		// TODO Auto-generated method stub

	}



	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}



	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}



	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}



	public void unlock() {
		// TODO Auto-generated method stub

	}



	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}

public class FineList <T>{

	private Node <T> head; 
	private Lock lock = new ReentrantLock();
	@SuppressWarnings("unchecked")
	public FineList() {
		head = new Node (Integer.MIN_VALUE);
		head.next= new Node(Integer.MAX_VALUE);
	}

	public boolean add(T item) {
		int key = item.hashCode();

		head.lock(); 
		Node <T> pred = head;
		try {
			Node <T> curr = pred.next;
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
				Node <T> newNode = new Node <T> (item);
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

	public boolean remove(T item) {
		Node <T> pred = null, curr = null;
		int key = item.hashCode();
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

	public boolean contains(T item) {
		int key = item.hashCode();
		head.lock();
		Node <T> pred = head;
		try {
			Node <T> curr = pred.next;
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
