package com.monitor.monitor;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class JmxReader {

    private static final int KB = 1024;
    private static final int MB = 1024*KB;
    private static final int GB = 1024*MB;


    private final JMXServiceURL jmxUrl;
    private JMXConnector jmxConnector;
    private MBeanServerConnection mbeanServerConnection;
    private OperatingSystemMXBean osMXBean;
    private ThreadMXBean threadMXBean;
    private MemoryMXBean memMXBean;
    private com.sun.management.OperatingSystemMXBean peOSMXBean;

    public JmxReader(String url, int port) {
        try {
            this.jmxUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + url + ":" + port + "/jmxrmi");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("URL is invalid: " + url, e);
        }
    }

    private void connectWhenNeeded() {
        if (jmxConnector != null) {
            return;
        }
        try {
            jmxConnector = JMXConnectorFactory.connect(jmxUrl);
            mbeanServerConnection = jmxConnector.getMBeanServerConnection();

            osMXBean = ManagementFactory.newPlatformMXBeanProxy(
                    mbeanServerConnection,
                    ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME,
                    OperatingSystemMXBean.class
            );
            threadMXBean = ManagementFactory.newPlatformMXBeanProxy(
                    mbeanServerConnection,
                    ManagementFactory.THREAD_MXBEAN_NAME,
                    ThreadMXBean.class
            );
            memMXBean = ManagementFactory.newPlatformMXBeanProxy(
                    mbeanServerConnection,
                    ManagementFactory.MEMORY_MXBEAN_NAME,
                    MemoryMXBean.class
            );

            peOSMXBean = ManagementFactory.newPlatformMXBeanProxy(
                    mbeanServerConnection,
                    ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME,
                    com.sun.management.OperatingSystemMXBean.class
            );
        } catch (IOException e) {
//            println("Could not connect to: " + jmxUrl + " due to: " + e.getMessage());
            reconnectNextRun();
        }
    }


    public Map<String, String> read() {
        connectWhenNeeded();
        if(jmxConnector == null) {
            return null;
        }
        Map<String, String> props = new HashMap<>();

        try {
            props.put("sysload", String.valueOf(round(osMXBean.getSystemLoadAverage())));
            props.put("cpu-process", String.valueOf(round(peOSMXBean.getProcessCpuLoad() * 100)));
            props.put("cpu-system", String.valueOf(round(peOSMXBean.getSystemCpuLoad() * 100)));
            props.put("mem-heap", String.valueOf(round(memMXBean.getHeapMemoryUsage().getUsed() / MB )));
            props.put("threads", String.valueOf(threadMXBean.getThreadCount()));
        } catch (Exception  e) {
            println("Something went wrong try to reconnect the next attempt");
            reconnectNextRun();
        }
        return props;
    }

    private void reconnectNextRun() {
        jmxConnector = null; // To reconnect on next run
    }

    static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    void println(Object message) {
        String timestamp = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(ZonedDateTime.now());
        System.out.println(timestamp + " " + getClass().getSimpleName() +" - " + message);
    }
}
