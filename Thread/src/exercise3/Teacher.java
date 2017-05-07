package exercise3;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Teacher implements Runnable
{
	public  ReentrantLock lock=new ReentrantLock();
	public  Condition condition=lock.newCondition();
	private School school;
	private Job job;
	public Teacher(School school,Job job)
	{
		super();
		this.school = school;
		this.job=job;
	}
	public Teacher() {
		super();
	}
	
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				lock.lock();
				while(job.getState()==false)
				{
					
					condition.await();
				}
				
				lock.unlock();
				TimeUnit.MILLISECONDS.sleep(300);
				job.changeJobState();
				System.out.println("老师改完了作业"+new Date());
				
				school.student.lock.lock();
				school.student.condition.signalAll();
				school.student.lock.unlock();
				
			}
			
			
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	
}
