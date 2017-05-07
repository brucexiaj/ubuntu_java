package exercise2;

public class Product
{
	//private static int id=0;
	//public final int count=id+1;
	private  boolean flag=false;
	public void changeFlag()
	{
		flag=flag==true?false:true;
	}
	public boolean getFlag()
	{
		return flag;
	}
	
	
}
