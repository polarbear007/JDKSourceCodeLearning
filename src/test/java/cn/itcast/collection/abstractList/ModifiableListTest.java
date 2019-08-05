package cn.itcast.collection.abstractList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

public class ModifiableListTest {
	@Test
	public void testGet() {
		ModifiableList<Integer> list = new ModifiableList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		System.out.println(list.get(0));
		System.out.println(list.get(2));
	}
	
	@Test
	public void testAdd() {
		ModifiableList<Integer> list = new ModifiableList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		// 看看索引是否会越界
		list.add(5, 4);
	}
	
	// 看看是否会自动扩容，需要debug 查看
	@Test
	public void testCapacity() {
		ModifiableList<Integer> list = new ModifiableList<>(Arrays.asList(new Integer[]{1, 2, 3, 4}));
		list.add(5);
		System.out.println(list);
	}
	
	@Test
	public void testSet() {
		ModifiableList<Integer> list = new ModifiableList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		list.set(0, 100);
		System.out.println(list);
	}
	
	@Test
	public void testRemove() {
		ModifiableList<Integer> list = new ModifiableList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		list.remove(0);
		System.out.println(list);
	}
	
	@Test
	public void testIterator() {
		ModifiableList<Integer> list = new ModifiableList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		// 普通for 
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		System.out.println("*******************");
		// 增强for
		for (Integer i : list) {
			System.out.println(i);
		}
		System.out.println("*******************");
		// 普通 iterator
		Iterator<Integer> it = list.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		System.out.println("*******************");
		// ListIterator
		ListIterator<Integer> listIt = list.listIterator(1);
		while(listIt.hasNext()) {
			System.out.println(listIt.next());
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
		subList.clear();
		System.out.println(list);
	}
	
}
