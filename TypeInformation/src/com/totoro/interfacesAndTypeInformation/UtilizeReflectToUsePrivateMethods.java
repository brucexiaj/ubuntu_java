package com.totoro.interfacesAndTypeInformation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.totoro.common.Teacher;
/**
 * ./TypeInformation/Interfaces and type information
 * 在反射的强大作用下，所有的修饰符都已经失去了往日的作用
 * @author brucexiajun
 *
 */
public class UtilizeReflectToUsePrivateMethods
{
	public static void main(String [] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		//不使用反射，同一个包，最低只能调用protected修饰的方法
		Student student = new Student();
		student.protectedMethod();
		//不使用反射，不同的包，最低只能使用public修饰的方法
		Teacher teacher = new Teacher();
		teacher.publicMethod();
		//下面是反射表演的环节
		System.out.println("启用了反射的方法后：");
		//1。同一个包不同的类
		Method privateMethodCall = student.getClass().getDeclaredMethod("privateMethod");
		privateMethodCall.setAccessible(true);
		privateMethodCall.invoke(student);
		//2不同的包
		Method protectedMethodCall = teacher.getClass().getDeclaredMethod("protectedMethod");
		protectedMethodCall.setAccessible(true);
		protectedMethodCall.invoke(teacher);
		
		Method privateMethodCallDifferentPackage = teacher.getClass().getDeclaredMethod("privateMethod");
		privateMethodCallDifferentPackage.setAccessible(true);
		privateMethodCallDifferentPackage.invoke(teacher);
	}
}
