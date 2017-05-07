package writingAnnotationProcessors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * ./Annotations/Writing annotation processors
 * 注释处理器
 * @author brucexiajun
 *
 */
public class UseCaseTracker 
{
	public static void main(String [] args)
	{
		List<Integer> userCases = new ArrayList<Integer>();
		Collections.addAll(userCases, 47,48,49,50);
		for(Method method:PasswordUtils.class.getDeclaredMethods())
		{
			//当前的方法是否使用了注释
			UseCase useCase = method.getAnnotation(UseCase.class);
			if(useCase!=null)
			{
				System.out.println("useCase:"+useCase.id()+" "+useCase.description());
				userCases.remove(new Integer(useCase.id()));
			}
		}
		for(int i:userCases)
		{
			System.out.println("警告：漏掉了useCase-"+i);
		}
	}
}
