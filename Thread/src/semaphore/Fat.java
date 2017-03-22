package semaphore;

public class Fat
{
	private volatile double d;
	private static int counter=0;
	private final int id=counter++;
	public Fat()
	{
		for(int i=0;i<10000;i++)
		{
			d+=(Math.E+Math.PI)/(double)i;
		}
	}
	
}
