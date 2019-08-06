package cn.itcast.collection.abstractSequencialList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

public class ModifiableListTest {
	@Test
	public void testAdd() {
		ModifiableList<Integer> list = new ModifiableList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		System.out.println(list);
		System.out.println(list.size());
	}
	
	@Test
	public void testAddAll() {
		ModifiableList<Integer> list = new ModifiableList<Integer>();
		list.addAll(Arrays.asList(new Integer[] {1,2,3,4,5}));
		System.out.println(list);
		System.out.println(list.size());
	}
	
	@Test
	public void testAddByIndex() {
		ModifiableList<Integer> list = new ModifiableList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(0, 100);
		System.out.println(list);
		System.out.println(list.size());
	}
	
	@Test
	public void testGet() {
		ModifiableList<Integer> list = new ModifiableList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		System.out.println(list.get(2));
	}
	
	@Test
	public void testSet() {
		ModifiableList<Integer> list = new ModifiableList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.set(2, 1000);
		list.set(0, 2000);
		System.out.println(list);
	}
	
	@Test
	public void testRemove() {
		ModifiableList<Integer> list = new ModifiableList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.remove(0);
		System.out.println(list);
	}
	
	@Test
	public void testClear() {
		ModifiableList<Integer> list = new ModifiableList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.clear();
		System.out.println(list);
	}
	
	@Test
	public void testIterator() {
		ModifiableList<Integer> list = new ModifiableList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		// 普通for , 但是不建议这样搞，因为每次 get 都会调用 node() 方法找到对应的结点，这需要花费至少 O(n/2)
		// 然后，我们总共调用了 n 次。 相当于需要   O(n^2 / 2)
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		System.out.println("*******************");
		// 增强for 
		for (Integer i : list) {
			System.out.println(i);
		}
		System.out.println("*******************");
		// 普通的 Iterator
		Iterator<Integer> it = list.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		System.out.println("*******************");
		// ListIterator
		ListIterator<Integer> listIt = list.listIterator(2);
		while(listIt.hasNext()) {
			System.out.println(listIt.next());
		}
		System.out.println("*******************");
		// 测试一下逆向遍历
		listIt = list.listIterator(list.size());
		while(listIt.hasPrevious()) {
			System.out.println(listIt.previous());
		}
	}
	
	@Test
	public void testSubList() {
		ModifiableList<Integer> list = new ModifiableList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		List<Integer> subList = list.subList(2, 5);
		System.out.println(subList);
		subList.clear();
		System.out.println(list);
	}
}
