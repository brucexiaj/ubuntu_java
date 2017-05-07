package exercise;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MainFrame implements Runnable
{
	private  static int id=0;
	public final int count=id+1;
	private  LinkedBlockingQueue<MainFrame> mainFrameQueue;
	
	public MainFrame(LinkedBlockingQueue<MainFrame> mainFrameQueue)
	{
		this.mainFrameQueue=mainFrameQueue;
	}
	
	
	public MainFrame() 
	{
		id++;
	}


	public void run()
	{
		while(!Thread.interrupted())
		{
			
			try 
			{
				mainFrameQueue.put(new MainFrame());
				
				System.out.println(id+"号主板下线");
			} 
			catch (InterruptedException e)
			{
				
				System.out.println("主板生产线中断!");
			}
		}
	}
	
}
