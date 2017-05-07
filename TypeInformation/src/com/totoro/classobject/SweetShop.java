package com.totoro.classobject;
/**
 * 
 * @author brucexiajun
 * ./TypeInformation/The Class Object
 * 类的static代码块会在类第一次被加载时执行
 * 
 *
 */
class Candy
{
	static //类的static代码块，在类第一次被加载时执行
	{
		System.out.println("Loading Candy.");
	}
}

class Gum
{
	static
	{
		System.out.println("Loading Gum.");
	}
}


public class SweetShop 
{
	public static void main(String[] args)
	{
		new Candy();//通过new一个类创建类的实例，会将类加载到虚拟机中
		System.out.println("After creating Candy.");
		try
		{
			Class.forName("Gum");//通过Class.forName加载类名时，会将类加载到虚拟机中
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("No Class Gum.");
		}
		System.out.println("After Class.forName(\"Gum\")");
		
	}
}
