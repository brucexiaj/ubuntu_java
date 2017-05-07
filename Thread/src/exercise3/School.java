package exercise3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 学生写完作业给老师批改，老师改完之后学生再写作业，池子的大小为1
 * 显示锁ReentrantLock和condition的使用
 * 不论是condition.await还是condition.signalAll，都需要被包围在lock和unlock中间
 * 一个对象的condition只有自己的condition能唤醒，这个和wait,notifyAll的原理相同
 * 这里通过School把student对象传递给了teacher对象，要不然在student类中无法给teacher类解锁
 * @author brucexiajun
 *参考:《Thinking in Java》，./Concurrency/Cooperation between tasks/Producers and Consumers
 */
public class School
{
	
	Job job=new Job();
	Student student = new Student(this,job);
	Teacher teacher = new Teacher(this,job);
	
	
	public School(ExecutorService exe)
	{
		
		exe.execute(student);
		exe.execute(teacher);
	}
	
	public static void main(String arguements[]) throws InterruptedException
	{
		ExecutorService exe=Executors.newCachedThreadPool();
		
		new School(exe);
		TimeUnit.SECONDS.sleep(5);
		exe.shutdownNow();
	}
	
}
