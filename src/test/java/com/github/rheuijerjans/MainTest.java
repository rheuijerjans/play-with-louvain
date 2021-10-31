package com.github.rheuijerjans;

import com.sun.management.HotSpotDiagnosticMXBean;
import nl.cwts.networkanalysis.CPMClusteringAlgorithm;
import nl.cwts.networkanalysis.Clustering;
import nl.cwts.networkanalysis.LouvainAlgorithm;
import nl.cwts.networkanalysis.Network;
import org.junit.jupiter.api.Test;

import javax.management.MBeanServer;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Random;

class TestingSize {

    @Test
    void test() {

        final int nrOfNodes = 10000000;
        final int nrOfEdges = 40000000;
        final Random random = new Random();

        final int[] a = new int[nrOfEdges];
        final int[] b = new int[nrOfEdges];

        for (int i = 0; i < nrOfEdges; i++) {
            final int nodeA = random.nextInt(nrOfNodes);

            // nodeA must not be equal to nodeB
            int nodeB;
            do {
                nodeB = random.nextInt(nrOfNodes);
            } while (nodeA == nodeB);

            a[i] = nodeA;
            b[i] = nodeB;
        }

        final int[][] edges = {a, b};

        final Network network = new Network(
                nrOfNodes,
                true,
                edges,
                false,
                false); // fixme throws exception when checkIntegrity=true, not sure yet why

        final CPMClusteringAlgorithm algorithm = new LouvainAlgorithm(0.2, 0, new Random());

        System.out.println("NEdges=" + network.getNEdges());
        System.out.println("NNodes=" + network.getNNodes());

        final Clustering clustering = algorithm.findClustering(network);

        System.out.println("Q=" + algorithm.calcQuality(network, clustering));

        createHeapDump();

    }

    private void createHeapDump() {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
                    server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
            mxBean.dumpHeap("heapdump.hprof", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}