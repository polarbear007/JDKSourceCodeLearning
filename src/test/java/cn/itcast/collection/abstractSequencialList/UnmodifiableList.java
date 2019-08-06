package cn.itcast.collection.abstractSequencialList;

import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 	这里我们使用双向链表来实现 List 集合。
 * 
 * 	不可修改的集合，我们只需要实现 ListIterator 接口，并实现迭代的基本逻辑就可以了。
 * 	也就是说，我们只需要实现 Listiterator 接口的 hasNext() / next() / hasPrevious() / previous() 这几个方法
 * 	就可以了。
 * 
 * 	因为不存在修改集合的可能，所以我们也不用考虑检查什么 modCount 了
 * 
 * 	另外，因为addAll() 方法其实是依赖于 ListIterator 接口的 add 方法的，而我们为了防止集合被修改，所以这个方法只会扔异常。
 * 	于是，我们就在带参构造函数中，使用手动的方式，直接操作 结点，添加另一个集合的全部元素到链表中
 * @author Administrator
 *
 * @param <E>
 */
public class UnmodifiableList<E> extends AbstractSequentialList<E> {
	// 链表的头结点
	private Node<E> headNode;
	// 链表的最后一个结点
	private Node<E> lastNode;
	private int size;
	
	public UnmodifiableList() {
		super();
	}
	
	/**
	 * 	跟AbstractList 不一样，因为这种顺序存取的集合底层并不是Object[]， 所以我们不能直接使用 toArray() 方法
	 * 	的返回值来初始化 不可修改的List 集合。
	 * 	比较正常的思路是实现 addAll() 方法，一次性把另一个集合添加到本集合中来。但是 addAll() 方法底层需要实现ListIterator
	 * 	的 add() 方法，如果实现了这个方法，那么我们的集合又并不是 不可修改的了。
	 * 	
	 * 	于是，我们只好使用最最底层的操作  链表的方法来实现了。这里我们甚至也不去使用什么 addAfter 之类的方法。
	 * @param col
	 */
	public UnmodifiableList(Collection<? extends E> col) {
		super();
		if(col == null) {
			throw new RuntimeException("传入的集合不能为 null");
		}
		Node<E> newNode = null;
		for (E e : col) {
			newNode = new Node<E>(e);
			// 如果原来根结点为空，那么我们就让新结点为根结点，
			// 此时并不需要维护父结点的 next 属性， 也不需要维护新结点的 parent 属性，因为都是 null
			if(headNode == null) {
				headNode = newNode;
			}else {
				lastNode.next = newNode;
				newNode.parent = lastNode;
			}
			// 不管什么情况，最后 lastNode 肯定是指向 newNode 的
			lastNode = newNode;
			// 不管是什么情况， size ++
			size++;
		}
		modCount++;
	}

	/**
	 * 	说明一下， ListIterator 要求支持双向遍历，我们这里以 正向遍历为主，让 next 直接返回  currNode 结点的值
	 * 	让 previous 返回 curNode.parent 结点的值
	 */
	@Override
	public ListIterator<E> listIterator(int index) {
		return new ListItr(index);
	}
	
	class ListItr implements ListIterator<E>{
		Node<E> currNode;
		Node<E> preNode;   
		int currIndex;
		int expectedModCount = modCount;
		
		public ListItr(int currIndex) {
			super();
			this.currIndex = currIndex;
			currNode = node(currIndex);
			// preNode 的取值严重依赖于 currNode
			preNode = currNode == null ? null : currNode.parent;
			// 但是存在一种情况下， preNode 应该独立于 currNode ；
			// 当 currIndex == size 的时候，我们应该明确 currNode 肯定是 null ，但是其实 preNode 不应该为 null
			// 这时候preNode 应该为 lastNode
			if(currIndex == size) {
				preNode = lastNode;
			}
		}

		@Override
		public boolean hasNext() {
			return currNode != null;
		}

		@Override
		public E next() {
			if(!hasNext()) {
				throw new NoSuchElementException("不存在此元素！");
			}
			// 如果前面没有报异常的话，那么我们就直接返回 currNode 的值就可以了
			E val = currNode.value;
			preNode = currNode;
			currNode = currNode.next;
			currIndex++;
			return val;
		}

		@Override
		public boolean hasPrevious() {
			return preNode != null;
		}

		@Override
		public E previous() {
			if(!hasPrevious()) {
				throw new NoSuchElementException("不存在此元素！");
			}
			// 如果前面没有扔异常，说明 preNode 不为 null 
			E val = preNode.value;
			currNode = preNode;
			preNode = preNode.parent;
			currIndex --;
			return val;
		}

		@Override
		public int nextIndex() {
			return currIndex;
		}

		@Override
		public int previousIndex() {
			return currIndex - 1;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("这是不可修改的List 集合，不支持此操作");
		}

		@Override
		public void set(E e) {
			throw new UnsupportedOperationException("这是不可修改的List 集合，不支持此操作");
		}

		@Override
		public void add(E e) {
			throw new UnsupportedOperationException("这是不可修改的List 集合，不支持此操作");
		}
	};

	@Override
	public int size() {
		return this.size;
	}

	/**
	 * 	我们希望搞一个双向链表，所以每个结点内部都保存 next 和 parent 指针
	 * @author Administrator
	 *
	 * @param <E>
	 */
	@SuppressWarnings("hiding")
	class Node<E> {
		E value;
		Node<E> parent;
		Node<E> next;
		
		public Node() {
			super();
		}

		public Node(E value) {
			super();
			this.value = value;
		}

		@Override
		public String toString() {
			return "Node [value=" + value + "]";
		}
	}
	
	// 根据索引值定位链表中的结点
	// 因为是双向链表，所以我们可以先看下指定的索引靠近头结点还是最后结点
	// 我们尽量从较近的结点开始找
	private Node<E> node(int index) {
		// 首先，看下头结点是不是空，如果是空的话，那么也不用定位了，我们直接返回 null
		if(headNode == null) {
			return null;
		}
		// 然后，我们应该确认一下 index 索引是否有效，如果是无效的索引，我们直接扔异常
		if(index < 0 || index > size()) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
		// 本来 index = size 时也算是无效索引，应该扔异常，
		// 但是在反向遍历的时候有时又需要返回放过这种情况，所以我们这里返回 null
		// 在需要扔异常的时候，判断一下本方法的返回值是否为 null 即可
		if(index == size) {
			return null;
		}
		
		int i = 0;
		Node<E> currNode = null;
		// 如果 size / 2 大于 index 的话，那么说明 index 离headNode 比较近，我们从 headNode 查起
		if(size / 2 >= index) {
			currNode = headNode;
			while(i < index) {
				currNode = currNode.next;
				i++;
			}
		}else {
			currNode = lastNode;
			i = size - 1;
			while(i > index) {
				currNode = currNode.parent;
				i--;
			}
		}
		return currNode;
	}
	
	private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size();
    }
}
