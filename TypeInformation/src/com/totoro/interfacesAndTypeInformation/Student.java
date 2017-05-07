package com.totoro.interfacesAndTypeInformation;

public class Student
{
	private String name;
	private void privateMethod()
	{
		System.out.println("我是同一个包中不同类的private方法。");
	}
	protected void protectedMethod()
	{
		System.out.println("我是同一个包中不同类的protected方法。");
	}
}
