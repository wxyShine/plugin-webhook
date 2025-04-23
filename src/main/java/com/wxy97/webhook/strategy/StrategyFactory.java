package com.wxy97.webhook.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 09:49
 * Description:
 */
@Component
public class StrategyFactory {

    private final Map<String, ExtensionStrategy> strategyMap = new HashMap<>();
    private final DefaultStrategy defaultStrategy;

    @Autowired
    public StrategyFactory(List<ExtensionStrategy> strategies, DefaultStrategy defaultStrategy) {
        for (ExtensionStrategy strategy : strategies) {
            StrategyKind annotation = strategy.getClass().getAnnotation(StrategyKind.class);
            if (annotation != null) {
                strategyMap.put(annotation.value(), strategy);
            }
        }
        this.defaultStrategy = defaultStrategy;
    }

    public ExtensionStrategy getStrategyForKind(String kind) {
        return strategyMap.getOrDefault(kind, defaultStrategy);
    }
}
