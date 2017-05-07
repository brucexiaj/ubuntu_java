package com.totoro.proxymode2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ./TypeInformation/Dynamic proxies
 * 动态代理模式
 * 没怎么搞懂
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
		System.out.println("I am RealObjects,do something");
	}
	@Override
	public void somethingElse(String args)
	{
		System.out.println("I am RealObjects,do something else:"+args);
	}
}


class DynamicProxyHandler implements InvocationHandler
{
	private Object proxied;

	public DynamicProxyHandler(Object proxy) {
		super();
		this.proxied = proxy;
	}
	public Object invoke(Object proxy,Method method,Object[] args)throws Throwable
	{
		//System.out.println("代理类的名字："+proxy.getClass()+"\n方法："+method.getName());
		
		return method.invoke(proxied, args);
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
		RealObject realObject = new RealObject();
		consumer(realObject);
		//创建一个动态代理
		//动态代理将会引导所有的调用到invocation handler
		Interface proxy = (Interface)Proxy.newProxyInstance(Interface.class.getClassLoader(), new Class<?>[]{Interface.class},new DynamicProxyHandler(realObject));
		consumer(proxy);
	}
}
