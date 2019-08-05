package cn.itcast.collection.abstractList;

import java.util.AbstractList;
import java.util.Collection;

public class UnmodifiableList<E> extends AbstractList<E> {
	private Object[] elementData;
	private int size;
	
	public UnmodifiableList() {
		super();
		// 如果使用默认构造器，我们就创建一个长度为0的Object[] 数组
		elementData = new Object[0];  
	}
	
	public UnmodifiableList(Collection<E> col) {
		super();
		if(col == null) {
			throw new RuntimeException("传入的集合不能为null");
		}
		// 如果使用的是带参构造，那么我们就把传入的集合使用 toArray() 方法，转成 Object[] 数组
		// 然后直接赋值给  elementData 就可以了
		elementData = col.toArray();
		this.size = elementData.length;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		return (E)elementData[index];
	}

	@Override
	public int size() {
		return this.size;
	}
}
