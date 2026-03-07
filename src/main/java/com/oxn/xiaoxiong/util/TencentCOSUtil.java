package com.oxn.xiaoxiong.util;

import com.oxn.xiaoxiong.config.TencentCOSConfig;
import com.oxn.xiaoxiong.enums.StatusCode;
import com.oxn.xiaoxiong.exception.ThrowUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Service
public class TencentCOSUtil {

    @Resource
    private TencentCOSConfig tencentCOSConfig;

    /**
     * 上传文件
     */
    public String uploadStream(String key, InputStream inputStream, long contentLength, String contentType) {
        String bucketName = tencentCOSConfig.getBucketName();
        COSClient cosClient = tencentCOSConfig.getCosClient();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            if (contentLength >= 0) {
                metadata.setContentLength(contentLength);
            }
            if (contentType != null && !contentType.isBlank()) {
                metadata.setContentType(contentType);
            }
            cosClient.putObject(bucketName, key, inputStream, metadata);
            return key;
        } catch (Exception e) {
            ThrowUtils.throwIf(true, StatusCode.SYSTEM_ERROR, e.getMessage());
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param key       COS 上的对象 key
     * @param localFile 本地保存的文件对象（若不存在会自动创建）
     * @return 下载完成后的本地文件
     */
    public File downloadFile(String key, File localFile) {
        String bucketName = tencentCOSConfig.getBucketName();
        TransferManager transferManager = tencentCOSConfig.getTransferManager();
        try {
            Download download = transferManager.download(bucketName, key, localFile);
            download.waitForCompletion();
            return localFile;
        } catch (Exception e) {
            ThrowUtils.throwIf(true, StatusCode.SYSTEM_ERROR, e.getMessage());
        }
        return null;
    }
}
