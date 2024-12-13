package com.duylv.aop.dynamicproxy;

public class Main {

    public static void main(String[] args) {
        var srv = new BankTransferServiceImpl();
        DynamicProxy dynamicProxy = new DynamicProxy(srv, new BaseHandler(srv));
        System.out.println(dynamicProxy.transfer());
    }

}
