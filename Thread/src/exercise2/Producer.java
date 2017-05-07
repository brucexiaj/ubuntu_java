package exercise2;

import java.util.concurrent.TimeUnit;

public class Producer implements Runnable
{
	private Product product;
	
	public Producer(Product product) 
	{
		super();
		this.product = product;
	}

	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				
				while(product.getFlag()==true)
				{
					synchronized(product)
					{
						product.wait();
					}
				}
				System.out.println("生产了一个产品");
				TimeUnit.MILLISECONDS.sleep(600);//这里的睡眠可有可无
				
				product.changeFlag();
					
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
