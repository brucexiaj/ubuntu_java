package com.totoro.factorymode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 设计模式之工厂模式
 * @author brucexiajun
 *一个接口，若干个在别的类的内部实现接口的该类
 *这个工厂模式不太好理解，自己看吧
 */
class Part
{
	
	//这个List里面包含的是：继承自Part类的类的工厂集合
	static List<Factory<? extends Part>> partFactoryList = new ArrayList<Factory<? extends Part>>();
	static 
	{
		//这里因该理解为new Factory(),前面的FanBlet/FuelFilter指明出自哪一个Factory
		partFactoryList.add(new FanBelt.Factory());
		partFactoryList.add(new FuelFilter.Factory());
		
	}
	public static Part createRandom()
	{
		int n = new Random().nextInt(partFactoryList.size());
		return partFactoryList.get(n).create();
	}
}


//产品1
class FuelFilter extends Part
{
	public String toString()
	{
		return getClass().getSimpleName();
	}
	public static class Factory implements com.totoro.factorymode.Factory<FuelFilter>
	{
		public FuelFilter create()
		{
			return new FuelFilter();
		}
	}
}


//产品2
class FanBelt extends Part
{
	public String toString()
	{
		return getClass().getSimpleName();
	}
	public static class Factory implements com.totoro.factorymode.Factory<FanBelt>
	{
		public FanBelt create()
		{
			return new FanBelt();
		}
	}
}
public class FactoryModeDemo
{
	public static void main(String args[])
	{
		for(int i=0; i<5; i++)
		{
			System.out.println(Part.createRandom());
		}
	}
}
