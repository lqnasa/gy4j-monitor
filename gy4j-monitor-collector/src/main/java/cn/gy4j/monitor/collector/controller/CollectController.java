package cn.gy4j.monitor.collector.controller;

import cn.gy4j.monitor.collector.bean.TransportTracer;
import cn.gy4j.monitor.collector.constant.RemoteEvent;
import cn.gy4j.monitor.collector.entity.JvmInfo;
import cn.gy4j.monitor.collector.service.CollectService;
import cn.gy4j.monitor.collector.util.GsonUtil;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
@Slf4j
@RestController
public class CollectController {
    private static final Logger logger = LoggerFactory.getLogger(CollectController.class);

    private static final int BUFFER_SIZE = 1024 * 16;
    private static final int THREAD_COUNT = 8;

    private Disruptor<ContentEntity> disruptor;
    private RingBuffer<ContentEntity> buffer;

    /**
     * 初始化.
     */
    @PostConstruct
    public void init() {
        disruptor = new Disruptor<ContentEntity>(new EventFactory<ContentEntity>() {
            @Override
            public ContentEntity newInstance() {
                return new ContentEntity();
            }
        }, BUFFER_SIZE, DaemonThreadFactory.INSTANCE);
        ContentHandler[] handlers = new ContentHandler[THREAD_COUNT];
        for (int i = 0; i < handlers.length; i++) {
            handlers[i] = new ContentHandler();
        }
        disruptor.handleEventsWithWorkerPool(handlers);
        buffer = disruptor.getRingBuffer();
        disruptor.start();
    }

    @Autowired
    private CollectService collectService;

    /**
     * 采集接口.
     *
     * @param event   采集事件
     * @param content 采集内容
     * @return
     */
    @RequestMapping("collect")
    public boolean collect(RemoteEvent event, String content) {
        try {
            if (event == RemoteEvent.HEART_BEAT) {
                return true;
            } else {
                //异步队列多线程处理
                saveContent(event, content);
                return true;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return false;
    }

    private void saveContent(RemoteEvent remoteEvent, String content) {
        // 如果采集的队列满了，则抛弃并给出警告，避免应用阻塞
        if (buffer.remainingCapacity() == 0) {
            logger.warn("队列满了！");
            return;
        }
        long next = buffer.next();
        try {
            ContentEntity transportEntity = buffer.get(next);
            transportEntity.setRemoteEvent(remoteEvent);
            transportEntity.setContent(content);
        } finally {
            buffer.publish(next);
        }
    }

    public class ContentEntity {
        private RemoteEvent remoteEvent;
        private String content;

        public RemoteEvent getRemoteEvent() {
            return remoteEvent;
        }

        public void setRemoteEvent(RemoteEvent remoteEvent) {
            this.remoteEvent = remoteEvent;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


    public class ContentHandler implements WorkHandler<ContentEntity> {
        @Override
        public void onEvent(ContentEntity entity) {
            try {
                if (entity.getRemoteEvent() == RemoteEvent.TRACER) {
                    TransportTracer transportTracer = GsonUtil.jsonToObject(entity.getContent(), TransportTracer.class);
                    collectService.saveTracer(transportTracer);
                } else if (entity.getRemoteEvent() == RemoteEvent.JVM) {
                    JvmInfo jvmInfo = GsonUtil.jsonToObject(entity.getContent(), JvmInfo.class);
                    collectService.saveJvm(jvmInfo);
                }
            } catch (Exception ex) {
                log.warn("写入异常：" + entity.getContent(), ex);
            } finally {
                entity.setRemoteEvent(null);
                entity.setContent(null);
            }
        }
    }
}
