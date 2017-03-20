package PriorityBlockingQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
/**
 * PriorityBlockingQueue的使用
 * ./Concurrency/New Library Components
 * @author brucexiajun
 *用法参考DelayQueue，优先级高的Task首先运行
 *队列queue按照优先级高低保存任务，表List按照任务创建的时间先后保存任务
 *输出的结果开始是按照优先级输出，然后按照创建时间的先后输出
 */



class PrioritizedTask implements Runnable,Comparable<PrioritizedTask>
{
	private Random random=new Random(47);
	private static int counter=0;
	private final int id=counter++;
	private final int priority;
	
	protected static List<PrioritizedTask> sequence=new ArrayList<PrioritizedTask>();
	public PrioritizedTask(int priority)
	{
		this.priority=priority;
		
		sequence.add(this);
	}
	public int compareTo(PrioritizedTask arg)
	{
	
		if(priority<arg.priority)
		{
			return 1;
		}
		else
		{
			return (priority>arg.priority)?-1:0;
		}
	}
	
	public void run()
	{
		try
		{
			TimeUnit.MICROSECONDS.sleep(random.nextInt(250));
		}
		catch(InterruptedException e)
		{}
		System.out.println(this);
	}
	
	public String toString()
	{
		return "Priority:"+String.format("[%1$-1d]", priority)+",id:"+id;
	}
	
	public String summary()
	{
		return "id:"+id+" priority:"+priority;
	}
	
	public static class EndSentinel extends PrioritizedTask
	{
		private ExecutorService exec;
		public EndSentinel(ExecutorService e)
		{
			super(-1);
			exec=e;
		}
		public void run()
		{
			for(PrioritizedTask pt:sequence)
			{
				System.out.println(pt.summary()+" ");
			}
			System.out.println(this+" Calling shutdownNow()");
			exec.shutdownNow();
		}
	}
	
}

class PrioritizedTaskProducer implements Runnable
{
	private Random random=new Random(47);
	private Queue<Runnable> queue;
	private ExecutorService exec;
	public PrioritizedTaskProducer(Queue<Runnable> q,ExecutorService e)
	{
		queue=q;
		exec=e;
	}
	
	public void run()
	{
		for(int i=0;i<5;i++)
		{
			queue.add(new PrioritizedTask(i));
		}
		for(int i=0;i<5;i++)
		{
			queue.add(new PrioritizedTask(5));
		}
		queue.add(new PrioritizedTask.EndSentinel(exec));
		System.out.println("Finished PrioritizedTaskProducer");
	}
}
class PrioritizedTaskConsumer implements Runnable
{
	private PriorityBlockingQueue<Runnable> q;
	public PrioritizedTaskConsumer(PriorityBlockingQueue<Runnable> q)
	{
		this.q=q;
	}
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				q.take().run();
			}
		}
		catch(InterruptedException e)
		{	}
		System.out.println("Finished PrioritizedTaskConsumer");
	}
	
}
public class PriorityBlockingQueueDemo 
{
	public static void main(String args[])throws Exception
	{
		ExecutorService exec=Executors.newCachedThreadPool();
		PriorityBlockingQueue<Runnable> queue=new PriorityBlockingQueue<Runnable>();
		exec.execute(new PrioritizedTaskProducer(queue,exec));
		exec.execute(new PrioritizedTaskConsumer(queue));
	}
}
