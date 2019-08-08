package cn.itcast.collection.abstractList;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

/**
 * 	想要通过继承 AbstractList 实现一个可修改的集合，我们不仅要实现 get 和 size 方法。
 * 	还需要另外实现 ： add / set /  remove 方法。
 * 
 * @author Administrator
 *
 * @param <E>
 */

public class ModifiableList<E> extends AbstractList<E>{
	private Object[] elementData;
	private int size;
	
	public ModifiableList() {
		super();
		// 如果使用默认构造器，我们就创建一个长度为 10 的Object[] 数组
		elementData = new Object[10];  
	}
	
	public ModifiableList(Collection<E> col) {
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
	
	@Override
	public void add(int index, E element) {
		rangeCheckForAdd(index);  // 先检查一下指定的索引是否越界
		// 没有问题的话，那么我们就再检查一下容量是否足够添加新元素
		ensureCapacity();
		// 如果容量没问题的话，那么我们就真正添加元素了
		// 我们先判断一下 index 是不是刚好添加在 size 位置，如果是的话，那么我们也不用位移了，直接添加即可
		if(size - index > 0) {
			System.arraycopy(elementData, index, elementData, index+1, size - index);
		}
		elementData[index] = element;
		// 维护一下 size 和 modCount
		size++;
		modCount++;
	}
	
	@Override
	public E set(int index, E element) {
		// 先确认一下index 索引是否越界
		rangeCheck(index);
		// 如果索引没有越界的话，那么我们先获取这个元素
		E old = get(index);
		// 然后我们需要使用新的元素去替换这个位置的元素
		elementData[index] = element;
		// 最后返回旧的元素即可
		return old;
	}
	
	@Override
	public E remove(int index) {
		// 先确认一下index 索引是否越界
		rangeCheck(index);
		// 如果索引没有越界的话，那么我们先获取这个元素
		E element = get(index);
		// 删除这个元素我们需要使用后面的元素来覆盖前面的元素
		System.arraycopy(elementData, index + 1, elementData, index, size - index);
		size--;
		modCount++;
		return element;
	}
	
	
	// 检查添加元素的时候，索引是否越界
	private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
	
	// set 或者 remove 方法执行前，都需要先确认一下是否会索引越界
	private void rangeCheck(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
	
	private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size();
    }
	
	// 确保集合的 elementData 数组长度足够保存添加的元素
	// 如果长度不够了，我们就进行扩容
	private void ensureCapacity() {
		int len = elementData.length;
		if(len == size()) {
			int newLen = len + (len / 2) + 1;
			// 检查一下，newLen 长度是否大于
			// 【注意】 一定不可以使用  newLen > Integer.Max_value - 8 ，因为可能会溢出，溢出以后变负数
			//        一个负数肯定小于Integer.MAX_VALUE - 8 ，这样会造成错误的判断
			if(newLen - (Integer.MAX_VALUE - 8) > 0) {
				// 如果原来数组的长度已经是极限长度了，那么我们直接扔异常
				// len 不可能为负数，所以我们不需要担心
				if(len > Integer.MAX_VALUE - 8) {
					throw new RuntimeException("集合太大了，内存不足");
				}
				
				// 如果集合本身的长度没有达到极限长度，那么我们就 创建一个极限长度的新数组
				elementData = Arrays.copyOf(elementData, Integer.MAX_VALUE - 8);
			}
			elementData = Arrays.copyOf(elementData, newLen);
		}
	}
}
