package com.hiaward.reflex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;


public class ReflectTest{
	
	private static ReflectTest reflectTest = new ReflectTest();
	
	public static void main(String[] args) {
		try {
			System.out.println(reflectTest.getClazz());
			
//			reflectTest.getObject();
			
			reflectTest.operateMethod();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//获得字节码的几种方式,得到信息:class test.dyy.Person  
	public Class<?> getClazz()throws Exception{  
	    //第一种方式  
	    Class<?> clazz=Class.forName("test.dyy.Person");  
	    //第二种方式  
	    clazz=Person.class;  
	    //第三种方式  
	    Person p=new Person();  
	    clazz=p.getClass();  
	    return clazz;  
	}
	
	//利用反射机制实例化对象  
	public void getObject()throws Exception{  
	    Class<?> clazz=Class.forName("test.dyy.Person");  
	    System.out.println("-------使用下面方法实例化对象，必须提供无参数构造方法--------------");  
	    Person person=(Person)clazz.newInstance();  
	    person.sayHello();  
	    System.out.println("---------实例化有参数构造方法，先获得所有构造方法，再进行实例化-----------");  
	    Constructor<?>[] cons=clazz.getConstructors();  
	    person=(Person)cons[0].newInstance();  
	    person.sayHello();  
	}  
	
	
	//利用反射机制操作属性  
	public void operateField()throws Exception{  
	    Class<?> clazz=Class.forName("test.dyy.Person");//获得字节码信息  
	    //访问特定的属性,只能获取public访问修饰的属性信息:public int test.dyy.Person.age  
	    System.out.println("-------得到类中特定的属性，该属性只能是public的---------");  
	    Field field=clazz.getField("personName");  
	    System.out.println(field);  
	    System.out.println("-------得到自身和父类的所有属性,属性只能是public的---------");  
	    //访问自身和父类的所有属性  
	    Field[] fields=clazz.getFields();  
	    for(Field field2:fields){  
	        System.out.println(field2);  
	    }  
	      
	    System.out.println("-------得到自身类中一切访问修饰符的特定属性--------");  
	    //可以获取一切访问修饰的属性信息，包括private修饰符修饰的属性  
	    field=clazz.getDeclaredField("personId");  
	    System.out.println(field);  
	    //要操作private属性，需要给其添加下面的权限  
	    field.setAccessible(true);  
	    Person p=(Person)clazz.newInstance();//创建Person的一个实例对象,调用无参数构造函数  
	    System.out.println("----------给属性赋值和取值------------");  
	    field.setInt(p, 23);//给属性赋值    
	    System.out.println(field.getInt(p));//取得相应属性的值  
	    System.out.println(field.getClass());//获得属性字节码文件  
	      
	    System.out.println("------得到自身类中一切访问修饰符的所有属性---------");  
	    fields=clazz.getDeclaredFields();  
	    for(Field field2:fields){  
	        System.out.println(field2);  
	    }  
	      
	    System.out.println("----------取得属性访问修饰符------------------");  
	    System.out.println(Modifier.toString(field.getModifiers()));  
	}  
	
	
	//利用反射机制操作构造方法  
	public void operateConstructor()throws Exception{  
	    Class<?> clazz=Class.forName("test.dyy.Person");//获得字节码信息  
	    System.out.println("------调用自身无参public构造方法----------");  
	    Constructor<?> con=clazz.getConstructor();  
	    System.out.println(con);  
	    System.out.println("------调用自身有参public构造方法----------");  
	    con=clazz.getConstructor(String.class);  
	    System.out.println(con);  
	    System.out.println("----------调用自身任何访问修饰符构造方法--------------");  
	    con=clazz.getDeclaredConstructor(int.class);  
	    System.out.println(con);  
	    System.out.println("----------调用自身所有的public构造方法--------------");  
	    Constructor[] cons=clazz.getConstructors();  
	    for(Constructor con2:cons){  
	        System.out.println(con2);  
	    }  
	    System.out.println("----------调用自身所有的任意修饰符修饰的构造方法--------------");  
	    cons=clazz.getDeclaredConstructors();  
	    for(Constructor con2:cons){  
	        System.out.println(con2);  
	    }  
	    System.out.println("---------利用构造方法实例化对象-------------");  
	    cons=clazz.getConstructors();  
	    Person p1=(Person)cons[0].newInstance();  
	    System.out.println("实例化无参数构造方法:"+p1);  
	    p1=(Person)cons[1].newInstance("Mary");  
	    System.out.println("实例化有参数构造方法:"+p1);  
	}  
	
	
	//利用反射机制操作方法  
	public void operateMethod()throws Exception{  
	    Class<?> clazz=Class.forName("test.dyy.Person");//获得字节码信息  
	    System.out.println("--------获得自身类中所有的方法-----------");  
	    //获得自身类中所有的方法，private方法也可获取到,打印输出private int test.dyy.Person.mul(int,int)  
	    Method[] methods=clazz.getDeclaredMethods();  
	    for(Method method:methods){  
	        System.out.println(method);  
	    }  
	    System.out.println("--------获得自身类以及父类的所有的public方法，包括Object类-----------");  
	    //获得自身类public以及父类的public方法,还包括Object类  
	    Method[] methods2=clazz.getMethods();  
	    for(Method method:methods2){  
	        System.out.println(method);  
	    }  
	    System.out.println("---------获得自身类public修饰的特定方法-----------");  
	    Method method=clazz.getMethod("add", int.class,int.class);//获得add方法，add方法的访问修饰符需为public  
	    System.out.println(method.getModifiers()+"---"+method.getName()+"---"+method.getDefaultValue());  
	    System.out.println(method.invoke(clazz.newInstance(), 3,4));//执行方法  
	      
	    System.out.println("---------获得自身类任意访问修饰符修饰的特定方法--------");  
	    Method method2=clazz.getDeclaredMethod("mul", int.class,int.class);//获得mul方法，mul方法访问修饰符可以为任意修饰符，包括private  
	    System.out.println(method2.getModifiers()+"---"+method2.getName()+"---"+method2.getDefaultValue());  
	    System.out.println("访问修饰符为"+Modifier.toString(method2.getModifiers()));  
	    method2.setAccessible(true);//如果方法的访问修饰符为private，需要加上这个，否则调用invoke方法会报错  
	    System.out.println(method2.invoke(clazz.newInstance(), 3,4));//执行方法  
	      
	    System.out.println("---------获得方法参数类型---------");  
	    //获得方法mul中的参数类型  
	    Type[] types=method2.getGenericParameterTypes();  
	    for(Type type:types){  
	        System.out.println("方法中的参数类型为:"+type);  
	    }  
	      
	    System.out.println("---------获得方法返回值类型-----------");  
	    //获得方法mul中的返回值类型  
	    System.out.println("方法返回值类型为:"+method2.getGenericReturnType());  
	}  
	
	//使用反射操作父类信息  
	public void operateParent()throws Exception{  
	    Class<?> clazz=Class.forName("test.dyy.Person");//获得字节码信息  
	    System.out.println("-----获得父类字节码-------------");  
	    clazz=clazz.getSuperclass();  
	    System.out.println(clazz);  
	    System.out.println("-----获得父类构造函数信息-------------");  
	    Constructor<?>[] cons=clazz.getConstructors();  
	    for(Constructor<?> con:cons){  
	        System.out.println(con);  
	    }  
	}  

}