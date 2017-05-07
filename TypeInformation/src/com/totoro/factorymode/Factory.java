package com.totoro.factorymode;
//工厂，带有一个泛型参数的接口，含有一个返回泛型类的方法
public interface Factory<T> {
	T create();

}
