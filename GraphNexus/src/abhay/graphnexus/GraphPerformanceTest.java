package Abhay.GraphNexus;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class GraphPerformanceTest {
    private static final int[] GRAPH_SIZES = {10, 100, 1000, 10000};
    private static final int ITERATIONS = 5;
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        for (int size : GRAPH_SIZES) {
            System.out.println("Testing graph with " + size + " vertices");
            testGraphPerformance(size);
            System.out.println("----------------------------------------");
        }
    }

    private static void testGraphPerformance(int size) {
        Graph g = new GraphImpl();
        List<String> edges = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();


        for (int i = 1; i < size; i++) {
            edges.add(String.valueOf(0));
            edges.add(String.valueOf(i));
            weights.add(i);
        }

        for (int i = 0; i < size * 2; i++) {
            int u = random.nextInt(size);
            int v = random.nextInt(size);
            if (u != v) {
                edges.add(String.valueOf(u));
                edges.add(String.valueOf(v));
                weights.add(random.nextInt(100) + 1);
            }
        }

        try {
            // Measure load time
            long startTime = System.nanoTime();
            g.load(edges, weights);
            long endTime = System.nanoTime();
            System.out.printf("Load time: %.3f ms%n", nanoToMs(endTime - startTime));

            // Measure getVertexCount
            measureMethod(g::getVertexCount, "getVertexCount");

            // Measure getEdgeCount
            measureMethod(g::getEdgeCount, "getEdgeCount");

            // Measure hasVertex
            measureMethod(() -> g.hasVertex(String.valueOf(random.nextInt(size))), "hasVertex");

            // Measure getVertices
            measureMethod(() -> {
                for (String v : g.getVertices()) {
                    // Just iterate
                }
            }, "getVertices");

            // Measure hasEdge
            measureMethod(() -> {
                String u = String.valueOf(random.nextInt(size));
                String v = String.valueOf(random.nextInt(size));
                g.hasEdge(u, v);
            }, "hasEdge");

            // Measure getWeight
            measureMethod(() -> {
                String u = String.valueOf(random.nextInt(size));
                String v = String.valueOf(random.nextInt(size));
                g.getWeight(u, v);
            }, "getWeight");

            // Measure getAdjacent
            measureMethod(() -> {
                String u = String.valueOf(random.nextInt(size));
                for (String v : g.getAdjacent(u)) {
                    // Just iterate
                }
            }, "getAdjacent");

            // Measure getMST
            measureMethod(g::getMST, "getMST");

            // Measure getShortestPaths
            measureMethod(() -> g.getShortestPaths(String.valueOf(random.nextInt(size))), "getShortestPaths");

            // Measure getReport
            List<String> mst = g.getMST();
            measureMethod(() -> g.getReport(String.valueOf(random.nextInt(size)), mst), "getReport");

        } catch (Exception e) {
            System.err.println("An error occurred during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void measureMethod(Runnable method, String methodName) {
        long totalTime = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            long startTime = System.nanoTime();
            method.run();
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }
        double averageTime = nanoToMs(totalTime) / ITERATIONS;
        System.out.printf("Average time for %s: %.6f ms%n", methodName, averageTime);
    }

    private static double nanoToMs(long nano) {
        return TimeUnit.NANOSECONDS.toMillis(nano) + (nano % 1_000_000) / 1_000_000.0;
    }
}