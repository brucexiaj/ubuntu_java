package ImplementingTheProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
/**
 * ./Annotations/Implementing the processor
 * Generate a sql creating a table via annotations
 * @author brucexiajun
 * 20170507
 *
 */
public class TableCreator 
{
	private static final String arguments = "ImplementingTheProcessor.Member";
	public static void main(String [] args) throws Exception
	{
		Class<?> memberClass = Class.forName(arguments);
		DataBaseTable dataBaseTable = memberClass.getAnnotation(DataBaseTable.class);
		String tableName = dataBaseTable.name();
		List<String> columnDefaults = new ArrayList<String>();
		for(Field field:memberClass.getDeclaredFields())
		{
			String columnName = null;
			Annotation[] annotation = field.getDeclaredAnnotations();
			if(annotation.length==0)
			{
				continue;
			}
			if(annotation[0] instanceof SQLInteger)
			{
				SQLInteger sqlInteger = (SQLInteger)annotation[0];
				columnName = sqlInteger.name();
				columnDefaults.add(columnName+" INT"+getConstraints(sqlInteger.constraints()));
			}
			if(annotation[0] instanceof SQLString)
			{
				SQLString sqlString = (SQLString)annotation[0];
				columnName = sqlString.name();
				columnDefaults.add(columnName+" VARCHAR("+sqlString.value()+")"+getConstraints(sqlString.constraints()));
			}
			
		}
		StringBuilder sb = new StringBuilder("CREATE TABLE "+tableName+"(");
		for(String columnDefault:columnDefaults)
		{
			sb.append("\n "+columnDefault+",");
			
		}
		
		sb.deleteCharAt(sb.length()-1);
		sb.append(");");
		System.out.println(sb);
	}
	
	private static String getConstraints(Constraints constraints)
	{
		String constraint = "";
		if(!constraints.allowNull())
		{
			constraint+=" NOT NULL";
		}
		if(constraints.primaryKey())
		{
			constraint+=" PRIMARY KEY";
		}
		if(constraints.unique())
		{
			constraint+=" UNIQUE";
		}
		
		
		return constraint;
		
	}
}
