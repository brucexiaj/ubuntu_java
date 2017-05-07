package exercise;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class KeyBoard implements Runnable
{
	private  static int id=0;
	public final int count=id+1;
	private  LinkedBlockingQueue<KeyBoard> keyBoardQueue;

	
	
	public KeyBoard(LinkedBlockingQueue<KeyBoard> keyBoardQueue) {
		super();
		this.keyBoardQueue = keyBoardQueue;
	}

	public KeyBoard() {
		id++;
	}

	public void run()
	{
		while(!Thread.interrupted())
		{
			
			
			try
			{
				keyBoardQueue.put(new KeyBoard());
				
				System.out.println(id+"号键盘下线");
			} 
			catch (InterruptedException e)
			{
				
				System.out.println("键盘生产线中断!");
			}
		}
	}
	
	
	
}
