package com.totoro.simple.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class ShowMethods 
{  
	public static void printTest(String s)
	{
		System.out.println(s);
	}
	//第一个\是为了转义后面的\w+，这个表示一个及以上的a-Az-Z0-9，后面的两个\\是为了转义.符号，因为.符号有特殊意义，所以当.表示本身是需要转义
	private static Pattern pattern = Pattern.compile("\\w+\\.");
	public static void main(String[] args)
	{
		try
		{
			Class<?> c = Class.forName("com.totoro.simple.reflection.ShowMethods");
			Method[] methods = c.getMethods();
			Constructor[] constructors = c.getConstructors();
			
			for(Method method:methods)
			{
				//System.out.println(method.toString());
				if(method.toString().indexOf("ShowMethods")==-1)//就是说不含有"ShowMethods"字符串，所以main函数不符合
				{
					System.out.println(pattern.matcher(method.toString()).replaceAll(""));
				}
			}
			for(Constructor constructor:constructors)
			{
				if(constructor.toString().indexOf("ShowMethods")!=-1)
				{
					System.out.println(pattern.matcher(constructor.toString()).replaceAll(""));
				}
			}
			Method method = c.getMethod("printTest", String.class);
			method.invoke(c, "test for invoke");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
