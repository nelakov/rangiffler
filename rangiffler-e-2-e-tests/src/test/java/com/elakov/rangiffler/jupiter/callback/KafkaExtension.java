package com.elakov.rangiffler.jupiter.callback;

import com.elakov.rangiffler.kafka.KafkaReader;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Starts the background KafkaReader once per test run and stops it when
 * the root context closes. Keyed by its own class (not TestSuiteCallback)
 * so it can coexist with other suite-level extensions in the same store.
 */
public class KafkaExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        context.getRoot().getStore(Namespace.GLOBAL)
                .getOrComputeIfAbsent(
                        KafkaExtension.class,
                        k -> {
                            KafkaReader kafkaReader = new KafkaReader();
                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            executor.execute(kafkaReader);
                            executor.shutdown();
                            return (ExtensionContext.Store.CloseableResource) kafkaReader::stop;
                        });
    }
}
