package LogBack_Prac;

public class Model2 {

    public static void main(String[] args) {

	CommonLog.writeDebug(CommonLog.DEFAULT_LOG, "Hello world.");

	Thread thread = new Thread() {

	    @Override
	    public void run() {
		CommonLog.writeDebug(CommonLog.ADCMOND_LOG, "adcmond1");
		Thread thread = new Thread() {

		    @Override
		    public void run() {
			CommonLog.writeDebug(CommonLog.ADCMOND_LOG, "adcmond2");
			CommonLog.writeDebug(CommonLog.DEFAULT_LOG, "default");
		    }
		};
		thread.start();

	    }
	};
	thread.start();
	Thread thread2 = new Thread() {
	    @Override
	    public void run() {
		CommonLog.writeDebug(CommonLog.SYSMOND_LOG, "sysmond1");

		Thread thread = new Thread() {
		    @Override
		    public void run() {
			CommonLog.writeDebug(CommonLog.SYSMOND_LOG, "sysmond2");
			CommonLog.writeDebug(CommonLog.DEFAULT_LOG, "default");
		    }
		};
		thread.start();
	    }
	};
	thread2.start();
	Thread thread3 = new Thread() {
	    @Override
	    public void run() {
		CommonLog.writeDebug(CommonLog.SYSLOGD_LOG, "syslog");
	    }
	};
	thread3.start();

    }

}
