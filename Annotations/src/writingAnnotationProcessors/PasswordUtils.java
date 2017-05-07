package writingAnnotationProcessors;

import java.util.List;
/**
 * 使用了注释的类
 * @author brucexiajun
 *"没有哪一个非原始类可以赋值为NULL"
 */
public class PasswordUtils
{
	
	@UseCase(id=47,description="密码必须最少含有一个数字")
	public boolean validatePassword(String password)
	{
		return (password.matches("\\w*\\d\\w*"));
	}
	
	@UseCase(id=48)
	public String encryptPassword(String password)
	{
		return new StringBuilder(password).reverse().toString();
	}
	
	@UseCase(id=49,description="新密码不能和旧密码相同")
	public boolean checkForNewPassword(List<String> previousPassword,String password)
	{
		return !previousPassword.contains(password);
	}
}
