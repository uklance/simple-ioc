package com.lazan.tinyioc;

public interface ServiceBuilder<T> {
	T build(ServiceBuilderContext context);
}
