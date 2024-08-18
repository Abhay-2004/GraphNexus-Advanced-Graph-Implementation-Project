package abhay.graphnexus;

import java.util.*;

public class GraphTestCases {
    public static void main(String[] args) {
        testSimpleGraph();
        testComplexGraph();
        testDisconnectedGraph();
        testSingleVertexGraph();
        testLargeGraph();
    }

    private static void testSimpleGraph() {
        System.out.println("Test Case 1: Simple Graph");
        Graph g = new GraphImpl();
        try {
            List<String> edges = Arrays.asList("A", "B", "B", "C", "A", "C");
            List<Integer> weights = Arrays.asList(1, 2, 3);
            g.load(edges, weights);

            // Test vertex and edge counts
            assert g.getVertexCount() == 3 : "Vertex count should be 3";
            assert g.getEdgeCount() == 3 : "Edge count should be 3";

            // Test vertex existence
            assert g.hasVertex("A") : "Vertex A should exist";
            assert g.hasVertex("B") : "Vertex B should exist";
            assert g.hasVertex("C") : "Vertex C should exist";
            assert !g.hasVertex("D") : "Vertex D should not exist";

            // Test edge existence and weights
            assert g.hasEdge("A", "B") : "Edge A-B should exist";
            assert g.getWeight("A", "B") == 1 : "Weight of A-B should be 1";
            assert g.hasEdge("B", "C") : "Edge B-C should exist";
            assert g.getWeight("B", "C") == 2 : "Weight of B-C should be 2";
            assert g.hasEdge("A", "C") : "Edge A-C should exist";
            assert g.getWeight("A", "C") == 3 : "Weight of A-C should be 3";

            // Test adjacency
            Set<String> adjacentToA = new HashSet<>();
            for (String v : g.getAdjacent("A")) adjacentToA.add(v);
            assert adjacentToA.equals(new HashSet<>(Arrays.asList("B", "C"))) : "Adjacent to A should be B and C";

            // Test MST
            List<String> mst = g.getMST();
            assert mst.size() == 4 : "MST should have 4 elements (2 edges)";
            assert new HashSet<>(mst).equals(new HashSet<>(Arrays.asList("A", "B", "B", "C"))) : "MST should contain edges A-B and B-C";

            // Test shortest paths
            Map<String, Integer> sp = g.getShortestPaths("A");
            assert sp.get("A") == 0 : "Shortest path A to A should be 0";
            assert sp.get("B") == 1 : "Shortest path A to B should be 1";
            assert sp.get("C") == 3 : "Shortest path A to C should be 3";

            // Test report
            Set<String> report = g.getReport("A", mst);
            assert report.equals(new HashSet<>(Arrays.asList("A", "B", "C"))) : "Report should contain all vertices";

            System.out.println("Simple Graph Test Passed");
        } catch (Exception e) {
            System.err.println("Simple Graph Test Failed: " + e.getMessage());
        }
    }

    private static void testComplexGraph() {
        System.out.println("\nTest Case 2: Complex Graph");
        Graph g = new GraphImpl();
        try {
            g.load("complex_graph.txt");
            // complex_graph.txt:
            // 5
            // A B 1
            // B C 2
            // C D 3
            // D E 4
            // E A 5
            // A C 6
            // B D 7
            // C E 8

            // Test vertex and edge counts
            assert g.getVertexCount() == 5 : "Vertex count should be 5";
            assert g.getEdgeCount() == 8 : "Edge count should be 8";

            // Test MST
            List<String> mst = g.getMST();
            assert mst.size() == 8 : "MST should have 8 elements (4 edges)";
            Set<String> mstEdges = new HashSet<>();
            for (int i = 0; i < mst.size(); i += 2) {
                mstEdges.add(mst.get(i) + mst.get(i + 1));
            }
            assert mstEdges.containsAll(Arrays.asList("AB", "BC", "CD", "DE")) : "MST should contain edges A-B, B-C, C-D, D-E";

            // Test shortest paths
            Map<String, Integer> sp = g.getShortestPaths("A");
            assert sp.get("A") == 0 : "Shortest path A to A should be 0";
            assert sp.get("B") == 1 : "Shortest path A to B should be 1";
            assert sp.get("C") == 3 : "Shortest path A to C should be 3";
            assert sp.get("D") == 6 : "Shortest path A to D should be 6";
            assert sp.get("E") == 5 : "Shortest path A to E should be 5";

            System.out.println("Complex Graph Test Passed");
        } catch (Exception e) {
            System.err.println("Complex Graph Test Failed: " + e.getMessage());
        }
    }

