package cn.itcast.collection.abstractList;

import org.junit.Test;

public class TestEveryThing {
	// 演示一下 2 * i - Integer.MAX_VALUE > 0   跟   2 * i > Integer.MAX_VALUE 之间的区别
	@Test
	public void test1() {
		int i = Integer.MAX_VALUE;
		// 2 * i 的结果是 -2 , 但是再减去 Integer.Max_value 的话，又会变成正数， 最后结果是 true 
		System.out.println(2 * i - Integer.MAX_VALUE > 0);
		// 这里是 -2 跟 Integer.MAX_VALUE 比较，一个负数，一个正数，最后结果为 false
		System.out.println(2 * i > Integer.MAX_VALUE);
	}
}
