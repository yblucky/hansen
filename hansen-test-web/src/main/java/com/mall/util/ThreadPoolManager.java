package com.mall.util;
/** 
* @author   zzwei 
* @version  2016年6月13日 下午5:18:05 
*
*/ 


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolManager {  
	private static  int  CORE_POOL_SIZE =1;
	private static  int  MAXI_MUM_POOL_SIZE=5000;
	private static  int  KEEP_ALIVE_TIME=1000;
	private static  int  QUEUE_SIZE=500;
	private ThreadPoolExecutor pool =null;
	private Runnable thread;
    public ThreadPoolManager() {
    	if (pool==null) {
    		this.pool = new ThreadPoolExecutor(  
    	    		CORE_POOL_SIZE,  
    	    		MAXI_MUM_POOL_SIZE,  
    	    		KEEP_ALIVE_TIME,  
    	            TimeUnit.MINUTES,  
    	            new ArrayBlockingQueue<Runnable>(QUEUE_SIZE),  
    	            new CustomThreadFactory(),  
    	            new CustomRejectedExecutionHandler()); 
		}
    }
    public void exec(Runnable thread){
    	this.setThread(thread);
    	System.out.println("线程名称 " + Thread.currentThread().getName()+"  " + Thread.currentThread().getId());
    	this.pool.execute(thread);
    }
    public Runnable getThread() {
		return thread;
	}
	public void setThread(Runnable thread) {
		this.thread = thread;
	}  

    public void init() {  
        this.pool = new ThreadPoolExecutor(  
        		CORE_POOL_SIZE,  
        		MAXI_MUM_POOL_SIZE,  
        		KEEP_ALIVE_TIME,  
                TimeUnit.MINUTES,  
                new ArrayBlockingQueue<Runnable>(QUEUE_SIZE),  
                new CustomThreadFactory(),  
                new CustomRejectedExecutionHandler());  
    }  
  
      
    public void destory() {  
        if(pool != null) {  
            pool.shutdownNow();  
        }  
    }  
      
      
    public ExecutorService getThreadPoolExecutor() {  
        return this.pool;  
    }  
      
    private class CustomThreadFactory implements ThreadFactory {  
  
        private AtomicInteger count = new AtomicInteger(0);  
          
        @Override  
        public Thread newThread(Runnable r) {  
            Thread t = new Thread(r);  
            String threadName = ThreadPoolManager.class.getSimpleName() + count.addAndGet(1);  
            System.out.println(threadName);  
            t.setName(threadName);  
            return t;  
        }  
    }  
      
      
    private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {  
  
        @Override  
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {  
        	try {
				executor.getQueue().put(r);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }  
    }  
      
      
      
    public static void main(String[] args) {  
    	
    	ThreadPoolManager manager = new ThreadPoolManager();
    	ExecutorService pool = manager.getThreadPoolExecutor();
    	System.out.println(pool);
    	final int i=0;
    	manager.exec(new Runnable() {
			
			@Override
			public void run() {
				for (int j = 0; j < 100; j++) {
				}
			}
		});
    }
	
}  