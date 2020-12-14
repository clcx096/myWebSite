package com.lcx.walle.test;

public class TestJavaDaemonThread {

    public static void main(String[] args) throws InterruptedException {
        Thread dt = new Thread(new DaemonThread(), "dt");
//        dt.setDaemon(true);//译者注：此次将User线程变为Daemon线程
        dt.start();
        //continue program
        Thread.sleep(6000);
        System.out.println("Finishing program");
    }

}

class DaemonThread implements Runnable{//译者注：此类虽类名是为Daemon线程，其实为User线程

    @Override
    public void run() {
        while(true){
            processSomething();
        }
    }
    private void processSomething() {
        try {
            System.out.println("Processing daemon thread");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
