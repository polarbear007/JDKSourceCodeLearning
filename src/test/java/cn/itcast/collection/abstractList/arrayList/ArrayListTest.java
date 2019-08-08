package cn.itcast.collection.abstractList.arrayList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.junit.Test;

public class ArrayListTest{
	@Test
	public void testWriteObject() throws IOException, ClassNotFoundException {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		// 写入 list 集合到输出流
		oos.writeObject(list);
		byte[] bys = bos.toByteArray();
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bys));
		ArrayList<Integer> obj = (ArrayList<Integer>)ois.readObject();
		System.out.println(obj);
		
		ois.close();
		
		oos.close();
		bos.close();
	}
}
