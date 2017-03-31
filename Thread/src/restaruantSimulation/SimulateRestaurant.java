package restaruantSimulation;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
/**
 * 这是一个模拟饭店营业的流程，主要测试了几个涉及线程的队列的使用
 * @author brucexiajun
 * SynchronousQueue,BlockingQueue,
 * 顾客下单->服务员取走单子->厨师收到订单->查看订单的食物->厨师做食物->通知服务员取食物->服务员将食物拿给顾客->顾客享用食物
 * 用a到j标示了整个流程
 *./Concurrency/Simulation/The restaurant simulation
 */


class Order
{
	
	private Customer customer;
	private WaitPerson wp;
	private Food food;
	public Order(Customer customer, WaitPerson wp, Food food) 
	{
		super();
		this.customer = customer;
		this.wp = wp;
		this.food = food;
	}
	public Food item()
	{
		return food;
	}
	public Customer getCustomer()
	{
		return customer;
	}
	public WaitPerson getWaitPerson()
	{
		return wp;

	}
	
}

class Plate
{
	private final Order order;
	private final Food food;
	public Plate(Order order, Food food) 
	{
		super();
		this.order = order;
		this.food = food;
	}
	public Order getOrder()
	{
		return order;
	}
	public Food getFood()
	{
		return food;
	}
	public String toString()
	{
		return "食物:"+food.toString();
	}
	
}

class Customer implements Runnable
{
	private static int counter=0;
	private final int id=counter++;
	private final WaitPerson wp;
	private SynchronousQueue<Plate> placeSetting=new SynchronousQueue<Plate>();//这个队列每次只能放一个对象
	public Customer(WaitPerson wp)
	{
		this.wp=wp;
	}
	//服务员将食物发放给顾客
	public void deliver(Plate p)throws InterruptedException
	{
		placeSetting.put(p);
	}
	public void run()
	{
		for(Course course:Course.values())
		{
			Food food=course.randomSelection();
			try
			{
				wp.placeOrder(this,food);//a.顾客下订单，将单子交给服务员
				//j.顾客拿到食物，开始享用。
				System.out.println(this+" 正在吃 "+placeSetting.take());//如果食物没有做好，会被阻塞
				
			}
			catch(InterruptedException e)
			{
				System.out.println(this+" 等待 "+food+" 中断了");
				break;
			}
		}
		System.out.println(this+" 吃完了食物，离开了");
	}
	
	public String toString()
	{
		return "顾客 "+id;
	} 
}

class WaitPerson implements Runnable
{
	private static int counter=0;
	private final int id=counter++;
	private final Restaurant restaurant;
	BlockingQueue<Plate> filledOrders=new LinkedBlockingQueue<Plate>();//这个队列没有大小限制
	public WaitPerson(Restaurant restaurant)
	{
	
		this.restaurant = restaurant;
	}
	
	public void placeOrder(Customer c,Food f)
	{
		try
		{
			//b.服务员将单子交给厨师
			restaurant.orders.put(new Order(c,this,f));
		}
		catch(Exception e)
		{
			System.out.println(this+" placeOrder interrupted.");
			
		}
	}
	
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				//h.服务员取走完成的订单
				Plate plate=filledOrders.take();//这里会被阻塞
				System.out.println(this+"拿到了 "+plate+"，这是发给 "+plate.getOrder().getCustomer()+"的食物");
				//i.将完成的订单（食物）送到顾客手中
				plate.getOrder().getCustomer().deliver(plate);
			}
		}
		catch(Exception e)
		{
			System.out.println(this+" interrupted.");
			
		}
		System.out.println(this+" off duty.");
	}
	
	public String toString()
	{
		return "侍者 "+id+" ";
	} 
}


class Chef implements Runnable
{
	private static int counter=0;
	private final int id=counter++;
	private final Restaurant restaurant;
	
	public Chef(Restaurant restaurant) 
	{
		super();
		this.restaurant = restaurant;
	}
	
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				//c.厨师取下订单
				Order order=restaurant.orders.take();//没有订单，这里也会被阻塞
				//d.查看订单里面的食物
				Food requestedItem=order.item();
				//e.烹制出该食物
				TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500));
			    //f.将食物放到盘子里面
				Plate plate=new Plate(order,requestedItem);
				//g.通知服务员，同时给完成的订单添加一项，表示厨师完成了一项订单
				order.getWaitPerson().filledOrders.put(plate);//这里不会被阻塞
				
			}
		}
		catch(Exception e)
		{
			System.out.println(this+"interrupted.");
			
		}
		System.out.println(this+"off duty.");
	}
	
	public String toString()
	{
		return "Chef "+id+" ";
	} 
}

class Restaurant implements Runnable
{
	private List<WaitPerson> waitPersons=new ArrayList<WaitPerson>();
	private List<Chef> chefs=new ArrayList<Chef>();
	private ExecutorService exe;
	BlockingQueue<Order> orders=new LinkedBlockingQueue<Order>();
	public Restaurant(ExecutorService exe,int nWaitPersons,int nChefs)
	{
		this.exe=exe;
		for(int i=0;i<nWaitPersons;i++)
		{
			WaitPerson waitPerson=new WaitPerson(this);
			waitPersons.add(waitPerson);
			exe.execute(waitPerson);
		}
		for(int i=0;i<nChefs;i++)
		{
			Chef chef=new Chef(this);
			chefs.add(chef);
			exe.execute(chef);
		}
	}

	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				//为顾客随机分配一名服务员
				WaitPerson wp=waitPersons.get(new Random().nextInt(waitPersons.size()));
				Customer c=new Customer(wp);
				exe.execute(c);
				TimeUnit.MILLISECONDS.sleep(100);
				
			}
		}
		catch(Exception e)
		{
			System.out.println("restaurant interrupted.");
			
		}
		System.out.println("饭店关门了！");
	}
	
	
}
public class SimulateRestaurant 
{
	public static void main(String args[])throws Exception
	{
		
		ExecutorService exe=Executors.newCachedThreadPool();
		Restaurant restaurant=new Restaurant(exe,5,2);
		exe.execute(restaurant);
		TimeUnit.SECONDS.sleep(1);
		exe.shutdownNow();
	}
}
