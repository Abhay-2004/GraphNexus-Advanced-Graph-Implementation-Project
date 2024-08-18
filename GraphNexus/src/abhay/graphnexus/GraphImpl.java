package abhay.graphnexus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
/**
 * Implementation of the Graph interface representing a weighted undirected graph.
 * This class provides methods for loading, querying, and analyzing graph structures.
 *
 * @author Abhay Prasanna Rao
 */
public class GraphImpl implements Graph {
    private Map<String, Map<String, Integer>> adjacencyMap;
    /**
     * Constructs an empty graph.
     */
    public GraphImpl() {
        adjacencyMap = new HashMap<>();
    }
    /**
     * Loads a graph from a file.
     *
     * @param pathToFile The path to the file containing the graph data.
     * @throws Exception If there's an error reading the file or if the file format is invalid.
     */

    @Override
    public void load(String pathToFile) throws Exception {
        adjacencyMap.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            int vertexCount = Integer.parseInt(reader.readLine().trim());
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length != 3) {
                    throw new Exception("Invalid input format");
                }
                String u = parts[0], v = parts[1];
                int weight = Integer.parseInt(parts[2]);
                if (weight < 0) {
                    throw new Exception("Negative edge weight not allowed");
                }
                // Only add edge if it's not a self-loop with zero weight
                if (!u.equals(v) || weight != 0) {
                    addEdge(u, v, weight);
                } else {
                    // For a self-loop with zero weight, just add the vertex
                    adjacencyMap.putIfAbsent(u, new HashMap<>());
                }
            }
            if (adjacencyMap.size() != vertexCount) {
                throw new Exception("Mismatch between declared vertex count and actual vertex count");
            }
        }
    }
    /**
     * Loads a graph from lists of edges and weights.
     *
     * @param edges A list of strings representing the edges. Each pair of consecutive strings represents an edge.
     * @param weights A list of integers representing the weights of the edges.
     * @throws Exception If there's a mismatch between edges and weights or if any weight is negative.
     */

    @Override
    public void load(List<String> edges, List<Integer> weights) throws Exception {
        adjacencyMap.clear();
        if (edges.size() != 2 * weights.size()) {
            throw new Exception("Mismatch between edges and weights lists");
        }
        for (int i = 0; i < weights.size(); i++) {
            String u = edges.get(2 * i);
            String v = edges.get(2 * i + 1);
            int weight = weights.get(i);
            if (weight < 0) {
                throw new Exception("Negative edge weight not allowed");
            }
            addEdge(u, v, weight);
        }
    }
    /**
     * Adds an edge to the graph.
     *
     * @param u One endpoint of the edge.
     * @param v The other endpoint of the edge.
     * @param weight The weight of the edge.
     */

    private void addEdge(String u, String v, int weight) {
        adjacencyMap.computeIfAbsent(u, k -> new HashMap<>()).put(v, weight);
        adjacencyMap.computeIfAbsent(v, k -> new HashMap<>()).put(u, weight);
    }
    /**
     * Returns the number of vertices in the graph.
     *
     * @return The number of vertices.
     */
    @Override
    public int getVertexCount() {
        return adjacencyMap.size();
    }
    /**
     * Checks if a vertex exists in the graph.
     *
     * @param v The vertex to check.
     * @return true if the vertex exists, false otherwise.
     */
    @Override
    public boolean hasVertex(String v) {
        return adjacencyMap.containsKey(v);
    }
    /**
     * Returns an iterable of all vertices in the graph.
     *
     * @return An iterable of all vertices.
     */
    @Override
    public Iterable<String> getVertices() {
        return adjacencyMap.keySet();
    }
    /**
     * Returns the number of edges in the graph.
     *
     * @return The number of edges.
     */
    @Override
    public int getEdgeCount() {
        int count = 0;
        for (Map<String, Integer> edges : adjacencyMap.values()) {
            count += edges.size();
        }
        return count / 2; // Each edge is counted twice
    }
    /**
     * Checks if an edge exists between two vertices.
     *
     * @param u One endpoint of the edge.
     * @param v The other endpoint of the edge.
     * @return true if the edge exists, false otherwise.
     */
    @Override
    public boolean hasEdge(String u, String v) {
        return adjacencyMap.containsKey(u) && adjacencyMap.get(u).containsKey(v);
    }
    
    
    /**
     * Returns the weight of an edge.
     *
     * @param u One endpoint of the edge.
     * @param v The other endpoint of the edge.
     * @return The weight of the edge, or -1 if the edge doesn't exist.
     */
    @Override
    public int getWeight(String u, String v) {
        if (hasEdge(u, v)) {
            return adjacencyMap.get(u).get(v);
        }
        return -1;
    }
    
    
    /**
     * Returns an iterable of vertices adjacent to a given vertex.
     *
     * @param u The vertex whose adjacent vertices are to be returned.
     * @return An iterable of adjacent vertices.
     */
    @Override
    public Iterable<String> getAdjacent(String u) {
        if (!adjacencyMap.containsKey(u)) {
            return Collections.emptyList();
        }
        return adjacencyMap.get(u).keySet();
    }
    
    /**
     * Computes a Minimum Spanning Tree (MST) of the graph using Prim's algorithm.
     *
     * @return A list of strings representing the edges in the MST.
     */
    @Override
    public List<String> getMST() {
        List<String> result = new ArrayList<>();
        if (adjacencyMap.isEmpty()) {
            return result;  // Return empty list for empty graph
        }
        
        if (adjacencyMap.size() == 1) {
            result.add(adjacencyMap.keySet().iterator().next());
            return result;  // Return the single vertex for single-vertex graphs
        }
        
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        String start = adjacencyMap.keySet().iterator().next();
        visited.add(start);

        for (Map.Entry<String, Integer> entry : adjacencyMap.get(start).entrySet()) {
            pq.offer(new Edge(start, entry.getKey(), entry.getValue()));
        }

        while (!pq.isEmpty() && visited.size() < adjacencyMap.size()) {
            Edge edge = pq.poll();
            if (visited.contains(edge.to)) continue;

            visited.add(edge.to);
            result.add(edge.from);
            result.add(edge.to);

            for (Map.Entry<String, Integer> entry : adjacencyMap.get(edge.to).entrySet()) {
                if (!visited.contains(entry.getKey())) {
                    pq.offer(new Edge(edge.to, entry.getKey(), entry.getValue()));
                }
            }
        }

        return result;
    }
    
    /**
     * Computes the shortest paths from a source vertex to all other vertices using Dijkstra's algorithm.
     *
     * @param s The source vertex.
     * @return A map of vertices to their shortest distance from the source.
     */

    @Override
    public Map<String, Integer> getShortestPaths(String s) {
        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (String v : adjacencyMap.keySet()) {
            distances.put(v, v.equals(s) ? 0 : Integer.MAX_VALUE);
        }

        pq.offer(new Node(s, 0));

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            String u = node.vertex;

            if (node.distance > distances.get(u)) continue;

            for (Map.Entry<String, Integer> entry : adjacencyMap.get(u).entrySet()) {
                String v = entry.getKey();
                int weight = entry.getValue();
                int newDist = distances.get(u) + weight;

                if (newDist < distances.getOrDefault(v, Integer.MAX_VALUE)) {
                    distances.put(v, newDist);
                    pq.offer(new Node(v, newDist));
                }
            }
        }

        return distances;
    }

    /**
     * Computes a report of vertices in a subgraph that have the same shortest path length
     * from a source vertex as in the full graph.
     *
     * @param s The source vertex.
     * @param subgraph A list of strings representing the edges in the subgraph.
     * @return A set of vertices that have the same shortest path length in the subgraph as in the full graph.
     */
    @Override
    public Set<String> getReport(String s, List<String> subgraph) {
        if (!hasVertex(s)) {
            return null;
        }
        
        if (subgraph == null || subgraph.isEmpty()) {
            return Collections.singleton(s);
        }

        Map<String, Integer> shortestPaths = getShortestPaths(s);
        Map<String, Integer> subgraphDistances = new HashMap<>();
        Map<String, Set<String>> subgraphAdjList = new HashMap<>();

        // Build subgraph adjacency list
        for (int i = 0; i < subgraph.size(); i += 2) {
            String u = subgraph.get(i);
            String v = subgraph.get(i + 1);
            subgraphAdjList.computeIfAbsent(u, k -> new HashSet<>()).add(v);
            subgraphAdjList.computeIfAbsent(v, k -> new HashSet<>()).add(u);
        }

        // BFS to calculate distances in subgraph
        Queue<String> queue = new LinkedList<>();
        queue.offer(s);
        subgraphDistances.put(s, 0);

        while (!queue.isEmpty()) {
            String u = queue.poll();
            for (String v : subgraphAdjList.getOrDefault(u, Collections.emptySet())) {
                if (!subgraphDistances.containsKey(v)) {
                    subgraphDistances.put(v, subgraphDistances.get(u) + getWeight(u, v));
                    queue.offer(v);
                }
            }
        }

        // Compare subgraph distances with shortest paths
        Set<String> result = new HashSet<>();
        for (String v : subgraphDistances.keySet()) {
            if (subgraphDistances.get(v).equals(shortestPaths.get(v))) {
                result.add(v);
            }
        }

        return result;
    }

    
    /**
     * Represents an edge in the graph.
     */
    private static class Edge implements Comparable<Edge> {
        String from, to;
        int weight;

        Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }
    
    /**
     * Represents a node in the priority queue for Dijkstra's algorithm.
     */
    private static class Node implements Comparable<Node> {
        String vertex;
        int distance;

        Node(String vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}
