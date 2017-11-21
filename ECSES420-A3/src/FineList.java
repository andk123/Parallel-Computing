import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;



class Node implements Lock
{
	private int data;
	private int key;
	private Node next;

	public Node(int data)
	{
		this.setData(data);
	}



	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}



	public int getData() {
		return data;
	}



	public void setData(int data) {
		this.data = data;
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



	public int getKey() {
		return key;
	}



	public void setKey(int key) {
		this.key = key;
	}
}

public class FineList {

	private Node head;
	public FineList() {
		head = new Node(Integer.MIN_VALUE);
		head.setNext(new Node(Integer.MAX_VALUE));
	}

	public boolean add(Node item) {
		int key = item.hashCode();

		head.lock(); 
		Node pred = head;
		try {
			Node curr = pred.getNext();
			curr.lock();
			try {
				while (curr.getKey() < key) {
					pred.unlock();
					pred = curr;
					curr = curr.getNext();
					curr.lock();
				}
				if (curr.getKey() == key) {
					return false;
				}
				Node newNode = item;
				newNode.setNext(curr);
				pred.setNext(newNode);
				return true;
			} finally {
				curr.unlock();
			}
		} finally {
			pred.unlock();
		}
	}

	public boolean remove(Node item) {
		Node pred = null, curr = null;
		int key = item.hashCode();
		head.lock();
		try {
			pred = head;
			curr = pred.getNext();
			curr.lock();
			try {
				while (curr.getKey() < key) {
					pred.unlock();
					pred = curr;
					curr = curr.getNext();
					curr.lock();
				}
				if (curr.getKey() == key) {
					pred.setNext(curr.getNext());
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

	public boolean contains(Node item) {
		int key = item.hashCode();
		head.lock();
		Node pred = head;
		try {
			Node curr = pred.getNext();
			curr.lock();
			try {
				while (curr.getKey() < key) {
					pred.unlock();
					pred = curr;
					curr = curr.getNext();
					curr.lock();
				}
				return curr.getKey() == key;
			} finally { curr.unlock(); }
		} finally { pred.unlock(); }
	}

}
