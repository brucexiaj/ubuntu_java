package semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class CheckoutTask<T> implements Runnable
{
	private static int counter=0;
	private final int id=counter++;
	private Pool<T> pool;
	public CheckoutTask(Pool<T> pool)
	{
		this.pool=pool;
	}
	public void run()
	{
		try
		{
			T item=pool.checkOut();
			System.out.println(this+" checked out "+item);
			TimeUnit.SECONDS.sleep(1);
			System.out.println(this+" checked in "+item);
			pool.checkIn(item);
		}
		catch(Exception e)
		{}
	}
	public String toString()
	{
		return "Check out task "+id+" ";
	}
	
}
public class SemphoreDemo 
{
	final static int SIZE=5;
	public static void main(String args[])throws Exception
	{
		final Pool<Fat> pool=new Pool<Fat>(Fat.class,SIZE);
		ExecutorService exe=Executors.newCachedThreadPool();
		System.out.println("执行5次checkout和5次checkin");
		//执行5次checkout和5次checkin
		for(int i=0;i<SIZE;i++)
		{
			exe.execute(new CheckoutTask<Fat>(pool));
		}

		List<Fat> fatList=new ArrayList<Fat>();
		//执行了5次checkout
		for(int i=0;i<SIZE;i++)
		{
			Fat f=pool.checkOut();
		
			
			fatList.add(f);
		}
		//试图再执行一次checkout被阻止了
		Future<?> blocked =exe.submit(new Runnable()
		{
			public void run()
			{
				try
				{
					pool.checkOut();
				}
				catch(Exception e)
				{
					System.out.println("checkout 被打断.");
				}
			}
		});
		TimeUnit.SECONDS.sleep(2);
		blocked.cancel(true);
		//执行5次checkin
		for(Fat fat:fatList)
		{
			pool.checkIn(fat);
		}
		//这里的checkin被阻止了，因为前面已经执行了所有的checkin
		for(Fat fat:fatList)
		{
			pool.checkIn(fat);
		}
		exe.shutdown();
	}
}
