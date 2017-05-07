package com.totoro.genericClassReference;

public class GenericClassReference
{
	public static void main(String[] args)
	{
		//普通的类引用
		Class intClass = int.class;
		//泛型类引用
		Class<Integer> genericIntClass = int.class;
		//包装类的引用
		Class integerClass1 = Integer.class;
		//通过Type实现的包装类的引用
		Class integerClass2 = Integer.TYPE; 
		//泛型类引用的重新赋值
		genericIntClass = Integer.class;
		//普通类引用的重新赋值
		intClass = double.class;
		//genericIntClass = double.class;这里不能运行
		//泛型的类引用，在尖括号中加上问号后，重新赋值不再有限制
		Class<?> genericIntClass2 = int.class;
		genericIntClass2 = double.class;
		genericIntClass2 = String.class;
		//限定了范围的泛型的类引用，重新赋值只能是Number和它的子类
		Class<? extends Number> genericNumberClass = Number.class;
		genericNumberClass = int.class;
		genericNumberClass = double.class;
		//genericNumberClass = String.class;无法运行
	}
}
