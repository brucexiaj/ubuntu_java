package writingAnnotationProcessors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 注释的定义
 * @author brucexiajun
 *
 */
@Target(ElementType.METHOD)//注释使用的地方
@Retention(RetentionPolicy.RUNTIME)//注释保持多久，这两个称为元注释
public @interface UseCase
{
	//自定义注释中的变量可以是任意的类型，甚至是注释类型
	public int id();
	public String description() default "no description";
}
