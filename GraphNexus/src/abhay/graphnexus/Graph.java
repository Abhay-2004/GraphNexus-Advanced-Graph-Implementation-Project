package Abhay.GraphNexus;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Abhay Prasanna Rao
 * 
 * An interface for implementing various operations on connected weighted undirected graphs.
 * Vertices are identified by Strings. Equal strings <=> same vertex.
 * Weights are non-negative integers.
 * Note that some documentation may use an overloaded definition for [i], such as [i] for List.get(i).
 */
public interface Graph {

    /**
     * Initializes the object from a text file described in the assignment document.
     * Do not make arbitrary assumptions about the location of the file relative to your machine.
     * @param pathToFile A string containing the path to the graph file.
     * @throws Exception Some possible reasons for an exception: file not found, malformed input, negative edge weights.
     */
    public void load(String pathToFile) throws Exception;

    /**
     * Initializes the object from the input lists. Input should satisfy edges.size() == 2 * weights.size().
     * @param edges For an even number index i, (i,i+1) represents an edge with vertices edges[i] and edges[i+1].
     * @param weights The element weights[i] is the weight of the edge (edges[i*2], edges[i*2+1]).
     * @throws Exception Malformed inputs, negative edge weights, etc.
     */
    public void load(List<String> edges, List<Integer> weights) throws Exception;

    /**
     *
     * @return |V|
     */
    public int getVertexCount();

    /**
     *
     * @param v The name of a vertex.
     * @return True if vertex v is present in the graph, false otherwise.
     */
    public boolean hasVertex(String v);

    /**
     * Get every vertex in the graph.
     * @return An object that is iterable over every vertex in the graph.
     */
    public Iterable<String> getVertices();

    /**
     *
     * @return |E|
     */
    public int getEdgeCount();

    /**
     *
     * @param u One endpoint of the edge.
     * @param v The other endpoint of the edge.
     * @return True if edge (u,v) is present in the graph, false otherwise.
     */
    public boolean hasEdge(String u, String v);

    /**
     * Return the weight for the given edge.
     * @param u An endpoint of the edge.
     * @param v The other endpoint of the edge.
     * @return The weight of edge (u,v) or -1 if the edge does not exist.
     */
    public int getWeight(String u, String v);

    /**
     * Returns all neighbors of vertex u.
     * @param u A vertex.
     * @return The neighbors of u.
     */
    public Iterable<String> getAdjacent(String u);

    /**
     * Gets an MST of the graph represented by this object.
     * @return A list of edges in an MST for this graph. For every even index i, ([i], [i+1]) represents an edge.
     */
    public List<String> getMST();

    /**
     * Finds the length of the shortest path from vertex s to all other vertices in the graph.
     * @param s The source vertex.
     * @return A map of shortest path distances. For key value pair (k,v), v is the length of the shortest s->k path or -1 if no such path exists.
     */
    public Map<String, Integer> getShortestPaths(String s);

    /**
     * A vertex v is in the return set iff the given subgraph contains a shortest path from s to v.
     * @param s A source vertex in the subgraph.
     * @param subgraph Edges representing a connected subgraph of the graph held by invoked object.
     * @return All vertices in the underlying object's graph with shortest paths present in the given subgraph.
     *          Returns null for invalid arguments.
     */
    public Set<String> getReport(String s, List<String> subgraph);
}
