package exercise3;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Student implements Runnable
{
	public  ReentrantLock lock=new ReentrantLock();
	public  Condition condition=lock.newCondition();
	private Job job;
	private School school;
	
	

	public Student(School school,Job job) 
	{
		super();
		this.school = school;
		this.job=job;
	}

	public Student() 
	{
		super();
	}

	

	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				lock.lock();
				while(job.getState()==true)
				{
					condition.await();
				}
				lock.unlock();
				TimeUnit.MILLISECONDS.sleep(300);
				job.changeJobState();
				System.out.println("学生做完了作业"+new Date());
				
				school.teacher.lock.lock();
				school.teacher.condition.signalAll();
				school.teacher.lock.unlock();
			}
			
			
			
		}
		catch(Exception e)
		{
			
		}
		
	}
}
