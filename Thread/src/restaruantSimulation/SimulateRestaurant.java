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

import semaphore.Fat;
import semaphore.Pool;

class Order
{
	private static int counter=0;
	private final int id=counter++;
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
	public String toString()
	{
		return "Order:"+id+" item:"+food+" for:"+customer+"Served by "+wp;
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
	private SynchronousQueue<Plate> placeSetting=new SynchronousQueue<Plate>();
	public Customer(WaitPerson wp)
	{
		this.wp=wp;
	}
	public void deliver(Plate p)throws Exception
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
				wp.placeOrder(this,food);
				System.out.println(this+" eating "+placeSetting.take());
			}
			catch(Exception e)
			{
				System.out.println(this+"waitting for "+course+"interrupted.");
				break;
			}
		}
		System.out.println(this+"finished meal,leaving");
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
	BlockingQueue<Plate> filledOrders=new LinkedBlockingQueue<Plate>();
	public WaitPerson(Restaurant restaurant)
	{
		super();
		this.restaurant = restaurant;
	}
	
	public void placeOrder(Customer c,Food f)
	{
		try
		{
			restaurant.orders.put(new Order(c,this,f));
		}
		catch(Exception e)
		{
			System.out.println(this+"placeOrder interrupted.");
			
		}
	}
	
	public void run()
	{
		try
		{
			while(!Thread.interrupted())
			{
				Plate plate=filledOrders.take();
				System.out.println(this+"拿到了 "+plate+"，分发给 "+plate.getOrder().getCustomer());
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
				Order order=restaurant.orders.take();
				Food requestedItem=order.item();
				TimeUnit.MICROSECONDS.sleep(200);
				Plate plate=new Plate(order,requestedItem);
				order.getWaitPerson().filledOrders.put(plate);
				
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
				WaitPerson wp=waitPersons.get(new Random().nextInt(waitPersons.size()));
				Customer c=new Customer(wp);
				exe.execute(c);
				TimeUnit.MICROSECONDS.sleep(100);
				
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
		TimeUnit.MILLISECONDS.sleep(100);
		exe.shutdownNow();
	}
}
