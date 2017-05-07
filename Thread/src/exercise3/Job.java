package exercise3;

public class Job 
{
	private boolean done=false;//作业开始是没有做的，学生需要去做。老师需要等待
	public void changeJobState()
	{
		done=done==false?true:false;
	}
	public boolean getState()
	{
		return done;
	}
}
