package Application;

import javafx.application.Platform;

import java.util.Date;

/**
 * Created by Sarah on 09/05/2015.
 */
public class EventThread implements Runnable {

    public static final int Event_SuperPillEaten=0;
    public static final int Event_SuperPillFading=1;

    private int interval;
    private int event;
    private boolean suspended=false;
    private boolean active=true;
    private Thread eventTimer=null;

    public void setParams(int interval, int event) {
        this.interval=interval;
        this.event=event;
    }

    public void start() {
        eventTimer=new Thread(this);
        eventTimer.start();
    }

    public synchronized void stop() {
        eventTimer=null;
        notify();
    }

    @Override
    public void run() {
        System.out.println("eventThread.run(), starting...");
        suspended=true;
        Thread thisThread=Thread.currentThread();
        /*while*/ if (eventTimer==thisThread) {
            try {
                System.out.println("eventThread.run(), "+new Date(System.currentTimeMillis())+" going to sleep for " + interval + "ms...");
                thisThread.sleep(interval);
                System.out.println("eventThread.run(), "+new Date(System.currentTimeMillis())+"... waking up!");

                // do some stuff in another thread
                System.out.println("eventThread.run(), doing something...");
                if (event == Event_SuperPillEaten) {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            Main.getInstance().SuperPillEffectIsWearingOff();
                        }
                    });
                } else if (event == Event_SuperPillFading) {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            Main.getInstance().SuperPillEffectHasWornOff();
                        }
                    });
                }
                System.out.println("eventThread.run(), ... done it!");

                // check if we're now suspended... and wait until we're not!
                /*
                System.out.println("eventThread.run(), now waiting, until we're needed again...");
                //if (suspended==true) {
                    //System.out.println("eventThread.run(), waiting...");
                    synchronized (this) {
                        while ((suspended == true) && (thisThread==eventTimer))
                            wait();
                    }
                //}
                System.out.println("eventThread.run(), oh, hello! (wait over).");
                */
            } catch (InterruptedException e) {
                System.out.println("eventThread.run(), interrupted.");
            }
        }
        System.out.println("eventThread.run(), done.");
    }
}
