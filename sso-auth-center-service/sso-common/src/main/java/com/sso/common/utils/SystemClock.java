/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 高并发场景下System.currentTimeMillis()的性能问题的优化
 * 时间戳打印建议使用
 *
 * @author 程序员小强
 */
public class SystemClock {
	private static final String THREAD_NAME = "system.clock";
	private static final SystemClock MILLIS_CLOCK = new SystemClock(1);
	private final long precision;
	private final AtomicLong now;

	private SystemClock(long precision) {
		this.precision = precision;
		now = new AtomicLong(System.currentTimeMillis());
		scheduleClockUpdating();
	}

	public static SystemClock millisClock() {
		return MILLIS_CLOCK;
	}

	private void scheduleClockUpdating() {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
			Thread thread = new Thread(runnable, THREAD_NAME);
			thread.setDaemon(true);
			return thread;
		});
		scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), precision, precision, TimeUnit.MILLISECONDS);
	}

	public long now() {
		return now.get();
	}
}
