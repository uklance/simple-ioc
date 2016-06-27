package com.lazan.tinyioc.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.lazan.tinyioc.IocException;
import com.lazan.tinyioc.ServiceBuilder;
import com.lazan.tinyioc.ServiceBuilderContext;
import com.lazan.tinyioc.ServiceDecorator;

@SuppressWarnings({ "rawtypes", "unchecked" })
class ServiceReference {
	private final String serviceId;
	private final Class<?> serviceType;
	private final ServiceBuilder<?> builder;
	private final ServiceDecorator decorator;
	private volatile Object service;
	
	public ServiceReference(String serviceId, Class<?> serviceType, ServiceBuilder<?> builder, ServiceDecorator<?> decorator) {
		super();
		this.serviceId = serviceId;
		this.serviceType = serviceType;
		this.builder = builder;
		this.decorator = decorator;
	}

	public synchronized Object get(ServiceRegistryImpl registry) {
		if (service == null) {
			Set<String> serviceIdStack = registry.getServiceIdStack();
			if (serviceIdStack.contains(serviceId)) {
				List<String> references = new LinkedList<>(serviceIdStack);
				references.add(serviceId);
				throw new IocException("Circular dependency reference detected %s", references);
			}
			ServiceRegistryImpl registryWrapper = new ServiceRegistryImpl(registry, serviceId);
			ServiceBuilderContext context = new ServiceBuilderContextImpl(registryWrapper, serviceId, serviceType);
			
			Object candidate = builder.build(context);
			if (decorator != null) {
				service = decorator.decorate(candidate, context);
			} else {
				service = candidate;
			}
		}
		return service;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	
	public Class<?> getServiceType() {
		return serviceType;
	}
}