package fr.husta.test;

import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.junit.Test;

public class PrometheusMeterRegistryTest {

    @Test
    public void test_PrometheusMeterRegistry_with_JvmMemoryMetrics() {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        JvmMemoryMetrics jvmMemoryMetrics = new JvmMemoryMetrics();
        jvmMemoryMetrics.bindTo(prometheusRegistry);

        String scrape = prometheusRegistry.scrape();
        System.out.println(scrape);
    }

    @Test
    public void test_PrometheusMeterRegistry_with_ProcessorMetrics() {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        ProcessorMetrics processorMetrics = new ProcessorMetrics();
        processorMetrics.bindTo(prometheusRegistry);

        String scrape = prometheusRegistry.scrape();
        System.out.println(scrape);
    }

    @Test
    public void test_PrometheusMeterRegistry_with_FileDescriptorMetrics() {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        FileDescriptorMetrics fileDescriptorMetrics = new FileDescriptorMetrics();
        fileDescriptorMetrics.bindTo(prometheusRegistry);

        String scrape = prometheusRegistry.scrape();
        System.out.println(scrape);
    }

}
