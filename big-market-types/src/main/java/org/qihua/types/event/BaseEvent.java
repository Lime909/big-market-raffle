package org.qihua.types.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author：Lime
 * @Description: 基础时间
 * @Date：2024/7/10 14:58
 */
@Data
public abstract class BaseEvent<T> {

    public abstract EventMessage<T> buildEventMessage(T data);
    public abstract String topic();

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventMessage<T> {
        private String id;
        private Date timestamp;
        private T data;
    }
}
