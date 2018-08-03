package com.github.pgcomb.course;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * Title: MemoryDelayCache <br>
 * Description: MemoryDelayCache <br>
 * Date: 2018年08月03日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class MemoryDelayCache<T> implements DelayCache<T> {

    private int refreshInterval = 5;

    private LocalDateTime expiresTime = LocalDateTime.MIN;

    private Supplier<T> supplier;

    private T data;

    public MemoryDelayCache(@Nonnull Supplier<T> tSupplier) {
        this.supplier = tSupplier;
    }

    public MemoryDelayCache(int refreshInterval, @Nonnull Supplier<T> tSupplier) {
        this.supplier = tSupplier;
        this.refreshInterval = refreshInterval;
    }

    @Override
    public Supplier<T> supplier() {
        return supplier;
    }

    @Override
    public synchronized T obtain() {
        if (data ==null || expiresTime.isBefore(LocalDateTime.now())){
            data = supplier.get();
            expiresTime = LocalDateTime.now().plusSeconds(refreshInterval);
        }
        return data;
    }
}
