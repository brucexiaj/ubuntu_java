package UsingAptToProcessAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

public class InterfaceExtractorProcessor implements AnnotationProcessor
{
	private final AnnotationProcessorEnvironment annotationProcessorEnvironment;
	private ArrayList<MethodDeclaration> methodDeclarations = new ArrayList<MethodDeclaration>();
	public InterfaceExtractorProcessor(AnnotationProcessorEnvironment annotationProcessorEnvironment)
	{
		super();
		this.annotationProcessorEnvironment = annotationProcessorEnvironment;
	}
	
	public void process()
	{
		for(TypeDeclaration typeDeclaration:annotationProcessorEnvironment.getSpecifiedTypeDeclarations())
		{
			ExtractInterface extractInterfaceAnnotation = typeDeclaration.getAnnotation(ExtractInterface.class);
			if(extractInterfaceAnnotation == null)
			{
				break;
			}
			for(MethodDeclaration methodDeclaration:typeDeclaration.getMethods())
			{
				if(methodDeclaration.getModifiers().contains(Modifier.PUBLIC)&&!(methodDeclaration.getModifiers().contains(Modifier.STATIC)))
				{
					methodDeclarations.add(methodDeclaration);
				}
			}
			if(methodDeclarations.size()>0)
			{
				try
				{
					PrintWriter printWriter = annotationProcessorEnvironment.getFiler().createSourceFile(extractInterfaceAnnotation.value());
					printWriter.println("package "+typeDeclaration.getPackage().getQualifiedName()+";");
					printWriter.println("public interface "+extractInterfaceAnnotation.value()+"{");
					for(MethodDeclaration methodDeclaration:methodDeclarations)
					{
						printWriter.print(" public ");
						printWriter.print(methodDeclaration.getReturnType()+" ");
						printWriter.print(methodDeclaration.getSimpleName()+" (");
						int i = 0;
						for(ParameterDeclaration parameterDeclaration:methodDeclaration.getParameters())
						{
							printWriter.print(parameterDeclaration.getType()+" "+parameterDeclaration.getSimpleName());
							if(++i<methodDeclaration.getParameters().size())
							{
								printWriter.print(", ");
							}
						}
						printWriter.println(");");
						
					}
					printWriter.println("}");
					printWriter.close();
				}
				catch(IOException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		
	}
	
}
