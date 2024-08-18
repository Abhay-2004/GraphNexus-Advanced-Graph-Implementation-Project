package Abhay.GraphNexus;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class E10KGraphTest {
    private static final String FILE_NAME = "e10k.txt";
    private static final int ITERATIONS = 5;
    private static final Random random = new Random(42); // Use a fixed seed for reproducibility

    public static void main(String[] args) {
        Graph g = new GraphImpl();
        try {
            System.out.println("Testing e10k.txt graph");
            System.out.println("=======================");

            // Load the graph
            long loadStartTime = System.nanoTime();
            g.load(FILE_NAME);
            long loadEndTime = System.nanoTime();
            System.out.printf("Load time: %.3f ms%n%n", nanoToMs(loadEndTime - loadStartTime));

            // Get vertex and edge counts
            int vertexCount = g.getVertexCount();
            int edgeCount = g.getEdgeCount();
            System.out.println("Graph Statistics:");
            System.out.println("Vertex Count: " + vertexCount);
            System.out.println("Edge Count: " + edgeCount);
            System.out.println();

            // Test getMST
            List<String> mst = measureOperation(g::getMST, "getMST");
            System.out.println("MST edge count: " + mst.size() / 2);
            System.out.println();

            // Test getShortestPaths
            String startVertex = g.getVertices().iterator().next();
            Map<String, Integer> shortestPaths = measureOperation(() -> g.getShortestPaths(startVertex), "getShortestPaths");
            System.out.println("Shortest paths from vertex " + startVertex + ":");
            printSampleResults(shortestPaths, 10);
            System.out.println();

            // Test getReport
            Set<String> report = measureOperation(() -> g.getReport(startVertex, mst), "getReport");
            System.out.println("Report from vertex " + startVertex + ":");
            printSampleResults(report, 10);

        } catch (Exception e) {
            System.err.println("An error occurred during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static <T> T measureOperation(Supplier<T> operation, String operationName) {
        long totalTime = 0;
        T result = null;
        for (int i = 0; i < ITERATIONS; i++) {
            long startTime = System.nanoTime();
            result = operation.get();
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }
        double averageTime = nanoToMs(totalTime) / ITERATIONS;
        System.out.printf("Average time for %s: %.3f ms%n", operationName, averageTime);
        return result;
    }

    private static double nanoToMs(long nano) {
        return TimeUnit.NANOSECONDS.toMillis(nano) + (nano % 1_000_000) / 1_000_000.0;
    }

    private static void printSampleResults(Map<String, Integer> results, int sampleSize) {
        int count = 0;
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
            if (++count >= sampleSize) break;
        }
        System.out.println("... (showing " + sampleSize + " out of " + results.size() + " results)");
    }

    private static void printSampleResults(Set<String> results, int sampleSize) {
        int count = 0;
        for (String item : results) {
            System.out.println(item);
            if (++count >= sampleSize) break;
        }
        System.out.println("... (showing " + sampleSize + " out of " + results.size() + " results)");
    }
}