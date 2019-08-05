package cn.itcast.collection.abstractList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class SubListTest {
	// 子集合里面的修改，是否会影响父集合？
	@Test
	public void test1() {
		ArrayList<Student> list = new ArrayList<Student>();
		list.add(new Student(0, "eric"));
		list.add(new Student(1, "rose"));
		list.add(new Student(2, "jack"));
		list.add(new Student(3,  "tom"));
		list.add(new Student(4,  "jerry"));
		List<Student> subList = list.subList(1, 4);  // [1, 4) 包左不包右
		System.out.println("子集：" + subList);
		
		// 然后，我们往子集合里面添加新元素，看看父集合是否会受到影响 
		subList.add(new Student(5, "jerry"));
		System.out.println("添加元素以后：");
		System.out.println("子集： " + subList);
		System.out.println("父集：" + list);
		
		// 然后，我们给子集合进行排序，看看父集合是否会受到影响 
		// 这里我们故意使用姓名的字母顺序进行排序
		subList.sort(new Comparator<Student>() {
			@Override
			public int compare(Student s1, Student s2) {
				return s1.getName().compareTo(s2.getName());
			}
		});
		System.out.println("对子集合进行排序以后：");
		System.out.println("子集： " + subList);
		System.out.println("父集：" + list);
	}
	
	// 增删原集合的元素，是否会造成子集合抛异常？
	@Test
	public void test2() {
		ArrayList<Student> list = new ArrayList<Student>();
		list.add(new Student(0, "eric"));
		list.add(new Student(1, "rose"));
		list.add(new Student(2, "jack"));
		list.add(new Student(3,  "tom"));
		list.add(new Student(4,  "jerry"));
		
		List<Student> subList = list.subList(2, 4);
		System.out.println("子集" + subList);
		list.add(new Student(5, "mary"));   // 增删的操作会影响父集合的 modCount 
		System.out.println("修改了父集合：");
		System.out.println("子集" + subList);  // 子集合一旦发现 modCount 跟父集合不一样，就直接扔并发异常
	}
	
	// 对原集合进行排序，是否会造成子集合扔异常？
	@Test
	public void test3() {
		ArrayList<Student> list = new ArrayList<Student>();
		list.add(new Student(0, "eric"));
		list.add(new Student(1, "rose"));
		list.add(new Student(2, "jack"));
		list.add(new Student(3,  "tom"));
		list.add(new Student(4,  "jerry"));
		List<Student> subList = list.subList(2, 4);
		list.sort(new Comparator<Student>() {
			@Override
			public int compare(Student s1, Student s2) {
				return s1.getName().compareTo(s2.getName());
			}
		});
		System.out.println("对父集合进行排序：");
		System.out.println("子集" + subList);  
	}
	
	// 测试修改 List 集合是否会造成 子集合扔异常？
	@Test
	public void test4() {
		ArrayList<Student> list = new ArrayList<Student>();
		list.add(new Student(0, "eric"));
		list.add(new Student(1, "rose"));
		list.add(new Student(2, "jack"));
		list.add(new Student(3,  "tom"));
		list.add(new Student(4,  "jerry"));
		List<Student> subList = list.subList(2, 4);
		// 对原集合使用 set 方法修改元素，子集合是否会抛异常？
		list.set(0, new Student(0, "小明"));
		System.out.println("对父集合的某个元素进行替换：");
		System.out.println("子集" + subList);  
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

		private SubListTest getEnclosingInstance() {
			return SubListTest.this;
		}

		@Override
		public String toString() {
			return "Student [id=" + id + ", name=" + name + "]";
		}
	}
}
