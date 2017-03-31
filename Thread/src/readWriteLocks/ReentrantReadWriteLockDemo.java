package readWriteLocks;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * 读写锁的使用
 * ./Concurrent/ReadWriteLocks
 * @author brucexiajun
 *1,2读线程都能顺利加上锁，但是3号读线程获得锁之后无法加锁，不是很理解
 */
class ReaderWriteListTest
{
	private class Writer implements Runnable
	{
		private ReentrantReadWriteLock rrwLock;
		private int id;
		public Writer(ReentrantReadWriteLock rrwLock,int id)
		{
			this.rrwLock=rrwLock;
			this.id=id;
		}
		public void run()
		{
			Lock writeLock=rrwLock.writeLock();
			System.out.println(id+"号写线程已经获得了写锁");
			writeLock.lock();
			System.out.println(id+"号写线程加锁完毕");
			
		}
	}
	
	private  class Reader implements Runnable
	{
		
		private ReentrantReadWriteLock rrwLock;
		private int id;
		public Reader(ReentrantReadWriteLock rrwLock,int id)
		{
			this.rrwLock=rrwLock;
			this.id=id;
		}
		public void run()
		{
			
			Lock readLock=rrwLock.readLock();
			System.out.println(id+"号读线程已经获得了读锁");
			readLock.lock();
			System.out.println(id+"号读线程加锁完毕");
			
		}
	}
	
	
	
	
	public void test()
	{
		 ReentrantReadWriteLock lock=new ReentrantReadWriteLock(true);
			ExecutorService exec=Executors.newCachedThreadPool();
			exec.execute(new Reader(lock,1));
			
			try 
			{
				TimeUnit.SECONDS.sleep(2);
				exec.execute(new Reader(lock,2));
				TimeUnit.MILLISECONDS.sleep(1);
				exec.execute(new Writer(lock,1));
				TimeUnit.MILLISECONDS.sleep(1);
				exec.execute(new Reader(lock,3));
				TimeUnit.SECONDS.sleep(2);
				exec.execute(new Writer(lock,2));
			
			
				
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
	}
}


public class ReentrantReadWriteLockDemo<T>
{
	
	
	public static void main(String args[])
	{
		new ReaderWriteListTest().test();
	}
	
	
}

