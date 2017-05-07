package exercise2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 生产者和消费者的问题，池子的大小为1
 * 这个例子最重要的结论是：wait和notifyAll函数只能放置在synchronized关键字的大括号中
 * 正如《Thinking in Java》书中所说：In fact, the only place you can call wait(),notify(),notifyAll() is within a synchronized method or block.
 * 还有，可以看到的是，要唤醒product的wait,只能靠product的notifyAll
 * 参考：./Concurrency/Cooperation between tasks/wait and notifyAll
 * @author brucexiajun
 *20170407
 */
public class MainClass {
	public static void main(String []args) throws InterruptedException
	{
		Product product = new Product();
		ExecutorService exe=Executors.newCachedThreadPool();
		exe.execute(new Producer(product));
		exe.execute(new Consumer(product));
		
		TimeUnit.SECONDS.sleep(5);
		exe.shutdownNow();
	}
}
