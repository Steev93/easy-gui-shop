package pers.zhangyang.easyguishop.util;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.manager.ConnectionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;


public class TransactionInvocationHandler implements InvocationHandler {

    private final Object target;

    public TransactionInvocationHandler(Object target) {
        this.target = target;
    }

    @Nullable
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Connection connection = ConnectionManager.INSTANCE.getConnection();
        Object obj;
        try {
            obj = method.invoke(target, args);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e.getCause() == null ? e : e.getCause();
            //处理的是什么异常，继续往上抛什么异常
        } finally {
            connection.close();
        }
        return obj;
    }

    @NotNull
    public Object getProxy() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

}