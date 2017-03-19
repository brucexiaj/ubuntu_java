package ExplicitLockAndCondition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 
 * @author brucexiajun
 *显示锁（外部锁）的使用方法
 *Lock对象负责锁或者解锁对象，Condition对象则负责让对象等待或者唤醒对象
 *同内部锁一样，显示锁也需要遵循：先锁住对象，再等待或者唤醒对象。
 *内部锁表现为wait和notify方法必须在synchronized修饰的方法中调用
 *外部锁表现为await和signal方法必须在lock.lock()方法之后调用
 *./Concurrency/CoorpationBetweenTasks
 */
class Car
{
	private Lock lock=new ReentrantLock();
	private Condition condition=lock.newCondition();
	
	private boolean waxOn=false;
	public void waxed()
	{
		//不要试图屏蔽这句话，否则你会得到一个IllegealMonitorStateException，不要问我为什么，前面已经解释过了
		lock.lock();
		try
		{
			waxOn=true;
			//！！！高能预警：这里不能用notifyAll()函数，java中规定，notifyAll只能用在synchronized关键字修饰的方法中，wait()也是
			condition.signalAll();
			//notifyAll();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public void buffed()
	{
		lock.lock();
		try
		{
			waxOn=false;
			condition.signalAll();
			
			
			
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public void waitForWaxing() throws InterruptedException
	{
		lock.lock();
		try
		{
			while(waxOn==false)
			{
				condition.await();
				
			}
			
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public void waitForBuffing() throws InterruptedException
	{
		lock.lock();
		try
		{
			while(waxOn==true)
			{
				condition.await();
		
			}
		}
		finally
		{
			lock.unlock();
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
public class WaxOnMatic2 
{
	public static void main(String args[]) throws Exception
	{
		Car car = new Car();
		ExecutorService exec=Executors.newCachedThreadPool();
		exec.execute(new WaxOff(car));
		exec.execute(new WaxOn(car));
		TimeUnit.MICROSECONDS.sleep(10000);
		exec.shutdownNow();//结束这两个线程
		//main线程结束
	}

}
