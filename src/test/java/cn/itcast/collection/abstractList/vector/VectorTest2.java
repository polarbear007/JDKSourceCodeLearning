package cn.itcast.collection.abstractList.vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class VectorTest2 {
	class Consumer extends Thread{
		private List<Integer> list;
		private int[] count;
		
		public Consumer(List<Integer> list, int[] count) {
			super();
			this.list = list;
			this.count = count;
		}
		
		@Override
		public void run() {
			while(true) {
				synchronized (list) {
					int size = list.size();
					if(size > 0) {
						Integer i = list.get(size - 1);
						//System.out.println("消费：" + i);
						list.remove(size - 1);
						count[i]++;
					}
				}
			}
		}
	}
	
	class Producer extends Thread{
		private List<Integer> list;

		public Producer(List<Integer> list) {
			super();
			this.list = list;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < 10000; i++) {
//				synchronized (list) {
//					list.add(i);
//				}
				list.add(i);
			}
		}
	}
	
	// 当我们使用同步代码块时， ArrayList 和  Vector 是否都能解决线程安全问题？
	@Test
	public void test1() throws InterruptedException {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 10000; i++) {
			list.add(i);
		}
		int[] count = new int[list.size()];
		
		Consumer c1 = new Consumer(list, count);
		Consumer c2 = new Consumer(list, count);
		Consumer c3 = new Consumer(list, count);
		
		c1.start();
		c2.start();
		c3.start();
		
		Thread.sleep(10000);
		
		// 检查 count 数组中，有没有哪个元素被被消费多次 或者 没有被消费
		for (int i = 0; i < count.length; i++) {
			if(count[i] != 1) {
				System.out.println(i + "这个元素被消费了" + count[i] + "次");
			}
		}
	}
	
	// 当我们使用同步代码块时， ArrayList 和  Vector 是否都能解决线程安全问题？
	@Test
	public void test2() throws InterruptedException {
		Vector<Integer> list = new Vector<Integer>();
		for (int i = 0; i < 10000; i++) {
			list.add(i);
		}
		int[] count = new int[list.size()];
		
		Consumer c1 = new Consumer(list, count);
		Consumer c2 = new Consumer(list, count);
		Consumer c3 = new Consumer(list, count);
		
		c1.start();
		c2.start();
		c3.start();
		
		Thread.sleep(10000);
		
		// 检查 count 数组中，有没有哪个元素被被消费多次 或者 没有被消费
		for (int i = 0; i < count.length; i++) {
			if(count[i] != 1) {
				System.out.println(i + "这个元素被消费了" + count[i] + "次");
			}
		}
	}
	
	// 添加的逻辑就只用到一个 add() 方法时， 不加同步代码块的话，ArrayList 是否会有线程安全问题？
	@Test
	public void test3() throws InterruptedException {
		ArrayList<Integer> list = new ArrayList<Integer>();
		Producer p1 = new Producer(list);
		Producer p2 = new Producer(list);
		Producer p3 = new Producer(list);
		p1.start();
		p2.start();
		p3.start();
		
		Thread.sleep(10000);
		System.out.println(list.size());
	}
	
	// 添加的逻辑就只用到一个 add() 方法时， 不加同步代码块的话，Vector 是否会有线程安全问题？
	@Test
	public void test4() throws InterruptedException {
		Vector<Integer> list = new Vector<Integer>();
		Producer p1 = new Producer(list);
		Producer p2 = new Producer(list);
		Producer p3 = new Producer(list);
		p1.start();
		p2.start();
		p3.start();
		
		Thread.sleep(10000);
		System.out.println(list.size());
	}
}
