package exercise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
/**
 * 20170406
 * Concurrency章节练习
 * 电脑生产线问题：主板，鼠标，键盘和组装线
 * 主板，鼠标，键盘生产线一直生产，直到当前池子里面的产品达到10个，这个时候阻塞
 * 组装线：只要池子里面主板鼠标键盘存在（各有一个以上），进行组装，当其中任何一个缺少的时候，组装阻塞
 *LinkedBlockingQueue:队列里面的东西达到10个阻塞
 *因为打印有时间，所以会看到11号鼠标下线之后，1号电脑才组装完成，但是鼠标池子里面实际上只有10个，1号电脑是在11号鼠标下线之前组装的
 * @author brucexiajun
 *
 */
public class ComputerProduce
{
	public static void main(String []args) throws InterruptedException
	{
		ExecutorService exec=Executors.newCachedThreadPool();
		LinkedBlockingQueue<MainFrame> mainFrameQueue=new LinkedBlockingQueue<MainFrame>(10);
		LinkedBlockingQueue<KeyBoard> keyBoardQueue=new LinkedBlockingQueue<KeyBoard>(10);
		LinkedBlockingQueue<Mouse> mouseQueue=new LinkedBlockingQueue<Mouse>(10);
		
		exec.execute(new Assemble(mainFrameQueue,keyBoardQueue,mouseQueue));
		exec.execute(new MainFrame(mainFrameQueue));
		exec.execute(new KeyBoard(keyBoardQueue));
		exec.execute(new Mouse(mouseQueue));
		
		TimeUnit.MILLISECONDS.sleep(100);
		exec.shutdownNow();
	}
}
