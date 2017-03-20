package scheduledExecutor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * ScheduledThreadPoolExecutor的使用方法
 * 通过对一个温室的模拟
 * @author brucexiajun
 *是一个线程池，里面的线程可以指定运行一次或者每隔一段时间重复运行
 *./Concurrency/New Library Components
 */
public class GreenhouseScheduler 
{
	private volatile boolean light=false;
	private volatile boolean water=false;
	private String thermostat="Day";//恒温器
	public synchronized String getThermostat()
	{
		return thermostat;
	}
	public synchronized void setThermostat(String value)
	{
		thermostat=value;
	}
	ScheduledThreadPoolExecutor scheduler=new ScheduledThreadPoolExecutor(10);
	
	//执行一次某项任务，在delay时间执行
	public void schedule(Runnable event,long delay)
	{
		scheduler.schedule(event, delay, TimeUnit.MILLISECONDS);
	}
	
	//重复执行某项任务
	public void repeat(Runnable event,long initialDelay,long period)
	{
		//event:某个任务;initialDelay:任务第一次执行的时间;period：任务每隔多久执行一次;TimeUnit时间的单位
		//即：任务第一次在initial时执行，第二次在initial+period时执行，依次类推
		scheduler.scheduleAtFixedRate(event, initialDelay, period, TimeUnit.MILLISECONDS);
	}
	
	class LightOn implements Runnable
	{
		public void run()
		{
			System.out.println("Turn on light!");
			light=true;
		}
	}
	
	class LightOff implements Runnable
	{
		public void run()
		{
			System.out.println("Turn off light!");
			light=false;
		}
	}
	
	class WaterOn implements Runnable
	{
		public void run()
		{
			System.out.println("Turn on water!");
			water=true;
		}
	}
	
	class WaterOff implements Runnable
	{
		public void run()
		{
			System.out.println("Turn off water!");
			water=false;
		}
	}
	
	class ThermostatNight implements Runnable
	{
		public void run()
		{
			System.out.println("Thermostat to night!");
			setThermostat("Night");
		}
	}
	
	class ThermostatDay implements Runnable
	{
		public void run()
		{
			System.out.println("Thermostat to day!");
			setThermostat("Day");
		}
	}
	
	class Bell implements Runnable
	{
		public void run()
		{
			//可以观察到铃铛每隔1秒响一次
			System.out.println("响铃的时间是："+new Date()+" Bing!");
		}
	}
	
	class Terminate implements Runnable
	{
		public void run()
		{
			System.out.println("Terminate!");
			scheduler.shutdownNow();//结束所有任务
			new Thread()//开启新的线程打印所有的温度和湿度的数据
			{
				public void run()
				{
					for(DataPoint d:data)
					{
						System.out.println(d);
					}
				}
			}.start();
		}
	}
	
	static class DataPoint
	{
		final Calendar time;
		final float temperature;
		final float humidity;
		public DataPoint(Calendar time,float humidity,float temperature)
		{
			this.time=time;
			this.humidity=humidity;
			this.temperature=temperature;
		}
		public String toString()
		{
			return time.getTime()+String.format("temperature:%1$.1f humidity:%2$.2f", temperature,humidity);
		}
	}
	
	private Calendar lastTime=Calendar.getInstance();
	{
		lastTime.set(Calendar.MINUTE, 30);
		lastTime.set(Calendar.SECOND, 00);
	}
	
	private float lastTemp=65.0f;
	private int tempDirection=+1;
	private float lastHumidity=50.0f;
	private int humidityDirection=+1;
	private Random rand=new Random(47);
	List<DataPoint> data=Collections.synchronizedList(new ArrayList<DataPoint>());
	
	//收集温度和湿度的线程
	class CollectionData implements Runnable
	{
		public void run()
		{
			System.out.println("Collection data");
			synchronized(GreenhouseScheduler.this)
			{
				lastTime.set(Calendar.MINUTE, lastTime.get(Calendar.MINUTE)+30);
				if(rand.nextInt(5)==4)
				{
					tempDirection=-tempDirection;
				}
				lastTemp=lastTemp+tempDirection*(1.0f+rand.nextFloat());
				if(rand.nextInt(5)==4)
				{
					humidityDirection=-humidityDirection;
				}
				lastHumidity=lastHumidity+humidityDirection*rand.nextFloat();
				//添加温湿度数据到List中
				data.add(new DataPoint((Calendar)lastTime.clone(),lastTemp,lastHumidity));
			}
			
		}
	}
	
	public static void main(String args[])
	{
		//所有task同时启动，所以前面7个输出结果是把所有任务都执行一遍
		//从第8个结果开始，按照任务间隔规定执行任务
		//可以画一个坐标轴帮助理解
		GreenhouseScheduler gh=new GreenhouseScheduler();
		gh.schedule(gh.new Terminate(),5000);//在第五秒时结束所有任务，同时统计并打印出温度和湿度的数据
		gh.repeat(gh.new Bell(), 0, 1000);
		gh.repeat(gh.new ThermostatNight(), 0, 2000);
		gh.repeat(gh.new LightOn(), 0, 200);
		gh.repeat(gh.new LightOff(), 0, 400);
		gh.repeat(gh.new WaterOn(), 0, 600);
		gh.repeat(gh.new WaterOff(), 0, 800);
		gh.repeat(gh.new ThermostatDay(), 0, 1400);
		gh.repeat(gh.new CollectionData(), 500, 500);//从第500毫秒开始，每隔500毫秒收集一次温度和湿度的数据
	}
}
