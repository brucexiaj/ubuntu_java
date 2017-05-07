package com.totoro.common;

public class Teacher 
{
	private String name;
	private void privateMethod()
	{
		System.out.println("我是不同包的private方法。");
	}
	protected void protectedMethod()
	{
		System.out.println("我是不同包的protected方法。");
	}
	public  void publicMethod()
	{
		System.out.println("我是不同包的public方法。");
	}
}
