package com.oxn.xiaoxiong.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 腾讯云对象存储（COS）配置类
 * -COSClient：用于基本的对象存储操作
 * -TransferManager：用于大文件上传和断点续传
 * -线程池：支持并发上传操作
 * 
 * @author xiaoxiong
 */
@Configuration
@Getter
public class TencentCOSConfig {

    @Value("${tencent.cos.secret-id}")
    private String secretId;

    @Value("${tencent.cos.secret-key}")
    private String secretKey;

    @Value("${tencent.cos.region}")
    private String region;

    @Value("${tencent.cos.bucket-name}")
    private String bucketName;

    /**
     * 腾讯云 COS 客户端，用于执行对象存储操作
     */
    private COSClient cosClient;

    /**
     * 传输管理器，用于大文件上传和断点续传
     */
    private TransferManager transferManager;

    /**
     * 线程池，用于支持并发上传操作
     */
    private ExecutorService threadPool;

    /**
     * 初始化腾讯云 COS 客户端和传输管理器
     * <li>创建 COS 客户端凭证</li>
     * <li>配置客户端区域信息</li>
     * <li>初始化 COSClient</li>
     * <li>创建固定大小为 8 的线程池</li>
     * <li>初始化 TransferManager，配置分片上传阈值（5MB）和最小分片大小（1MB）</li>
     */
    @PostConstruct
    public void init() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setRegion(new Region(region));

        cosClient = new COSClient(cred, clientConfig);

        // 创建固定大小为 8 的线程池，用于并发上传
        threadPool = Executors.newFixedThreadPool(8);

        // 初始化传输管理器
        transferManager = new TransferManager(cosClient, threadPool);

        // 配置传输管理器参数
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        // 设置分片上传阈值：文件大小超过 5MB 时使用分片上传
        transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
        // 设置最小分片大小：每个分片至少 1MB
        transferManagerConfiguration.setMinimumUploadPartSize(1 * 1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);
    }

    /**
     * 销毁资源
     * <p>
     * 在 Spring 容器关闭时自动执行，用于释放资源：
     * <ul>
     * <li>关闭 TransferManager 并等待所有上传任务完成</li>
     * <li>关闭线程池</li>
     * <li>关闭 COSClient（由 TransferManager 自动处理）</li>
     * </ul>
     * </p>
     */
    @PreDestroy
    public void destroy() {
        if (transferManager != null) {
            // 关闭传输管理器，true 表示等待所有任务完成后再关闭
            transferManager.shutdownNow(true);
        }
    }
}
