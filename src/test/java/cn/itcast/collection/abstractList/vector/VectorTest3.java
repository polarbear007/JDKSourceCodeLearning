package cn.itcast.collection.abstractList.vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class VectorTest3 {
	class Consumer extends Thread{
		private List<Integer> list;
		
		public Consumer(List<Integer> list) {
			super();
			this.list = list;
		}

		
		@Override
		public void run() {
			while(true) {
				// 故意不添加同步代码块
				synchronized (list) {
					int size = list.size();
					if(size > 0) {
						System.out.println("消费：" + list.get(size - 1));
						list.remove(size - 1);
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
			int i = 0;
			while(true) {
				synchronized (list) {
					int size = list.size();
					if(size < 10) {
						
						//System.out.println("添加： " + i);
						list.add(i++);
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
					}
				}
			}
		}
	}
	
	// 使用生产者-消费者模型，来看看在使用同步代码块的情况下， Vector 和 ArrayList 有没有什么区别？
	@Test
	public void test1() throws InterruptedException {
		Vector<Integer> v = new Vector<Integer>();
		Consumer consumer = new Consumer(v);
		Producer producer = new Producer(v);
		consumer.start();
		producer.start();
		
		Thread.sleep(20000);
	}
	
	@Test
	public void test2() throws InterruptedException {
		ArrayList<Integer> list = new ArrayList<Integer>();
		Consumer consumer = new Consumer(list);
		Producer producer = new Producer(list);
		consumer.start();
		producer.start();
		
		Thread.sleep(20000);
	}
}
