package com.wxy97.webhook.strategy;

import com.wxy97.webhook.watch.ExtensionChangedEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Comment;
import run.halo.app.core.extension.content.Post;
import run.halo.app.core.extension.content.Reply;
import run.halo.app.core.extension.notification.Reason;
import run.halo.app.extension.Extension;
import run.halo.app.extension.Unstructured;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 09:49
 * Description:
 */
@Component
public class StrategyFactory {
    private final Map<String, ExtensionStrategy> strategyMap;
    private final DefaultStrategy defaultStrategy; // 添加默认策略字段


    public StrategyFactory(ReplyStrategy replyStrategy,
                           ReasonStrategy reasonStrategy,
                           PostStrategy postStrategy,
                           CommentStrategy commentStrategy,
                           UnstructuredStrategy unstructuredStrategy,
                           DeviceStrategy deviceStrategy,
                           DefaultStrategy defaultStrategy) {
        strategyMap = new HashMap<>();
        strategyMap.put(ReplyStrategy.KIND, replyStrategy);
        strategyMap.put(ReasonStrategy.KIND, reasonStrategy);
        strategyMap.put(PostStrategy.KIND, postStrategy);
        strategyMap.put(CommentStrategy.KIND, commentStrategy);
        strategyMap.put(UnstructuredStrategy.KIND, unstructuredStrategy);
        strategyMap.put(DeviceStrategy.KIND, deviceStrategy);

        this.defaultStrategy = defaultStrategy;
        // Add more mappings if needed
    }

    public Optional<ExtensionStrategy> getStrategyForKind(String kind) {
        ExtensionStrategy strategy = strategyMap.get(kind);

        if (strategy == null) {
            // 如果没有找到特定策略，则返回默认策略
            return Optional.ofNullable(defaultStrategy);
        } else {
            return Optional.of(strategy);
        }


    }
}
