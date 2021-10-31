package com.github.rheuijerjans;

import nl.cwts.networkanalysis.*;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {


        final CPMClusteringAlgorithm algorithm = new LouvainAlgorithm(0.1, 0, new Random());

        // network layout from https://github.com/CWTSLeiden/networkanalysis/#example
        final int[][] edges = {
                {0, 1, 2, 2, 3, 5, 4},
                {1, 2, 0, 3, 5, 4, 3}
        };

        final Network network  = new Network(
                6,
                true,
                edges,
                false,
                true);

        System.out.println("NEdges=" + network.getNEdges());
        System.out.println("NNodes=" + network.getNNodes());

        final Clustering clustering = algorithm.findClustering(network);

        System.out.println("Q="+algorithm.calcQuality(network, clustering));
        System.out.println("Clusters:");
        System.out.println(
                Stream.of(clustering.getNodesPerCluster())
                        .map(Arrays::toString)
                        .collect(Collectors.joining("\n")));
    }
}
