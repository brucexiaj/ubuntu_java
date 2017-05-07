package com.totoro.checkingBeforeCast;
/**
 * ./TypeInformation/Checking Before a cast
 * instanceof isInstance() isAssignableFrom() 三个方法的比较
 * 说实话，我觉得第三个方法和前两个作用都不同，没有什么可比性
 * @author brucexiajun
 *
 */
class Creature
{
	public Creature()
	{
		
	}
}

class Human extends Creature
{
	public Human(){super();}
}



class Student extends Human
{
	public Student(){super();}
}



public class InstanceofDemo 
{
	public static void main(String []args)
	{
		Human human = new Human();
		//子类的对象是父类的实例
		if(human instanceof Creature)
		{
			System.out.println("human是Creature的实例");
		}
		if(human instanceof Human)
		{
			System.out.println("human是Human的实例");
		}
		//父类的对象不是子类的实例
		if(human instanceof Student)
		{
			System.out.println("human是Student的实例");
		}
		//isInstance()是半静态半动态的，而instanceof是静态的，两者功能上完全相同
		Class humanClass = Human.class;
		System.out.println("human是不是Human的实例："+humanClass.isInstance(human));
		//A.isAssignableFrom(B)，如果A是B的同类或者父类，返回true
		System.out.println("Creature:"+humanClass.isAssignableFrom(Creature.class));
		System.out.println("Human:"+humanClass.isAssignableFrom(Human.class));
		System.out.println("Student:"+humanClass.isAssignableFrom(Student.class));
		
		//测试==和equal判断子类和父类的类引用之间的关系
		Creature creature = new Creature();
	
		if(creature.getClass() == human.getClass())
		{
			System.out.println("父类和子类的类引用是==的");
		}
		if(creature.getClass().equals(human.getClass()))
		{
			System.out.println("父类和子类的类引用是equal的");
		}
		if(human.getClass().equals(creature.getClass()))
		{
			System.out.println("子类和父类的类引用是equal的");
		}
	}
	
}
