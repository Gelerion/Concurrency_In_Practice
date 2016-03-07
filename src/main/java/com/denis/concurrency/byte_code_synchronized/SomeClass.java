package com.denis.concurrency.byte_code_synchronized;

public class SomeClass {
    /**
     * A dangerous case is what would happen if a lock is acquired via MonitorEnter,
     * but isn’t released via a corresponding call to a MonitorExit.
     * In this case the thread owning the lock can cause other threads who are trying to
     * obtain the lock to block indefinitely. It’s worth noting that since the lock is reentrant,
     * the thread owning the lock may continue to happily execute even if it were to reach
     * and reenter the same lock again.
     */
    public void hello() {
        synchronized (this) {
            System.out.println("Hi!, I'm alone here");
        }
    }

    /**
     * And here’s the catch. To prevent this from happening, the Java compiler generates matching
     * enter and exit instructions in such a way that once execution has entered into a synchronized
     * block or method, it must pass through a matching MonitorExit instruction for the same object.
     * One thing that can throw a wrench into this, is if an exception is thrown within the critical section.
     *
     aload_0       //load this into the operand stack
     dup           //load it again
     astore_1      //backup this into an implicit variable stored at register 1
     monitorenter  //pop the value of this from the stack to enter the monitor

     //the actual critical section
     getstatic java/lang/System/out Ljava/io/PrintStream;
     ldc "Hi!, I'm alone here"
     invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V

     aload_1     //load the backup of this
     monitorexit //pop up the var and exit the monitor
     goto 14     // completed - jump to the end

     // the added catch clause - we got here if an exception was thrown -
     aload_1     // load the backup var.
     monitorexit //exit the monitor
     athrow      // rethrow the exception object, loaded into the operand stack
     return
     */


}
