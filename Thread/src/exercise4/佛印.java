package exercise4;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class 佛印 implements Runnable
{
	private CyclicBarrier cyclicBarrier;
	private ExecutorService exe;
	private String []cities={"临安","金陵","开封","洛阳"};
	private int []distances={20,30,40,28};
	private int currentCity=0;
	private int totalDistance=0;
	
	
	


	public 佛印(CyclicBarrier cyclicBarrier, ExecutorService exe) {
		super();
		this.cyclicBarrier = cyclicBarrier;
		this.exe = exe;
	}





	public void run()
	{
		try
		{
			while(currentCity<=3)
			{
				System.out.println("佛印出发了,目的地"+cities[currentCity]+",距离:"+distances[currentCity]);
				while(totalDistance<distances[currentCity])
				{
					TimeUnit.SECONDS.sleep(1);
					int todayDistance=new Random().nextInt(distances[currentCity]/2);
					totalDistance+=todayDistance;
					System.out.println("佛印走了"+todayDistance);
				}
				totalDistance=0;
				
				System.out.println("佛印到达"+cities[currentCity]);
				cyclicBarrier.await();
				
				currentCity++;
			}
			System.out.println("佛印到达终点");
			exe.shutdownNow();
		}
		catch(Exception e)
		{
			
		}
	}
}
