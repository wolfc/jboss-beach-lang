package org.jboss.beach.lang.current;

import org.jboss.beach.lang.ThreadLocalStack;
import org.jboss.beach.lang.reflect.CurrentInvocationHandler;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class CurrentTestCase
{
   public static interface Context
   {
      String getValue();
   }

   public static interface ExtendedContext extends Context
   {
      String getData();
   }

   /**
    * A naive implement would look something like this.
    * It could never be injected into a property that has ExtendedContext.
    */
   private static class NaiveCurrentContext implements Context
   {
      private static ThreadLocalStack<Context> current = new ThreadLocalStack<Context>();

      @Override
      public String getValue()
      {
         return current.get().getValue();
      }

      public static Context pop()
      {
         return current.pop();
      }

      public static void push(Context ctx)
      {
         current.push(ctx);
      }
   }

   @Test
   public void test1()
   {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      Class<?> interfaces[] = { ExtendedContext.class };
      ThreadLocalStack<Context> current = new ThreadLocalStack<Context>();
      CurrentInvocationHandler<Context> handler = new CurrentInvocationHandler<Context>(current);
      ExtendedContext proxy = (ExtendedContext) Proxy.newProxyInstance(loader, interfaces, handler);

      current.push(new ExtendedContext() {
         @Override
         public String getData()
         {
            return "data";
         }

         @Override
         public String getValue()
         {
            return "value";
         }
      });
      assertEquals("data", proxy.getData());
   }
}
