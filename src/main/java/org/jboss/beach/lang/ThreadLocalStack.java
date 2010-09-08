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

import java.util.LinkedList;

/**
 * Provide thread-local variables whose association is maintained
 * until they are popped from the stack.
 * 
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 */
public class ThreadLocalStack<T> implements Current<T>
{
   private ThreadLocal<LinkedList<T>> stack = new ThreadLocal<LinkedList<T>>();

   /**
    * Associate an object as the current, pushing the previous one down.
    *
    * @param obj the object to associate as current
    */
   public void push(T obj)
   {
      LinkedList<T> list = stack.get();
      if (list == null)
      {
         list = new LinkedList<T>();
         stack.set(list);
      }
      list.addLast(obj);
   }

   /**
    * Disassociate the current object, the previously associated
    * object becomes the currently associated.
    *
    * @return the previous associated object
    */
   public T pop()
   {
      LinkedList<T> list = stack.get();
      if (list == null)
      {
         return null;
      }
      T rtn = list.removeLast();
      if (list.size() == 0)
      {
         stack.remove();
      }
      return rtn;
   }

   /**
    * Return the currently associated object.
    *
    * @return the current object
    */
   public T get()
   {
      LinkedList<T> list = stack.get();
      if (list == null)
      {
         return null;
      }
      return list.getLast();
   }
}
