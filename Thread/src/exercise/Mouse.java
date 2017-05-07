package exercise;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Mouse implements Runnable
{
	private   static int id=0;
	public final int count=id+1;
	private  LinkedBlockingQueue<Mouse> mouseQueue;
	
	
	
	public Mouse(LinkedBlockingQueue<Mouse> mouseQueue) {
		super();
		this.mouseQueue = mouseQueue;
	}

	public Mouse()
	{
		id++;
	}

	public void run()
	{
		while(!Thread.interrupted())
		{
			
			try {
				mouseQueue.put(new Mouse());
				
				System.out.println(id+"号鼠标下线");
			} catch (InterruptedException e) {
				
				System.out.println("鼠标生产线中断!");
			}
		}
	}
	

	
	

}
