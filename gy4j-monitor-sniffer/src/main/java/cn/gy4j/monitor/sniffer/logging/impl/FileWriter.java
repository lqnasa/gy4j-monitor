package cn.gy4j.monitor.sniffer.logging.impl;

import cn.gy4j.monitor.sniffer.config.AgentConfig;
import cn.gy4j.monitor.sniffer.constant.Constants;
import cn.gy4j.monitor.sniffer.logging.api.IWriter;
import cn.gy4j.monitor.sniffer.util.DateUtil;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * 基于文件输出的Writer.
 * <p>
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class FileWriter implements IWriter, EventHandler<FileWriter.LoggerEvent> {
    /**
     * 单例实例对象.
     */
    private static FileWriter INSTANCE;
    /**
     * 单例创建的锁对象.
     */
    private static final Object CREATE_LOCK = new Object();
    /**
     * 文件输出流.
     */
    private FileOutputStream fileOutputStream;
    /**
     * Disruptor对象：Disruptor是一个高性能的异步处理框架.
     */
    private Disruptor<LoggerEvent> disruptor;
    /**
     * Disruptor对象的环形缓存区对象.
     */
    private RingBuffer<LoggerEvent> buffer;
    /**
     * 是否启动.
     */
    private volatile boolean started = false;
    /**
     * 行数.
     */
    private volatile long lineNum;
    /**
     * 文件大小.
     */
    private volatile long fileSize;

    /**
     * 构造函数：初始化Disruptor相关.
     */
    private FileWriter() {
        disruptor = new Disruptor<LoggerEvent>(new EventFactory<LoggerEvent>() {
            @Override
            public LoggerEvent newInstance() {
                return new LoggerEvent();
            }
        }, 1024, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(this);
        buffer = disruptor.getRingBuffer();
        lineNum = 0;
        disruptor.start();
    }

    /**
     * 单例获取方法.
     *
     * @return
     */
    public static IWriter getInstance() {
        if (INSTANCE == null) {
            synchronized (CREATE_LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new FileWriter();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 日志输出：写入buffer，异步输出日志.
     *
     * @param message 日志信息
     */
    @Override
    public void write(String message) {
        // 拿到buffer里面的下一个可用序号.
        long seq = buffer.next();
        try {
            // 通过序号拿到buffer里面的对象.
            LoggerEvent logEvent = buffer.get(seq);
            // set日志信息.
            logEvent.setMessage(message);
        } finally {
            // 提交.
            buffer.publish(seq);
        }
    }

    /**
     * 通过文件流输出日志.
     *
     * @param message    日志消息
     * @param forceFlush 是否flush
     */
    private void write(String message, boolean forceFlush) {
        try {
            fileOutputStream.write(message.getBytes());
            fileSize += message.getBytes().length;
            if (forceFlush || lineNum % 20 == 0) {
                fileOutputStream.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            checkFile();
        }
    }

    /**
     * 日志事件处理.
     *
     * @param logEvent   日志事件对象
     * @param sequence   序号
     * @param endOfBatch 是否批量结束
     */
    @Override
    public void onEvent(LoggerEvent logEvent, long sequence, boolean endOfBatch) {
        if (hasWriterStream()) {
            try {
                lineNum++;
                write(logEvent.getMessage() + Constants.LINE_SEPARATOR, endOfBatch);
            } finally {
                logEvent.setMessage(null);
            }
        }
    }

    /**
     * 检查日志文件:如果文件大小超过限制，重命名日志文件.
     */
    private void checkFile() {
        if (fileSize > AgentConfig.Logging.MAX_FILE_SIZE) {
            // flush文件流
            forceExecute(new Callable() {
                @Override
                public Object call() throws Exception {
                    fileOutputStream.flush();
                    return null;
                }
            });
            // 关闭文件流
            forceExecute(new Callable() {
                @Override
                public Object call() throws Exception {
                    fileOutputStream.close();
                    return null;
                }
            });
            // 重命名文件
            forceExecute(new Callable() {
                @Override
                public Object call() throws Exception {
                    new File(AgentConfig.Logging.PATH, AgentConfig.Logging.FILE_NAME)
                            .renameTo(new File(AgentConfig.Logging.PATH,
                                    AgentConfig.Logging.FILE_NAME + "." + DateUtil.getFormatForFile(new Date())));
                    return null;
                }
            });
            // 重置文件流对象和启动状态
            forceExecute(new Callable() {
                @Override
                public Object call() throws Exception {
                    fileOutputStream = null;
                    started = false;
                    return null;
                }
            });
        }
    }

    /**
     * 强制执行，忽略异常.
     *
     * @param callable Callable实例
     */
    private void forceExecute(Callable callable) {
        try {
            callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否存在文件输出流.
     *
     * @return
     */
    private boolean hasWriterStream() {
        if (fileOutputStream != null) {
            return true;
        }
        if (!started) {
            // 检查日志目录是否存在
            File logFilePath = new File(AgentConfig.Logging.PATH);
            if (!logFilePath.exists()) {
                logFilePath.exists();
            } else if (!logFilePath.isDirectory()) {
                System.err.println("Log dir(" + AgentConfig.Logging.PATH + ") is not a directory.");
            }
            try {
                // 获取日志文件流和文件大小
                File logFile = new File(AgentConfig.Logging.PATH + AgentConfig.Logging.FILE_NAME);
                fileOutputStream = new FileOutputStream(logFile, true);
                fileSize = logFile.length();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            started = true;
        }
        return fileOutputStream != null;
    }

    /**
     * 日志事件对象.
     */
    public static class LoggerEvent {
        /**
         * 日志消息.
         */
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
