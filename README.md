# tiny-ioc [![Build Status](https://travis-ci.org/uklance/tiny-ioc.svg?branch=master)](https://travis-ci.org/uklance/tiny-ioc) [![Coverage Status](https://coveralls.io/repos/uklance/tiny-ioc/badge.svg?branch=master)](https://coveralls.io/r/uklance/tiny-ioc?branch=master) [![Download](https://api.bintray.com/packages/uklance/maven/tiny-ioc/images/download.svg) ](https://bintray.com/uklance/maven/tiny-ioc/_latestVersion)

A lightweight IOC container with minimal dependencies inspired by [google-guice](https://github.com/google/guice) and [tapestry-ioc](https://tapestry.apache.org/ioc.html) 

## Building and Registering Services With the IOC Container

```java
import com.lazan.tinyioc.annotations.*;
public class MyModule {
    @Bind
    public void bind(ServiceBinder binder) {
           binder.bind(Service1.class, Service1Impl.class);
    }
    
    @Service
    public Service2 service2() {
        return new Service2Impl("xxx");
    }
}
```

```java
import com.lazan.tinyioc.*;
public class MyMain {
    public static void main(String[] args) {
        ServiceRegistry registry = new ServiceRegistryBuilder()
            .withModuleType(MyModule.class)
            .build();
        
        Service1 s1 = registry.getService(Service1.class);
        Service2 s2 = registry.getService(Service2.class);
    }
}
```

## Dependency Injection

```java
import javax.inject.Inject;
public class Service1Impl implements Service1 {
    @Inject private Dependency1;
    @Inject private Dependency2;

    public Dependency1 getDependency1() { return dependency1; }
    public Dependency2 getDependency2() { return dependency2; }
}
```

```java
public class Service2Impl implements Service2 {
    private final Dependency1 dependency1;
    private final Dependency2 dependency2;
    
    public Service2Impl(Dependency1 dependency1, Dependency2 dependency2) {
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
    }
    
    public Dependency1 getDependency1() { return dependency1; }
    public Dependency2 getDependency2() { return dependency2; }
}
```

```java
public class MyModule {
    @Bind
    public void bind(ServiceBinder binder) {
        binder.bind(Dependency1.class, Dependency1Impl.class);
        binder.bind(Dependency2.class, Dependency2Impl.class);
        binder.bind(Service1.class, Service1Impl.class);
        binder.bind(Service2.class, Service2Impl.class);
    }
}
```

```java
import com.lazan.tinyioc.*;
public class MyMain {
    public static void main(String[] args) {
        ServiceRegistry registry = new ServiceRegistryBuilder()
            .withModuleType(MyModule.class)
            .build();
            
        Dependency1 d1 = registry.getService(Dependency1.class);
        Dependency2 d2 = registry.getService(Dependency2.class);
        
        Service1 s1 = registry.getService(Service1.class);
        assert d1 == s1.getDependency1();
        assert d2 == s1.getDependency2();

        Service2 s2 = registry.getService(Service2.class);        
        assert d1 == s2.getDependency1();
        assert d2 == s2.getDependency2();
    }
}
```
