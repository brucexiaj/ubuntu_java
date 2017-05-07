package com.totoro.classObject2;

interface Eat
{
	void eat();
}
interface Sleep
{
	
}
class Person
{
	public void print()
	{
		System.out.println("我是Person类。");
	}
}	
class Student extends Person implements Eat,Sleep
{
	private int age;
	private String name;
	public int getAge() 
	{
		return age;
	}
	public void setAge(int age) 
	{
		this.age = age;
	}
	public Student(int age, String name) {
		super();
		this.age = age;
		this.name = name;
	}
	public Student()
	{
		
	}
	@Override
	public void print()
	{
		System.out.println("我是Person的子类Student。");
	}
	
	@Override
	public void eat()
	{
		System.out.println("我是Studnet，正在eat");
	}
	
	
}
public class ClassObjectDemo
{
	public static void main(String[] arguments)
	{
		Class classStudent = Student.class;
		//获取类的名字
		System.out.println("类的完整的名字是："+classStudent.getName());
		//获取类的简单的名字
		System.out.println("类的简单的名字是："+classStudent.getSimpleName());
		//判断类是不是接口
		System.out.println("类是不是接口:"+classStudent.isInterface());
		//获取类的规范的名字，这个和完整的名字在内部类有区别，其他的地方似乎没有什么区别
		System.out.println("类的规范的名字是："+classStudent.getCanonicalName());
		//打印类实现的所有接口
		for(Class interFace:classStudent.getInterfaces())
		{
			System.out.println("类实现的接口："+interFace.getSimpleName());
		}
		//打印类的直接父类
		System.out.println("类的父类是："+classStudent.getSuperclass().getSimpleName());
		
	}
	
	
}
