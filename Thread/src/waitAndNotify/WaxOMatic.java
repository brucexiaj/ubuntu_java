package waitAndNotify;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



class Car
{

	private boolean waxOn=false;
	public synchronized void waxed()
	{
		
		
			waxOn=true;
			
		
			notifyAll();
		
	}
	
	public synchronized void buffed()
	{
		
			waxOn=false;
			notifyAll();
			
			
			
		
	}
	
	public synchronized void waitForWaxing() throws InterruptedException
	{
		
			while(waxOn==false)
			{
			wait();
				
			}
			
		
	}
	
	public synchronized void waitForBuffing() throws InterruptedException
	{
		
			while(waxOn==true)
			{
				wait();
		
			}
		
	}
}

class WaxOn implements Runnable
{
	private Car car;
	public WaxOn(Car car)
	{
		this.car=car;
	}
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				System.out.println("Wax On!");
				TimeUnit.MICROSECONDS.sleep(200);
				car.waxed();
				car.waitForBuffing();
			}
		}
		catch(InterruptedException e)
		{
			System.out.println("Exiting via Interrupted!");
		}
		System.out.println("Ending wax on task.");
	}
}

class WaxOff implements Runnable
{
	private Car car;
	public WaxOff(Car c)
	{
		car=c;
	}
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				car.waitForWaxing();
				System.out.println("Wax off!");
				TimeUnit.MICROSECONDS.sleep(200);
				car.buffed();
			}
		}
		catch(InterruptedException e)
		{
			System.out.println("Exiting via interrupt.");
		}
		System.out.println("Ending wax off task.");
	}
}
public class WaxOMatic
{
	public static void main(String args[]) throws Exception
	{
		Car car = new Car();
		ExecutorService exec=Executors.newCachedThreadPool();
		exec.execute(new WaxOff(car));
		exec.execute(new WaxOn(car));
		TimeUnit.MICROSECONDS.sleep(30000);
		exec.shutdownNow();//结束这两个线程
		//main线程结束
	}

}

