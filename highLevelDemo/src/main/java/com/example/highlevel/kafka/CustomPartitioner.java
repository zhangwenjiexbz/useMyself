package com.example.highlevel.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * @author Sebastian
 * 
 * 自定义分区分配器：决定消息存放在哪个分区.。默认分配器使用轮询存放，轮到已满分区将会写入失败。
 * 
 */
public class CustomPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 获取topic所有分区
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int numPartitions = partitionInfos.size();
        // 消息必须有key
        if (null == key || !(key instanceof String)) {
            throw new InvalidRecordException("kafka message must have key");
        }
        // 如果只有一个分区，即0号分区
        if (numPartitions == 1) {
            return 0;
        }
        // 如果key为name，发送至最后一个分区
        if ("name".equals(key)) {
            return numPartitions-1;
        }
        
        return Math.abs(Utils.murmur2(keyBytes)) % numPartitions-1;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {
        
    }
}
