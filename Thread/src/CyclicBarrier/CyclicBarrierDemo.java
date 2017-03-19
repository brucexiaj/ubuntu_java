package CyclicBarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 
 * @author brucexiajun
 * CyclicBarrier初始化时规定一个数目，然后计算调用了CyclicBarrier.await()进入等待的线程数。当线程数达到了这个数目时，所有进入等待状态的线程被唤醒并继续。 
 * CyclicBarrier就象它名字的意思一样，可看成是个障碍， 所有的线程必须到齐后才能一起通过这个障碍。 
 * CyclicBarrier初始时还可带一个Runnable的参数， 此Runnable任务在CyclicBarrier的数目达到后，所有其它线程被唤醒前被执行
 * 添加马-》到达数量-》打印栅栏和马的步数-》马醒来-》继续等待
 * ./Concurrency/New Library Components
 * 
 */
class Horse implements Runnable
{
	private static int counter=0;
	private final int id=counter++;
	private int strides=0;
	private static CyclicBarrier barrier;
	public Horse(CyclicBarrier cb)
	{
		barrier=cb;
	}
	
	public synchronized int getStrides()
	{
		return strides;
	}
	
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				synchronized(this)
				{
					strides+=new Random(47).nextInt(3);
				}
				barrier.await();
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
	}
	
	public String toString()
	{
		return "Horse"+id+" ";
	}
	
	public String tracks()
	{
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<getStrides();i++)
		{
			sb.append("*");
		}
		sb.append(id);
		return sb.toString();
	}
}

public class CyclicBarrierDemo
{
	static final int FINISH_LINE=25;//栅栏的数目
	private List<Horse> horses=new ArrayList<Horse>();
	private ExecutorService exec=Executors.newCachedThreadPool();
	private CyclicBarrier barrier;
	public CyclicBarrierDemo(int nHorses)
	{
		barrier=new CyclicBarrier(nHorses,new Runnable(){
			public void run()//这个在处于等待数量的马到达数量，马醒来之前执行
			{
				StringBuilder sb=new StringBuilder();
				for(int i=0;i<FINISH_LINE;i++)
				{
					sb.append("#");
				}
				System.out.println(sb);//打印栅栏
				for(Horse horse:horses)
				{
					System.out.println(horse.tracks());//打印马当前走过的距离
				}
				System.out.println("\n\n");
				for(Horse horse:horses)
				{
					if(horse.getStrides()>=FINISH_LINE)//有没有那匹马已经到达终点
					{
						System.out.println(horse+"won!");
						exec.shutdownNow();
						return;
					}
				}
			}
		});
		
		for(int i=0;i<nHorses;i++)
		{
			Horse horse=new Horse(barrier);
			horses.add(horse);
			exec.execute(horse);
		}
	}
	
	public static void main(String args[])
	{
		int nHorses=5;
		new CyclicBarrierDemo(nHorses);
	}
}
