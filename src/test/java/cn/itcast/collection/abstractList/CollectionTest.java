package cn.itcast.collection.abstractList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class CollectionTest {
	// 测试一下两个集合是否相等的方法
	// List 集合相等，要求类型同样，集合长度相同，集合里面的元素值相同，元素的顺序也相同 
	@Test
	public void test1() {
		ArrayList<Integer> arr1 = new ArrayList<Integer>();
		arr1.add(1);
		arr1.add(2);

		ArrayList<Integer> arr2 = new ArrayList<Integer>();
		arr2.add(1);
		arr2.add(2);
//		arr2.add(3);

		System.out.println(arr1.equals(arr2));
	}

	// 类型不同，但是元素值都一样的两个集合是否相等？
	@Test
	public void test2() {
		ArrayList<Integer> arr1 = new ArrayList<Integer>();
		arr1.add(1);
		arr1.add(2);

		HashSet<Integer> set = new HashSet<Integer>();
		set.add(1);
		set.add(2);

		System.out.println(arr1.equals(set));
	}

	// 演示一个对象添加到 HashSet 集合以后，如果我们改变了这个对象的成员变量值，会出现什么问题？
	@Test
	public void test3() {
		HashSet<Student> set = new HashSet<Student>();
		Student stu1 = new Student(1, "eric");
		Student stu2 = new Student(2, "rose");
		Student stu3 = new Student(3, "jack");
		set.add(stu1);
		set.add(stu2);
		set.add(stu3);
		System.out.println(set);
		// 修改 stu1 的 id 值  
		stu1.setId(10);
		// 集合里面确实可以看到 我们的修改
		System.out.println(set);

		// 现在我们知道，set 集合里面肯定是包含 stu1 对象的
		//  可是 contains() 方法却返回 false 
		System.out.println(set.contains(stu1));
	}
	
	 // 演示一个对象添加到 TreeSet 集合以后，如果我们改变了这个对象的成员变量值，会出现什么问题？
	@Test
	public void test4() {
		TreeSet<Teacher> set = new TreeSet<>(new Comparator<Teacher>() {
			@Override
			public int compare(Teacher t1, Teacher t2) {
				int num1 = t1.getId() - t2.getId();
				int num2 = num1 == 0 ? t1.getName().compareTo(t2.getName()) : num1;
				return num2;
			}
		});
		
		Teacher t1 = new Teacher(1, "eric");
		Teacher t2 = new Teacher(2, "rose");
		Teacher t3 = new Teacher(3, "jack");
		Teacher t4 = new Teacher(4, "tom");
		set.add(t1);
		set.add(t2);
		set.add(t3);
		set.add(t4);
		// treeSet 集合里面的元素是有序的
		System.out.println(set);
		// 修改一下 t1 的id 值。
		t1.setId(10);
		// treeSet 里面的元素应该是有序的，但是现在 顺序却乱了。 第一个元素的 id 为10， 应该排在后面，但是却排在最前面
		System.out.println(set);
		// 更严重的问题在于： 我们知道 集合里面肯定是存在 t1 对象的，但是 contains() 方法却返回 false
		System.out.println(set.contains(t1));
	}
	
	// 演示一个对象添加到 HashMap 集合以后，如果我们改变了这个对象的成员变量值，会出现什么问题？
	@Test
	public void test5() {
		HashMap<Student, Integer> map = new HashMap<Student, Integer>();
		Student stu1 = new Student(1, "eric");
		Student stu2 = new Student(2, "rose");
		Student stu3 = new Student(3, "jack");
		map.put(stu1, stu1.getId());
		map.put(stu2, stu2.getId());
		map.put(stu3, stu3.getId());
		System.out.println(map);
		// 修改 stu1 的 id 值  
		stu1.setId(10);
		// 集合里面确实可以看到 我们的修改
		System.out.println(map);

		// 现在我们知道，map 集合里面肯定是包含 stu1 对象的
		//  可是 containsKey() 方法却返回 false 
		System.out.println(map.containsKey(stu1));
	}
		
	 // 演示一个对象添加到 TreeMap 集合以后，如果我们改变了这个对象的成员变量值，会出现什么问题？
	@Test
	public void test6() {
		TreeMap<Teacher, Integer> map = new TreeMap<>(new Comparator<Teacher>() {
			@Override
			public int compare(Teacher t1, Teacher t2) {
				int num1 = t1.getId() - t2.getId();
				int num2 = num1 == 0 ? t1.getName().compareTo(t2.getName()) : num1;
				return num2;
			}
		});
		
		Teacher t1 = new Teacher(1, "eric");
		Teacher t2 = new Teacher(2, "rose");
		Teacher t3 = new Teacher(3, "jack");
		Teacher t4 = new Teacher(4, "tom");
		map.put(t1, t1.getId());
		map.put(t2, t2.getId());
		map.put(t3, t3.getId());
		map.put(t4, t4.getId());
		// treeMap 集合里面的元素是有序的
		System.out.println(map);
		// 修改一下 t1 的id 值。
		t1.setId(10);
		// treeMap 里面的元素应该是有序的，但是现在 顺序却乱了。 第一个元素的 id 为10， 应该排在后面，但是却排在最前面
		System.out.println(map);
		// 更严重的问题在于： 我们知道 集合里面肯定是存在 t1 对象的，但是 containsKey() 方法却返回 false
		System.out.println(map.containsKey(t1));
	}

	class Student {
		private int id;
		private String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Student() {
			super();
		}

		public Student(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + id;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Student other = (Student) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (id != other.id)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private CollectionTest getEnclosingInstance() {
			return CollectionTest.this;
		}

		@Override
		public String toString() {
			return "Student [id=" + id + ", name=" + name + "]";
		}
	}

	class Teacher {
		private int id;
		private String name;
		
		public Teacher() {
			super();
		}

		public Teacher(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Teacher [id=" + id + ", name=" + name + "]";
		}
	}
}
