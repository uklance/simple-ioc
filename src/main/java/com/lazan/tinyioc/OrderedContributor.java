package com.lazan.tinyioc;

public interface OrderedContributor<T> {
	void contribute(OrderedConfiguration<T> configuration);
}
