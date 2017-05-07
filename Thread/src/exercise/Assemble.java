package exercise;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Assemble implements Runnable
{
	private static int id=0;
	private LinkedBlockingQueue<MainFrame> mainFrameQueue=new LinkedBlockingQueue<MainFrame>();
	private LinkedBlockingQueue<KeyBoard> keyBoardQueue=new LinkedBlockingQueue<KeyBoard>();
	private LinkedBlockingQueue<Mouse> mouseQueue=new LinkedBlockingQueue<Mouse>();
	
	
	public Assemble(LinkedBlockingQueue<MainFrame> mainFrameQueue, LinkedBlockingQueue<KeyBoard> keyBoardQueue,
			LinkedBlockingQueue<Mouse> mouseQueue)
	{
		
		this.mainFrameQueue = mainFrameQueue;
		this.keyBoardQueue = keyBoardQueue;
		this.mouseQueue = mouseQueue;
	}
	
	
	public void run()
	{
		while(!Thread.interrupted())
		{
			try
			{
				
				
				System.out.println(++id+"号电脑下线："+mainFrameQueue.take().count+"号主板，"+keyBoardQueue.take().count+"号键盘，"+mouseQueue.take().count+"号鼠标");
			}
			catch(Exception e)
			{
				System.out.println("组装线中断!");
			}
		}
	}

}
