package distributingWork;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
/**
 * ./Concurrency/Simulation/DistributingWork
 * @author brucexiajun
 *汽车生产线的模拟
 *安装好汽车的底盘->装配程序雇佣机器人->轮胎机器人/动力传动系统机器人/发动机机器人开始工作->汽车完工->报道者汇报汽车生产的结束
 *CyclicBarrier,LinkedBlockingQueue的使用
 *CyclicBarrier构造函数里面整型参数n的意思是，等到有n个线程进入到await()状态时，所有线程自动唤醒
 */
class Car
{
	private int id;
	private boolean engine=false,driveTrain=false,wheels=false;
	public Car(int id)
	{
		this.id=id;
	}
	public Car()
	{
		id=-1;
	}
	public synchronized int getId()
	{
		return id;
	}
	public synchronized void installEngine()
	{
		engine=true;
	}
	public synchronized void installDriveTrain()//动力传动系统
	{
		driveTrain=true;
	}
	public synchronized void installWheel()
	{
		wheels=true;
	}
	public String toString()
	{
		return "Car "+id+" ["+"enginee:"+engine+",driveTrain:"+driveTrain+",wheels:"+wheels+"]";
	}
}

class CarQueue extends LinkedBlockingQueue<Car>{}

//底盘的生产，汽车生产第一步
class ChassisBuilder implements Runnable
{
	private CarQueue carQueue;
	private int counter=0;
	public ChassisBuilder(CarQueue carQueue)
	{
		
		this.carQueue = carQueue;
	}
	
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				TimeUnit.MILLISECONDS.sleep(500);
				Car c=new Car(counter++);
				System.out.println("底盘建造者创造了"+c);
				carQueue.put(c);
			}
		}
		catch(Exception e)
		{
			System.out.println("ChassisBuilder:interrupted.");
			
		}
		System.out.println("ChassisBuilder off");
	}
	
}

//装配程序
class Assembler implements Runnable
{
	private CarQueue chassisQueue,finishingQueue;
	private Car car;
	private CyclicBarrier cb=new CyclicBarrier(4);//包括装配程序线程，轮胎线程，动力传动系统线程，发动机线程
	private RobotPool robotPool;
	public Assembler(CarQueue chassisQueue, CarQueue finishingQueue, RobotPool robotPool)
	{
		this.chassisQueue = chassisQueue;
		this.finishingQueue = finishingQueue;
		this.robotPool = robotPool;
	}
	
	public Car car()
	{
		return car;
	}
	
	public CyclicBarrier barrier()
	{
		return cb;
	}
	
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				//如果没有一辆汽车从底盘生产程序出来，那么等待（阻塞）
				car= chassisQueue.take();
				//雇佣机器人安装汽车的其他组件
				robotPool.hire(EngineRobot.class,this);
				robotPool.hire(DriveTrainRobot.class,this);
				robotPool.hire(WheelRobot.class,this);
				//等待三个机器人程序都完工，没完工的阻塞在这里
				cb.await();
				finishingQueue.put(car);
				
			}
		}
		catch(Exception e)
		{
			System.out.println("Assembler:interrupted.");
			
		}
		System.out.println("Assembler off");
	}
}

//汇报任务的完成
class Reporter implements Runnable
{
	private CarQueue carQueue;

	public Reporter(CarQueue carQueue) 
	{
		super();
		this.carQueue = carQueue;
	}
	
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				//如果没有汽车完工，阻塞在这里
				System.out.println(carQueue.take().getId()+" 号汽车组装完成!");
				System.out.println();
			}
		}
		catch(Exception e)
		{
			System.out.println("Reporter:interrupted.");
			
		}
		System.out.println("Reporter off");
	}
}

abstract class Robot implements Runnable
{
	private RobotPool pool;

	public Robot(RobotPool pool) 
	{

		this.pool = pool;
	}
	
	protected Assembler ass;
	public Robot assignAssembler(Assembler assembler)
	{
		ass=assembler;
		return this;
	}
	private boolean engage=false;
	public synchronized void engage()
	{
		engage=true;
		notifyAll();
	}
	
	abstract protected void performService();
	
	public void run()
	{
		try
		{
			powerDown();
			while(!Thread.interrupted())
			{
				performService();
				ass.barrier().await();//等待所有四个程序（装配，轮胎，发动机，动力）完成，否则阻塞在这里
				powerDown();
			}
			
		}
		catch(Exception e)
		{
			System.out.println(this+" Robot:interrupted.");
			
		}
		System.out.println(this+" Robot off");
	}
	
	private synchronized void powerDown() throws InterruptedException
	{
		engage=false;
		ass=null;
		pool.release(this);
		while(engage==false)
		{
			wait();
		}
	}
	
	public String toString()
	{
		return getClass().getName();
	}
}

//安装发动机
class EngineRobot extends Robot
{
	public EngineRobot(RobotPool pool)
	{
		super(pool);
	}
	protected void performService()
	{
		System.out.println(this+" 安装了发动机.");
		ass.car().installEngine();
	}
}

//安装动力传动系统
class DriveTrainRobot extends Robot
{
	public DriveTrainRobot(RobotPool pool)
	{
		super(pool);
	}
	protected void performService()
	{
		System.out.println(this+" 安装了动力传动系统");
		ass.car().installDriveTrain();
	}
}

//安装轮胎
class WheelRobot extends Robot
{
	public WheelRobot(RobotPool pool)
	{
		super(pool);
	}
	protected void performService()
	{
		System.out.println(this+" 安装了车轮.");
		ass.car().installWheel();
	}
}

class RobotPool
{
	private Set<Robot> pool=new HashSet<Robot>();
	public synchronized void add(Robot r)
	{
		pool.add(r);
		notifyAll();
	}
	
	public synchronized void hire(Class<? extends Robot> robotType,Assembler ass)throws InterruptedException
	{
		for(Robot r:pool)
		{
			if(r.getClass().equals(robotType))
			{
				pool.remove(r);
				r.assignAssembler(ass);
				r.engage();
				return;
			}
		}
		wait();
		hire(robotType,ass);
	}
	
	public synchronized void release(Robot r)
	{
		add(r);
	}
}
public class CarBuilder
{
	public static void main(String args[])throws Exception
	{
		CarQueue chassisQueue=new CarQueue(),finishingQueue=new CarQueue();
		ExecutorService exe=Executors.newCachedThreadPool();
		RobotPool robotPool=new RobotPool();
		exe.execute(new EngineRobot(robotPool));
		exe.execute(new DriveTrainRobot(robotPool));
		exe.execute(new WheelRobot(robotPool));
		exe.execute(new Assembler(chassisQueue,finishingQueue,robotPool));
		exe.execute(new Reporter(finishingQueue));
		exe.execute(new ChassisBuilder(chassisQueue));
		TimeUnit.SECONDS.sleep(7);
		exe.shutdownNow();
	}
}
