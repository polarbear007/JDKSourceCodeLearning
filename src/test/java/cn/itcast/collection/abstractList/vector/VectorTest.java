package cn.itcast.collection.abstractList.vector;

import java.util.Iterator;
import java.util.Vector;

import org.junit.Test;

public class VectorTest {
	class MyThread extends Thread{
		private Iterator<Integer> it;
		
		public MyThread(Iterator<Integer> it) {
			super();
			this.it = it;
		}

		@Override
		public void run() {
			while(it.hasNext()) {
				try {
					System.out.println(Thread.currentThread().getName() +"删除：" + it.next());
					it.remove();
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void testIterator() throws InterruptedException {
		Vector<Integer> v = new Vector<>();
		for (int i = 0; i < 20; i++) {
			v.add(i);
		}
		Iterator<Integer> it = v.iterator();
		
		Thread t1 = new MyThread(it);
		Thread t2 = new MyThread(it);
		Thread t3 = new MyThread(it);
		Thread t4 = new MyThread(it);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		// 注意： 一个迭代器内部的同步代码块只是用来保证一个迭代器对象在多个线程中同时迭代时
		//      一个元素并不会被重复遍历或者重复删除。
		//      但是如果我们通过外部的 Vector 方法添加或者删除一个元素，不管是在哪一个线程的操作
		//      所有的线程都会发现，并报并发修改异常。（这一点，就算迭代器内部没有同步代码块，也是可以发现的）
		v.add(1);
		
		// 为了防止Junit 强行中止所有线程，我们让主线程等待10秒
		Thread.sleep(100);
	}
}
