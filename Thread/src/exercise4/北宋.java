package exercise4;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class 北宋 
{
	public static void main(String args[])
	{
		ExecutorService exe=Executors.newCachedThreadPool();
		CyclicBarrier cyclicBarrier=new CyclicBarrier(3);
		exe.execute(new 苏轼(cyclicBarrier,exe));
		exe.execute(new 佛印(cyclicBarrier,exe));
		exe.execute(new 黄庭坚(cyclicBarrier,exe));
	}
	
}
