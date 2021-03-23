import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyALDAList<E> implements ALDAList<E> {

	private static class Node<E> {
		E data;
		Node<E> next;

		public Node(E data) {
			this.data = data;
		}
	}

	private Node<E> head;
	private Node<E> tail;

	@Override
	public void add(E element) {
		if (element == null) {
			throw new NullPointerException();
		}
		addAtTail(element);
	}

	// Distribuerar metodanrop beroende på var elementet ska sättas in
	@Override
	public void add(int index, E element) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			addAtHead(element);
		} else if (index == size()) {
			addAtTail(element);
		} else {
			addAtIndex(index, element);
		}
	}

	// Lägger till elementet längst fram
	private void addAtHead(E element) {
		if (size() == 0) {
			head = new Node<>(element);
			tail = head;
		} else {
			Node<E> newNode = new Node<>(element);
			newNode.next = head;
			head = newNode;
		}
	}

	// Lägger till elementet längst bak
	private void addAtTail(E element) {
		if (size() == 0) {
			addAtHead(element);
		} else {
			tail.next = new Node<>(element);
			tail = tail.next;
		}
	}

	// Lägger till elementet på den angivna positionen
	private void addAtIndex(int index, E element) {
		Node<E> temp = head;
		Node<E> newNode;
		for (int i = 0; i < index - 1; i++) {
			temp = temp.next;
		}
		newNode = new Node<>(element);
		newNode.next = temp.next;
		temp.next = newNode;
	}

	@Override
	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		Node<E> temp = head;
		for (int i = 0; i < index; i++) {
			temp = temp.next;
		}
		return temp.data;
	}

	@Override
	public int size() {
		Node<E> temp = head;
		int count = 0;
		while (temp != null) {
			temp = temp.next;
			++count;
		}
		return count;
	}

	@Override
	public void clear() {
		head = null;
	}

	@Override
	public boolean contains(E element) {
		if (element == null) {
			throw new NullPointerException();
		}
		Node<E> temp = head;
		while (temp != null) {
			if (temp.data == element || temp.data.equals(element)) {
				return true;
			}
			temp = temp.next;
		}
		return false;
	}

	@Override
	public int indexOf(E element) {
		if (element == null) {
			throw new NullPointerException();
		}
		int index = 0;
		Node<E> temp = head;
		while (temp != null) {
			if (temp.data == element || temp.data.equals(element)) {
				return index;
			} else {
				++index;
			}
			temp = temp.next;
		}
		return -1;
	}

	@Override
	public E remove(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		Node<E> temp = head;
		Node<E> old;
		if (index == 0) {
			head = head.next;
			return temp.data;
		} else {
			for (int i = 0; i < index - 1; i++) {
				temp = temp.next;
			}
			old = temp.next;
			if (temp.next == tail) {
				temp.next = null;
				tail = temp;
			} else {
				temp.next = temp.next.next;
			}
			return old.data;

		}
	}

	// Tar bort elementet om det ligger längst fram, anropar annars removeIfNotHead
	@Override
	public boolean remove(E element) {
		if (element == null) {
			throw new NullPointerException();
		}
		if (size() == 0) {
			return false;
		} else {
			if (head.data == element || head.data.equals(element)) {
				head = head.next;
				return true;
			} else {
				return removeIfNotHead(element);
			}
		}
	}

	// Tar bort den angivna elementet om det inte ligger längst fram
	public boolean removeIfNotHead(E element) {
		Node<E> temp = head;
		while (temp != null) {
			if (temp.next != null) {
				if (temp.next.data == element || temp.next.data.equals(element)) {
					if (temp.next.next != null) {
						temp.next = temp.next.next;
						return true;
					} else {
						temp.next = null;
						tail = temp;
						return true;
					}
				}
			}
			temp = temp.next;
		}
		return false;
	}

	@Override
	public String toString() {
		if (size() == 0) {
			return "[]";
		} else {
			String output = "[" + head.data;
			Node<E> temp = head.next;
			while (temp != null) {
				output += ", " + temp.data;
				temp = temp.next;
			}
			return output + "]";
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new MyALDAListIterator();
	}

	private class MyALDAListIterator implements Iterator<E> {
		Node<E> currentNode = head;
		Node<E> nextNode = head;
		private boolean removable = false;

		public boolean hasNext() {
			return nextNode != null;
		}

		public E next() {
			if (hasNext()) {
				currentNode = nextNode;
				nextNode = nextNode.next;
				removable = true;
				return currentNode.data;
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			if (removable) {
				MyALDAList.this.remove(currentNode.data);
				removable = false;
			} else {
				throw new IllegalStateException();
			}
		}
	}
}
