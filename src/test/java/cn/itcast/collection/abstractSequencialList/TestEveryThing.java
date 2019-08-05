package cn.itcast.collection.abstractSequencialList;

/**
 * 	演示一下一个普通的类，可以被一个抽象类继承。
 *	一个普通的方法，可以被一个抽象方法重写。
 *   AbstractList 里面已经实现好的 listIterator() 方法，被其子类 AbstractSequentialList 用一个抽象方法重写了。
 * @author Administrator
 *
 */
public class TestEveryThing {
	class A {
		public void hello() {
			System.out.println("hello");
		}
	}
	
	abstract class B extends A{
		@Override
		public abstract void hello();
	}
}
