package com.ostec.nats_jms_bridge_oom;

import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

public class Ticker {
    private static long targetRate = 1_000_000;
    private static AtomicLong sent = new AtomicLong();
    private static long start;
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) {
        try {
        	Options o = new Options.Builder().server("nats://locactus-vm-ser:4222").maxReconnects(-1).build();
            Connection nc = Nats.connect(o);
            long published = 0;

            System.out.println("Running publisher...");

            start = System.nanoTime();
            while (true) {
                nc.publish("updates", String.format("%s - %d", "nats", published).getBytes(StandardCharsets.UTF_8));
                adjustAndSleep(nc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String humanBytes(double bytes) {
        int base = 1024;
        String[] pre = new String[] {"k", "m", "g", "t", "p", "e"};
        String post = "b";
        if (bytes < (long) base) {
            return String.format("%.2f b", bytes);
        }
        int exp = (int) (Math.log(bytes) / Math.log(base));
        int index = exp - 1;
        String units = pre[index] + post;
        return String.format("%.2f %s", bytes / Math.pow((double) base, (double) exp), units);
    }

	public static void adjustAndSleep(Connection nc) throws InterruptedException {

        long count = sent.incrementAndGet();

        if (count % 1000 != 0) { // Only sleep every 1000 message
            return;
        }

        long now = System.nanoTime();
        double rate = (1e9 * (double) count)/((double)(now - start));
        double delay = (1.0/((double)targetRate));
        double adjust = delay / 20.0; // 5%
        
		if (adjust == 0) {
			adjust = 1e-9; // 1ns min
        }
        
		if (rate < targetRate) {
			delay -= adjust;
		} else if (rate > targetRate) {
			delay += adjust;
        }
        
		if (delay < 0) {
			delay = 0;
        }

        delay = delay * 1000; // we are doing this every 1000 messages
        
        long nanos = (long)(delay * 1e9);
        LockSupport.parkNanos(nanos);

        if (count % 1_000_000 == 0) {
            try {
                nc.flush(Duration.ZERO);
            } catch (TimeoutException e) {
                // ignore it for test
            }
            System.out.printf("%s: Sent %s messages.  [%s, %s]\n", formatter.format(new Date()),
                                            NumberFormat.getIntegerInstance().format(count),
                                            humanBytes(Runtime.getRuntime().freeMemory()),
                                            humanBytes(Runtime.getRuntime().totalMemory()));
        }
	}
}