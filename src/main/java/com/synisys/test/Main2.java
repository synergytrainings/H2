package com.synisys.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main2 {
	private static final int THREAD_CONST = 100;
	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_CONST);
		int i = THREAD_CONST;
		/*while (i-- > 0) {
			executorService.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});

		}*/
		
		int j = THREAD_CONST;
		//new SelectTask().run();
		while (j-- > 0) {
			//executorService.execute(new SelectTask());
			//new Thread(new SelectTask()).start();

		}
		executorService.shutdown();
	}
}
