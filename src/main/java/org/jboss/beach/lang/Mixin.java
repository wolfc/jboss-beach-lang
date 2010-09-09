/*
 * JBoss, Home of Professional Open Source
 * Copyright (c) 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.beach.lang;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class Mixin implements InvocationHandler
{
   private Object[] delegates;
   private List<Class<?>> interfaces;
   private Map<Class<?>, Object> interfaceMap = new HashMap<Class<?>, Object>();

   protected Mixin(Object... delegates)
   {
      this.delegates = delegates;
      this.interfaces = new ArrayList<Class<?>>();
      if(delegates != null)
      {
         // make sure we get the first object as override
         for(int i = delegates.length - 1; i >= 0; i--)
         {
            Object delegate = delegates[i];
            Class<?> delegateInterfaces[] = delegate.getClass().getInterfaces();
            for(Class<?> delegateInterface : delegateInterfaces)
            {
               interfaces.add(delegateInterface);
               do
               {
                  interfaceMap.put(delegateInterface, delegate);
                  delegateInterface = delegateInterface.getSuperclass();
               } while(delegateInterface != null);
            }
         }
      }
   }

   public Class<?>[] getInterfaces()
   {
      return interfaces.toArray(new Class<?>[0]);
   }

   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      try
      {
         return method.invoke(interfaceMap.get(method.getDeclaringClass()), args);
      }
      catch(InvocationTargetException e)
      {
         throw e.getCause();
      }
   }

   public static Object newProxyInstance(ClassLoader loader, Object... delegates)
   {
      Mixin handler = new Mixin(delegates);
      return Proxy.newProxyInstance(loader, handler.getInterfaces(), handler);
   }
}
