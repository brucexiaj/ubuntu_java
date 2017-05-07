package com.totoro.proxymode;
/**
 * 代理模式
 * ./TypeInformation/Dynamic proxies
 * @author brucexiajun
 *
 */
//首先，你得定义一个接口
interface Interface
{
	void doSomething();
	void somethingElse(String args);
}
//然后，你得找一个所谓的真实类实现这个接口
class RealObject implements Interface
{
	@Override
	public void doSomething()
	{
		System.out.println("I am RealObjects");
	}
	@Override
	public void somethingElse(String args)
	{
		System.out.println("I am RealObjects,doing something else:"+args);
	}
}

//第三，你得找一个代理类实现这个接口，这个代理类有一个接口的属性，
//然后它在具体实现的时候会让接口去实现（其实本质上是让真实类去实现了），当然，它自己也会去实现（听起来好像脑子有问题）
class SimpleProxy implements Interface
{
	private Interface proxy;
	
	//这个接口属性实际上在用的时候传过来的是真实类的对象
	//就是说：RealObject realObject-->>Interface proxy
	
	public SimpleProxy(Interface proxy) {
		super();
		this.proxy = proxy;
	}

	@Override
	public void doSomething() {
		System.out.println("proxy do something");
		proxy.doSomething();
		
	}

	@Override
	public void somethingElse(String args) {
		System.out.println("proxy do something else.");
		proxy.somethingElse(args);
		
	}
	
}


public class ProxyModeDemo 
{
	public static void consumer(Interface interFace)
	{
		interFace.doSomething();
		interFace.somethingElse("bonobo");
	}
	
	public static void main(String []args)
	{
		consumer(new RealObject());
		consumer(new SimpleProxy(new RealObject()));
	}
}
