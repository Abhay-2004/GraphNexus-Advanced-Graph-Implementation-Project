package abhay.graphnexus;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphMain {
    public static void main(String[] args) {
        String[] testFiles = {
            "complex_graph.txt", "g9.txt", "g10.txt", "g11.txt",
            "g2.txt", "g3.txt", "g4.txt", "g5.txt", "g6.txt", "g1.txt"
        };

        for (String fileName : testFiles) {
            System.out.println("\n\nTesting file: " + fileName);
            System.out.println("=".repeat(50));

            try {
                Graph g = new GraphImpl();
                g.load(fileName);

                System.out.println("Graph Analysis");
                System.out.println("==========================");

                // Get the first vertex to use as the starting point
                String startVertex = g.getVertices().iterator().next();

                // Primary outputs
                System.out.println("1. Shortest paths from '" + startVertex + "':");
                Map<String, Integer> sp = g.getShortestPaths(startVertex);
                System.out.println(sp);

                System.out.println("\n2. Minimum Spanning Tree:");
                List<String> mst = g.getMST();
                System.out.println(mst);

                System.out.println("\n3. Report for subgraph (MST) starting from '" + startVertex + "':");
                Set<String> report = g.getReport(startVertex, mst);
                System.out.println(report);

                // Separator
                System.out.println("\n" + ".".repeat(50));

                // Additional outputs
                System.out.println("\nAdditional Graph Information:");
                System.out.println("----------------------------");
                System.out.println("Vertex Count: " + g.getVertexCount());
                System.out.println("Edge Count: " + g.getEdgeCount());
                System.out.println("Has vertex '" + startVertex + "': " + g.hasVertex(startVertex));

                String secondVertex = g.getAdjacent(startVertex).iterator().next();
                System.out.println("Has edge (" + startVertex + "," + secondVertex + "): " + g.hasEdge(startVertex, secondVertex));
                System.out.println("Weight of edge (" + startVertex + "," + secondVertex + "): " + g.getWeight(startVertex, secondVertex));

                System.out.println("\nAdjacent vertices to '" + startVertex + "':");
                for (String v : g.getAdjacent(startVertex)) {
                    System.out.println("- " + v);
                }

                System.out.println("\nDetailed Minimum Spanning Tree:");
                for (int i = 0; i < mst.size(); i += 2) {
                    System.out.println("- " + mst.get(i) + " - " + mst.get(i + 1));
                }

                System.out.println("\nDetailed Shortest Paths from '" + startVertex + "':");
                for (Map.Entry<String, Integer> entry : sp.entrySet()) {
                    System.out.println("- " + entry.getKey() + ": " + entry.getValue());
                }

            } catch (Exception e) {
                System.err.println("An error occurred while processing " + fileName + ":");
                e.printStackTrace();
            }
        }
    }
}