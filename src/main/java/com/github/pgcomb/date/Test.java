package com.github.pgcomb.date;

/**
 * Title: Test <br>
 * Description: Test <br>
 * Date: 2018年08月21日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        DateLog.record();
        Thread.sleep(1000L);
        DateLog.record();
        Thread.sleep(1000L);
        DateLog.record();
        Thread.sleep(1000L);
        DateLog.record();
        Thread.sleep(1000L);
        DateLog.record();
        new Thread(() -> {
            DateLog.record();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DateLog.record();
        }).start();
        Thread.sleep(100000L);
    }
}