    private static void testDisconnectedGraph() {
        System.out.println("\nTest Case 3: Disconnected Graph");
        Graph g = new GraphImpl();
        try {
            List<String> edges = Arrays.asList("A", "B", "C", "D");
            List<Integer> weights = Arrays.asList(1, 2);
            g.load(edges, weights);

            // Test vertex and edge counts
            assert g.getVertexCount() == 4 : "Vertex count should be 4";
            assert g.getEdgeCount() == 2 : "Edge count should be 2";

            // Test MST
            List<String> mst = g.getMST();
            assert mst.size() == 2 : "MST should have 2 elements (1 edge)";

            // Test shortest paths
            Map<String, Integer> sp = g.getShortestPaths("A");
            assert sp.get("A") == 0 : "Shortest path A to A should be 0";
            assert sp.get("B") == 1 : "Shortest path A to B should be 1";
            assert sp.get("C") == Integer.MAX_VALUE : "Shortest path A to C should be infinity";
            assert sp.get("D") == Integer.MAX_VALUE : "Shortest path A to D should be infinity";

            System.out.println("Disconnected Graph Test Passed");
        } catch (Exception e) {
            System.err.println("Disconnected Graph Test Failed: " + e.getMessage());
        }
    }

    private static void testSingleVertexGraph() {
        System.out.println("\nTest Case 4: Single Vertex Graph");
        Graph g = new GraphImpl();
        try {
            // Create a file with a single vertex
            String filename = "single_vertex_graph.txt";
            try (java.io.PrintWriter out = new java.io.PrintWriter(filename)) {
                out.println("1");  // Number of vertices
                out.println("A A 0");  // Single vertex with a self-loop of weight 0
            }

            g.load(filename);

            // Test vertex and edge counts
            assert g.getVertexCount() == 1 : "Vertex count should be 1";
            assert g.getEdgeCount() == 0 : "Edge count should be 0";

            // Get the name of the single vertex
            String vertexName = g.getVertices().iterator().next();

            // Test vertex existence
            assert g.hasVertex(vertexName) : "Vertex '" + vertexName + "' should exist";

            // Test MST
            List<String> mst = g.getMST();
            assert mst.isEmpty() : "MST should be empty for a single vertex";

            // Test shortest paths
            Map<String, Integer> sp = g.getShortestPaths(vertexName);
            assert sp.size() == 1 : "Shortest paths map should have 1 entry";
            assert sp.get(vertexName) == 0 : "Shortest path from " + vertexName + " to itself should be 0";

            System.out.println("Single Vertex Graph Test Passed");
        } catch (Exception e) {
            System.err.println("Single Vertex Graph Test Failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up the temporary file
            new java.io.File("single_vertex_graph.txt").delete();
        }
    }

    private static void testLargeGraph() {
        System.out.println("\nTest Case 5: Large Graph");
        Graph g = new GraphImpl();
        try {
            // Generate a large graph
            int n = 1000; // number of vertices
            List<String> edges = new ArrayList<>();
            List<Integer> weights = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    edges.add(String.valueOf(i));
                    edges.add(String.valueOf(j));
                    weights.add((i + j) % 100); // some arbitrary weight
                }
            }
            g.load(edges, weights);

            // Test vertex and edge counts
            assert g.getVertexCount() == n : "Vertex count should be " + n;
            assert g.getEdgeCount() == (n * (n - 1)) / 2 : "Edge count should be " + ((n * (n - 1)) / 2);

            // Test MST (just check if it returns without error and has correct size)
            List<String> mst = g.getMST();
            assert mst.size() == 2 * (n - 1) : "MST should have " + (2 * (n - 1)) + " elements";

            // Test shortest paths (just check if it returns without error)
            Map<String, Integer> sp = g.getShortestPaths("0");
            assert sp.size() == n : "Shortest paths map should have " + n + " entries";

            System.out.println("Large Graph Test Passed");
        } catch (Exception e) {
            System.err.println("Large Graph Test Failed: " + e.getMessage());
        }
    }
}