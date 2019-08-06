package cn.itcast.collection.abstractSequencialList;

import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 	注意： 可修改的List 集合依赖于 ListIterator 实现的 add / remove / set 等方法
 * 		 因为集合可修改，所以我们在遍历的时候，一定要检查 modCount 
 * 
 *	另外，因为 AbstractList 和 AbstractSequentialList 已经甚于 ListIterator 帮我们实现好了 增删改查逻辑
 *		所以我们在实现  add / remove / set 方法的时候，一定要按照他们的既定思路来走，不然就容易出问题。
 *		特别是 删除元素 和 修改元素的时候，为了返回旧值，会先调用一次 next() 方法，然后再调用 remove 或者 set 方法进行操作
 *		而我们在 next() 方法中已经移动了指针，所以删除和修改的时候，一定要注意是操作 preNode ，而不是 currNode
 * @author Administrator
 *
 * @param <E>
 */
public class ModifiableList<E> extends AbstractSequentialList<E> {
	// 链表的头结点
	private Node<E> headNode;
	// 链表的最后一个结点
	private Node<E> lastNode;
	private int size;

	public ModifiableList() {
		super();
	}

	/**
	 * 	跟AbstractList 不一样，因为这种顺序存取的集合底层并不是Object[]， 所以我们不能直接使用 toArray() 方法 的返回值来初始化
	 * 	不可修改的List 集合。 比较正常的思路是实现 addAll() 方法，一次性把另一个集合添加到本集合中来。但是 addAll()
	 * 	方法底层需要实现ListIterator 的 add() 方法，如果实现了这个方法，那么我们的集合又并不是 不可修改的了。
	 * 
	 * 	于是，我们只好使用最最底层的操作 链表的方法来实现了。这里我们甚至也不去使用什么 addAfter 之类的方法。
	 * 
	 * @param col
	 */
	public ModifiableList(Collection<? extends E> col) {
		super();
		if (col == null) {
			throw new RuntimeException("传入的集合不能为 null");
		}
		Node<E> newNode = null;
		for (E e : col) {
			newNode = new Node<E>(e);
			// 如果原来根结点为空，那么我们就让新结点为根结点，
			// 此时并不需要维护父结点的 next 属性， 也不需要维护新结点的 parent 属性，因为都是 null
			if (headNode == null) {
				headNode = newNode;
			} else {
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
	 * 	说明一下， ListIterator 要求支持双向遍历，我们这里以 正向遍历为主，让 next 直接返回 currNode 结点的值 让 previous
	 * 	返回 curNode.parent 结点的值
	 */
	@Override
	public ListIterator<E> listIterator(int index) {
		return new ListItr(index);
	}

	class ListItr implements ListIterator<E> {
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
			if (currIndex == size) {
				preNode = lastNode;
			}
		}

		@Override
		public boolean hasNext() {
			return currNode != null;
		}

		@Override
		public E next() {
			checkForConcurrModification();
			if (!hasNext()) {
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
			checkForConcurrModification();
			if (!hasPrevious()) {
				throw new NoSuchElementException("不存在此元素！");
			}
			// 如果前面没有扔异常，说明 preNode 不为 null
			E val = preNode.value;
			currNode = preNode;
			preNode = preNode.parent;
			currIndex--;
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

		/**
		 * 	【注意】 AbstractSequentialList 已经帮我们实现好了 remove 方法，所以我们必须得按他的思路来
		 * 			它删除的逻辑是，先调用 next() 方法返回要删除的结点的值， 然后再调用迭代器的 remove 方法来删除该结点
		 * 			但是我们在 next() 方法里面已经移动了 currIndex / currNode / preNode
		 * 			所以我们这里实际上需要删除的是 preNode
		 *    
		 *	【注意】 在上一个 next 方法中，如果 currNode 为 null ，那么会直接扔异常，也就不会走到这个方法，所以我们不用担心
		 *			链表为空的情况；也不用担心走到这个方法的时候， preNode 为null 的情况。 
		 *
		 */
		@Override
		public void remove() {
			checkForConcurrModification();
			// 这里我们需要放过 currIndex == size　的情况，因为 currIndex 在 next() 方法中自增了1
			if(currIndex < 0 || currIndex > size) {
				throw new NoSuchElementException();
			}
			
			// 调用删除方法删除掉 preNode
			ModifiableList.this.remove(preNode);
			// 然后更新一下 expectedModCount
			expectedModCount = modCount;
			// 再然后 preNode 已经被删除掉了，我们应该让 preNode 指向preNode.parent, currNode 不需要变
			preNode = preNode.parent;
			// currIndex 需要自减1
			currIndex--;
		}

		/**
		 *	【注意】 AbstractSequentialList 已经帮我们实现好了 set 方法，但是会调用迭代器的方法
		 *			我们得按照他写好的 set 方法来走。
		 *        	首先，会调用 next() 方法得到原来的那个元素的值。
		 *        	然后，会再调用一次 set()方法，替换值。===> 但是我们在 next() 方法里已经让 currIndex 往后移动了
		 *         		所以，所以判断 currIndex 等于 size 时，是没有越界的。因为我们实际上要替换的是 preNode 的值。
		 */
		@Override
		public void set(E e) {
			checkForConcurrModification();
			if(currIndex < 0 || currIndex > size) {
				throw new NoSuchElementException();
			}
			// 直接替换 preNode 的值就可以了
			preNode.value = e;
		}

		@Override
		public void add(E e) {
			checkForConcurrModification();
			if(currIndex < 0 || currIndex > size) {
				throw new IndexOutOfBoundsException();
			}
			// 如果currIndex 等于 size 的话，那么我们就直接给lastNode 后面添加一个新的结点
			// 因为这时候，currNode 是null
			Node<E> newNode = new Node<E>(e);
			if(currIndex == size) {
				ModifiableList.this.addAfter(lastNode, newNode);
			}else {
				// 如果 currNode 小于 size , 那么我们就添加在 currNode 前面
				ModifiableList.this.addBefore(currNode, newNode);
			}
			// 不管什么情况，我们都需要更新 一下 expectedModcount
			expectedModCount = modCount;
			// 然后 currNode 指向的位置应该是新添加的元素, currIndex 和 preNode 都不需要变化
			currNode = newNode;
		}
		
		/**
		 * 	检查modCount 是否跟迭代器内部的 expectedModCount 相同， 如果不同的话，说明在遍历期间，集合被修改了
		 */
		private void checkForConcurrModification() {
			if(modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
		}
	};

	@Override
	public int size() {
		return this.size;
	}

	/**
	 * 我们希望搞一个双向链表，所以每个结点内部都保存 next 和 parent 指针
	 * 
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
		if (headNode == null) {
			return null;
		}
		// 然后，我们应该确认一下 index 索引是否有效，如果是无效的索引，我们直接扔异常
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
		// 本来 index = size 时也算是无效索引，应该扔异常，
		// 但是在反向遍历的时候有时又需要返回放过这种情况，所以我们这里返回 null
		// 在需要扔异常的时候，判断一下本方法的返回值是否为 null 即可
		if (index == size) {
			return null;
		}

		int i = 0;
		Node<E> currNode = null;
		// 如果 size / 2 大于 index 的话，那么说明 index 离headNode 比较近，我们从 headNode 查起
		if (size / 2 >= index) {
			currNode = headNode;
			while (i < index) {
				currNode = currNode.next;
				i++;
			}
		} else {
			currNode = lastNode;
			i = size - 1;
			while (i > index) {
				currNode = currNode.parent;
				i--;
			}
		}
		return currNode;
	}

	/**
	 * 正常指定索引位置添加都是调用此方法
	 * 
	 * @param targetNode
	 * @param newNode
	 */
	private void addBefore(Node<E> targetNode, Node<E> newNode) {
		Node<E> parent = targetNode.parent;
		targetNode.parent = newNode;
		newNode.next = targetNode;
		newNode.parent = parent;

		// 判断一下，parent 结点如果不是 null 的话，我们还需要修改其 next ，指向 newNode
		if (parent != null) {
			parent.next = newNode;
		} else {
			// 如果 parent 结点是 null ，那么说明 targetNode 其实是根结点，那么添加以后，其实 newNode 变成根结点
			headNode = newNode;
		}

		// 不管结果怎么样，我们最终都需要维护 size 和 modCount
		size++;
		modCount++;
	}

	/**
	 *	在调用本方法前，一般都会先通过 node() 方法确认targetNode 结点， 传入的 targetNode 有两种情况：
	 *	1、 targetNode 是一个正常的结点，这种我们就正常添加即可【需要处理】
	 *	2、 targetNode 是一个 null ， node() 方法有两种情况会返回 null
	 *			一种就是索引值等于 size , 这种情况下，我们会控制直接传入 lastNode，于是实际就是走正常添加流程
	 *			一种就是 链表为空，那么我们就得直接让新结点成为头结点和最后一个结点【需要处理】
	 * @param targetNode
	 * @param newNode
	 */
	private void addAfter(Node<E> targetNode, Node<E> newNode) {
		// 如果 targetNode 为 null的话，那么只能是链表为空的情况
		if(targetNode == null) {
			headNode = newNode;
			lastNode = newNode;
		}else {
			Node<E> nextNode = targetNode.next;
			targetNode.next = newNode;
			newNode.parent = targetNode;
			newNode.next = nextNode;

			// 这里我们需要判断一下，nextNode 是否为空，如果不为空，我们需要维护一下 nextNode 的parent 属性
			if (nextNode != null) {
				nextNode.parent = newNode;
			} else {
				// 如果 nextNode 为空，说明 targetNode 其实是 lastNode ，那么添加以后， newNode 就变成lastNode
				lastNode = newNode;
			}
		}

		// 不管怎么样，添加结点以后，需要维护 size 和 modCount
		size++;
		modCount++;
	}

	/**
	 * 	删除指定结点
	 * 
	 * @param targetNode
	 * @return
	 */
	private void remove(Node<E> targetNode) {
		Node<E> parent = targetNode.parent;
		Node<E> nextNode = targetNode.next;

		// 如果父结点不为null ，说明 targetNode不是头结点，我们维护一下父结点的 next 属性
		if (parent != null) {
			parent.next = nextNode;
		} else {
			// 如果父结点为 null ，那么说明 targetNode是头结点，删除以后，我们需要重新维护 headNode
			headNode = nextNode;
		}

		// 如果下一个结点不为 null ，说明 targetNode 不是最后一个结点，我们需要维护一下 下一个结点的 parent 属性
		if (nextNode != null) {
			nextNode.parent = parent;
		} else {
			// 如果下一个结点为 null ，说明 targetNode 就是最后一个结点，删除以后，我们需要重新维护一下 lastNode
			lastNode = parent;
		}

		// 不管怎么样，我们都需要维护一下 size 和 modCount
		size--;
		modCount++;
	}

	private String outOfBoundsMsg(int index) {
		return "Index: " + index + ", Size: " + size();
	}

}
