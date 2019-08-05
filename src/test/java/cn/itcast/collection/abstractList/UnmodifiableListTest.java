package cn.itcast.collection.abstractList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

public class UnmodifiableListTest {
	@Test
	public void testGet() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		UnmodifiableList<Integer> unmodList = new UnmodifiableList<>(list);
		System.out.println("size: " + unmodList.size());
		System.out.println("get(2) = " + unmodList.get(2));
	}
	
	@Test
	public void testIterator() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		UnmodifiableList<Integer> unmodList = new UnmodifiableList<>(list);
		// 增强for 遍历
		for (Integer i : unmodList) {
			System.out.println(i);
		}
		System.out.println("****************");
		// 普通迭代器遍历
		Iterator<Integer> it = unmodList.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		System.out.println("****************");
		// ListIterator 遍历
		ListIterator<Integer> listIterator = unmodList.listIterator(2);
		while(listIterator.hasNext()) {
			System.out.println(listIterator.next());
		}
		System.out.println("****************");
		// 普通 for 遍历
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		System.out.println("****************");
	}
	
	@Test
	public void testAdd() {
		UnmodifiableList<Integer> unmodList = new UnmodifiableList<>();
		unmodList.add(1);
	}
	
	@Test
	public void testRemove() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		UnmodifiableList<Integer> unmodList = new UnmodifiableList<>(list);
		unmodList.remove(0);
	}
	
	@Test
	public void testSet() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		UnmodifiableList<Integer> unmodList = new UnmodifiableList<>(list);
		unmodList.set(2, 100);
	}
	
	@Test
	public void testSubList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		UnmodifiableList<Integer> unmodList = new UnmodifiableList<>(list);
		List<Integer> subList = unmodList.subList(2, 5);
		System.out.println(subList);
	}
}
