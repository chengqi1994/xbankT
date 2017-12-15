package test;


public class Test1{
	public static void main(String[] args) {
		MyThread myThread = new MyThread();
		new Thread(myThread,"第一个线程").start();
		new Thread(myThread,"第二个线程").start();
		new Thread(myThread,"第三个线程").start();
		new Thread(myThread,"第四个线程").start();
		new Thread(myThread,"第五个线程").start();
		new Thread(myThread,"第六个线程").start();
		new Thread(myThread,"第七个线程").start();
		new Thread(myThread,"第八个线程").start();
		new Thread(myThread,"第九个线程").start();
		new Thread(myThread,"第十个线程").start();
	}
}
