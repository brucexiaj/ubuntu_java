package CountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 
 * @author brucexiajun
 *java.util.concurrent中引入的新类CountDownLatch的使用方法的介绍
 *latch的构造函数有一个整型的参数，我们称之为小a吧
 *Task1运行了，他调用了latch的countDown()方法，小a减1
 *Task1运行了，他调用了latch的countDown()方法，小a减1
 *。。。。。。。。。。。。。
 *小a终于减为0了，一直在latch.await()中苦苦挣扎的TaskNB终于醒过来了，开始了自己的征程
 *简言之：一群特定数量的任务完成后，另外一个任务才能开始，至于任务如何分类，这取决与它调用了countDown()方法还是await()方法
 */
class TaskPortion implements Runnable
{
	private static int counter=0;
	private final int id=counter++;
	private final CountDownLatch cdownl;
	TaskPortion(CountDownLatch cdl)
	{
		cdownl=cdl;
	}
	
	public void run()
	{
		try
		{
			TimeUnit.MICROSECONDS.sleep(100);
			System.out.println(this+"completed.");
			cdownl.countDown();//任务的数量减1
		}
		catch(InterruptedException e)
		{
			//
		}
	}
	
	public String toString()
	{
		return String.format("%1$-3d", id);
	}
}

class WaitingTask implements Runnable
{
	private static int counter=0;
	private final int id=counter++;
	private final CountDownLatch cdownl;
	WaitingTask(CountDownLatch cdl)
	{
		cdownl=cdl;
	}
	
	public void run()
	{
		try
		{
			cdownl.await();//cdownl的值减为0后，自动醒来
			
			System.out.println("哈哈，老子终于被放出来了！！！"+this);
		
		}
		catch(InterruptedException e)
		{
			System.out.println(this+"Interrupted.");
		}
	}
	
	public String toString()
	{
		return String.format("WatingTask %1$-3d", id);
	}
}
public class CountDownLatchDemo 
{
	private static final int SIZE=7;
	public static void main(String args[]) throws Exception
	{
		ExecutorService exec=Executors.newCachedThreadPool();
		CountDownLatch latch=new CountDownLatch(SIZE);
		exec.execute(new WaitingTask(latch));
		for(int i=0;i<SIZE;i++)
		{
			exec.execute(new TaskPortion(latch));
		}
		System.out.println("所有任务加载完毕，准备发射!");
		exec.shutdown();
	}
	
	

}
