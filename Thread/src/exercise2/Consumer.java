package exercise2;

import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable
{
	private Product product;
	public Consumer(Product product)
	{
		this.product=product;
	}
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				while(product.getFlag()==false)
				{
					
					synchronized(product)//这里不能是synchronized(this)
					{
						product.wait();//这里不能直接是wait();
					}
					
					
				}
				TimeUnit.MILLISECONDS.sleep(600);//这里的睡眠可有可无
				product.changeFlag();
				System.out.println("消费了一个产品");
				synchronized(product)
				{
					product.notifyAll();
				}
					
				
				
			}
		}
		catch(Exception e)
		{
			
		}
	}
}
