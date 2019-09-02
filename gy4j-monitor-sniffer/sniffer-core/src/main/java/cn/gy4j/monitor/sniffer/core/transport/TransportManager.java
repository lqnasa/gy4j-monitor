package cn.gy4j.monitor.sniffer.core.transport;

import cn.gy4j.monitor.sniffer.core.remote.RemoteManager;
import cn.gy4j.monitor.sniffer.core.trace.Tracer;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class TransportManager {
    private static final int BUFFER_SIZE = 1024 * 4;
    private static final int THREAD_COUNT = 8;
    private static TransportManager INSTANCE;
    private static final Object CREATE_LOCK = new Object();

    private Disruptor<TransportEntity> disruptor;
    private RingBuffer<TransportEntity> buffer;

    /**
     * 获取单例对象.
     *
     * @return
     */
    public static TransportManager getInstance() {
        if (INSTANCE == null) {
            synchronized (CREATE_LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new TransportManager();
                }
            }
        }
        return INSTANCE;
    }

    private TransportManager() {
        disruptor = new Disruptor<TransportEntity>(new EventFactory<TransportEntity>() {
            @Override
            public TransportEntity newInstance() {
                return new TransportEntity();
            }
        }, BUFFER_SIZE, DaemonThreadFactory.INSTANCE);
        TransportHandler[] handlers = new TransportHandler[THREAD_COUNT];
        for (int i = 0; i < handlers.length; i++) {
            handlers[i] = new TransportHandler();
        }
        disruptor.handleEventsWithWorkerPool(handlers);
        buffer = disruptor.getRingBuffer();
        disruptor.start();
    }

    /**
     * 采集MonitorTracer.
     *
     * @param tracer tracer对象
     */
    public void transport(Tracer tracer) {
        TransportTracer transportTracer = tracer.getTransportTracer();
        if (transportTracer.getSpans() == null || transportTracer.getSpans().size() == 0) {
            return;
        }
        transport(transportTracer.toString());
    }

    /**
     * push到queue进行采集.
     *
     * @param content 采集内存
     */
    public void transport(String content) {
        long next = buffer.next();
        try {
            TransportEntity transportEntity = buffer.get(next);
            transportEntity.setContent(content);
        } finally {
            buffer.publish(next);
        }
    }

    public static class TransportHandler implements WorkHandler<TransportEntity> {
        @Override
        public void onEvent(TransportEntity entity) {
            try {
                RemoteManager.send(entity.getContent());
            } finally {
                entity.setContent(null);
            }
        }
    }
}
