package fr.husta.test;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class JmxMeterRegistryTest {

    private static MBeanServer platformMBeanServer;

    @BeforeClass
    public static void setUpClass() throws Exception {
        platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    @Test
    public void test_JmxMeterRegistry_with_JvmMemoryMetrics() throws MalformedObjectNameException, InstanceNotFoundException, MBeanException, AttributeNotFoundException, ReflectionException {
        JmxMeterRegistry jmxMeterRegistry = new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);

        JvmMemoryMetrics jvmMemoryMetrics = new JvmMemoryMetrics();
        jvmMemoryMetrics.bindTo(jmxMeterRegistry);

        // we can have JMX MBean like "metrics:name=jvmMemoryUsed.???"
        // check with JVisualVM for example
        for (Meter meter : jmxMeterRegistry.getMeters()) {
            System.out.println(String.format(" * %s (%s) -- %s", meter.getId().getName(), meter.getId().getDescription(),
                    meter.getId().getType()));
            System.out.println(String.format(" --> %s", meter.measure()));
        }

        ObjectName objectName = new ObjectName("metrics:name=jvmMemoryUsed.area.heap.id.PS_Eden_Space");
        ObjectInstance objectInstance = platformMBeanServer.getObjectInstance(objectName);
        assertThat(objectInstance).isNotNull();

        Object value = platformMBeanServer.getAttribute(objectName, "Value");
        assertThat(value).isNotNull();
        System.out.println("JMX : jvmMemoryUsed.area.heap.id.PS_Eden_Space = " + value);
        BigDecimal valueBd = new BigDecimal(String.valueOf(value));
        System.out.println(" ~ " + valueBd.divideToIntegralValue(new BigDecimal(1024 * 1024)) + " Mb");
    }

    @Test
    public void test_JmxMeterRegistry_with_ProcessorMetrics() throws MalformedObjectNameException, InstanceNotFoundException, MBeanException, AttributeNotFoundException, ReflectionException {
        JmxMeterRegistry jmxMeterRegistry = new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);

        ProcessorMetrics processorMetrics = new ProcessorMetrics();
        processorMetrics.bindTo(jmxMeterRegistry);

        // we can have JMX MBean like "metrics:name=systemCpuCount"
        // check with JVisualVM for example
        for (Meter meter : jmxMeterRegistry.getMeters()) {
            System.out.println(String.format(" * %s (%s) -- %s", meter.getId().getName(), meter.getId().getDescription(),
                    meter.getId().getType()));
            System.out.println(String.format(" --> %s", meter.measure()));
        }

        ObjectName objectName = new ObjectName("metrics:name=systemCpuCount");
        ObjectInstance objectInstance = platformMBeanServer.getObjectInstance(objectName);
        assertThat(objectInstance).isNotNull();

        Object value = platformMBeanServer.getAttribute(objectName, "Value");
        assertThat(value).isNotNull();
        System.out.println("JMX : systemCpuCount = " + value);
    }

}
