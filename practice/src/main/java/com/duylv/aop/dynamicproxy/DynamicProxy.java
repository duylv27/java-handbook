package com.duylv.aop.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy {

    private BankTransferService bankTransferService;
    private final InvocationHandler invocationHandler;

    public DynamicProxy(BankTransferService bankTransferService, InvocationHandler invocationHandler) {
        this.bankTransferService = bankTransferService;
        this.invocationHandler = invocationHandler;
        initProxy();
    }

    private void initProxy() {
        this.bankTransferService = (BankTransferService) Proxy.newProxyInstance(
                DynamicProxy.class.getClassLoader(),
                new Class<?>[]{BankTransferService.class},
                invocationHandler
        );
    }

    public String transfer() {
        return bankTransferService.transfer();
    }

}

interface BankTransferService {
    default String transfer() {
        return "Bank $100";
    }
}

class BankTransferServiceImpl implements BankTransferService {
    @Override
    public String transfer() {
        return BankTransferService.super.transfer();
    }
}

class BaseHandler implements InvocationHandler {

    private final Object targetObj;

    public BaseHandler(Object targetObj) {
        this.targetObj = targetObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("# Before " + method.getName());
        var returnVal = method.invoke(targetObj, args);
        System.out.println("# After " + method.getName());
        return returnVal;
    }

}
