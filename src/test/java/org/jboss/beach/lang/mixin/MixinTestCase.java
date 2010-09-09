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
package org.jboss.beach.lang.mixin;

import org.jboss.beach.lang.Mixin;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class MixinTestCase
{
   public static interface A
   {
      String getA();
   }

   public static interface B
   {
      String getB();
   }

   @Test
   public void test1()
   {
      A a = new A() {
         @Override
         public String getA()
         {
            return "A";
         }
      };

      B b = new B() {
         @Override
         public String getB()
         {
            return "B";
         }
      };

      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      Object mixin = Mixin.newProxyInstance(loader, a, b);

      assertEquals("A", ((A) mixin).getA());
      assertEquals("B", ((B) mixin).getB());
   }
}
