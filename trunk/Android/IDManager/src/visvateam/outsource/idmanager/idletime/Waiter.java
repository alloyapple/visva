package visvateam.outsource.idmanager.idletime;

import android.util.Log;

public class Waiter extends Thread{
	private static final String TAG=Waiter.class.getName();
    private long lastUsed;
    private long period;
    private boolean stop;

    public Waiter(long period)
    {
        this.period=period;
        stop=false;
    }

    public void run()
    {
        long idle=0;
        this.touch();
        do
        {
            idle=System.currentTimeMillis()-lastUsed;
			Log.d(TAG, "Application is idle for " + idle + " ms");
            try
            {
                Thread.sleep(5000); //check every 5 seconds
            }
            catch (InterruptedException e)
            {
                Log.d(TAG, "Waiter interrupted!");
            }
            if(idle > period)
            {
                idle=0;
                //do something here - e.g. call popup or so
            }
        }
        while(!stop);
        Log.d(TAG, "Finishing Waiter thread");
    }

    public synchronized void touch()
    {
        lastUsed=System.currentTimeMillis();
    }

    public synchronized void forceInterrupt()
    {
        this.interrupt();
    }

    //soft stopping of thread
    public synchronized void stop(boolean b)
    {
    	super.stop();
        stop=true;
    }

    public synchronized void setPeriod(long period)
    {
        this.period=period;
    }

}
