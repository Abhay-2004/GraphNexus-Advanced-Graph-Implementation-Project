package abhay.graphnexus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllGraphTest {



    // //Replace these two lines to the correct file address
	String filePath = "g1.txt";
	
    String SuperlongfilePath = "e10k.txt";
    // //Replace these two lines to the correct file address


    public GraphImpl graph;

    @BeforeEach
    public void setup() throws Exception{
        graph = new GraphImpl();
    }

    @Test
    public void testLoadFromFile() throws Exception {
        graph.load(filePath);

        Assertions.assertEquals(4, graph.getVertexCount(), "Vertex count should match the number of unique vertices");
        Assertions.assertEquals(5, graph.getEdgeCount(), "Edge count should match the number of edges in the file");
    }

    @Test
    public void testVertexAndEdgeExistence() throws Exception {
        graph.load(List.of("a", "b", "b", "c", "c", "a"), List.of(1, 2, 3));
        Assertions.assertTrue(graph.hasVertex("a"));
        Assertions.assertTrue(graph.hasEdge("a", "b"));
        Assertions.assertFalse(graph.hasVertex("d"));
        Assertions.assertFalse(graph.hasEdge("a", "d"));
    }

    @Test
    public void testGetWeight() throws Exception {
        graph.load(List.of("a", "b", "b", "c", "c", "a"), List.of(1, 2, 3));
        Assertions.assertEquals(1, graph.getWeight("a", "b"));
        Assertions.assertEquals(-1, graph.getWeight("a", "d"));  // Edge does not exist
    }

    @Test
    public void testGetAdjacent() throws Exception {
        graph.load(List.of("a", "b", "b", "c", "c", "a"), List.of(1, 2, 3));
        List<String> expectedNeighbors = Arrays.asList("b", "c");
        Assertions.assertIterableEquals(expectedNeighbors, graph.getAdjacent("a"));
    }

    @Test
    public void testMST() throws Exception {
        graph.load(List.of("a", "b", "b", "c", "c", "d", "d", "a"), List.of(1, 2, 3, 4));
        List<String> mst = graph.getMST();
        // Assuming MST algorithm is correct and it includes all vertices
        Assertions.assertTrue(mst.size() >= 6);  // Should contain at least 3 edges, each represented by two vertices
    }

    @Test
    public void testShortestPaths() throws Exception {
        graph.load(List.of("a", "b", "b", "c", "c", "a"), List.of(1, 2, 3));
        Map<String, Integer> shortestPaths = graph.getShortestPaths("a");
        Assertions.assertEquals(Integer.valueOf(0), shortestPaths.get("a"));
        Assertions.assertEquals(Integer.valueOf(1), shortestPaths.get("b"));
        Assertions.assertEquals(Integer.valueOf(3), shortestPaths.get("c"));
    }

    @Test
    public void testGetReport() throws Exception {
        graph.load(List.of("a", "b", "b", "c", "c", "a"), List.of(1, 2, 3));
        List<String> subgraph = List.of("a", "b", "b", "c");  
        Set<String> report = graph.getReport("a", subgraph);
        Assertions.assertTrue(report.contains("a") && report.contains("b") && report.contains("c"));
    }


        @Test
    public void testVertexCount() throws Exception{
        graph.load(filePath);
        assertEquals(4, graph.getVertexCount());
    }

    @Test
    public void testEdgeCount()throws Exception {
        graph.load(filePath);

        assertEquals(5, graph.getEdgeCount());
    }

    @Test
    public void testHasVertex() throws Exception{
        graph.load(filePath);

        assertTrue(graph.hasVertex("a"));
        assertTrue(graph.hasVertex("b"));
        assertTrue(graph.hasVertex("c"));
        assertTrue(graph.hasVertex("d"));
        assertFalse(graph.hasVertex("e"));
    }

    @Test
    public void testHasEdge() throws Exception{
        graph.load(filePath);

        assertTrue(graph.hasEdge("a", "b"));
        assertTrue(graph.hasEdge("a", "d"));
        assertTrue(graph.hasEdge("b", "c"));
        assertTrue(graph.hasEdge("d", "c"));
        assertTrue(graph.hasEdge("a", "c"));
        assertFalse(graph.hasEdge("b", "d"));
    }

    @Test
    public void testsGetWeight()  throws Exception{
        graph.load(filePath);

        assertEquals(2, graph.getWeight("a", "b"));
        assertEquals(2, graph.getWeight("a", "d"));
        assertEquals(2, graph.getWeight("b", "c"));
        assertEquals(5, graph.getWeight("d", "c"));
        assertEquals(3, graph.getWeight("a", "c"));
        assertEquals(-1, graph.getWeight("b", "d"));
    }

    @Test
    public void testsGetAdjacent() throws Exception{
        graph.load(filePath);

        Set<String> expectedAdjA = new HashSet<>(Arrays.asList("b", "d", "c"));
        Set<String> expectedAdjB = new HashSet<>(Arrays.asList("a", "c"));
        Set<String> expectedAdjC = new HashSet<>(Arrays.asList("a", "b", "d"));
        Set<String> expectedAdjD = new HashSet<>(Arrays.asList("a", "c"));

        assertEquals(expectedAdjA, new HashSet<>((Collection<String>) graph.getAdjacent("a")));
        assertEquals(expectedAdjB, new HashSet<>((Collection<String>) graph.getAdjacent("b")));
        assertEquals(expectedAdjC, new HashSet<>((Collection<String>) graph.getAdjacent("c")));
        assertEquals(expectedAdjD, new HashSet<>((Collection<String>) graph.getAdjacent("d")));
    }

    @Test
    public void testGetShortestPaths() throws Exception{
        graph.load(filePath);

        Map<String, Integer> expected = Map.of(
                "a", 0,
                "b", 2,
                "d", 2,
                "c", 3
        );
        assertEquals(expected, graph.getShortestPaths("a"));
    }

    @Test
    public void testGetMST() throws Exception{
        graph.load(filePath);

        List<String> mst = graph.getMST();
        Set<String> edgesInMST = new HashSet<>();
        for (int i = 0; i < mst.size(); i += 2) {
            String u = mst.get(i);
            String v = mst.get(i + 1);
            edgesInMST.add(u + v);
            edgesInMST.add(v + u);
        }
        Set<String> expectedEdgesInMST = new HashSet<>(Arrays.asList("ab", "ad", "bc"));
        assertTrue(edgesInMST.containsAll(expectedEdgesInMST));
    }

    @Test
    public void testsGetReport() throws Exception{
        graph.load(filePath);

        List<String> mst = graph.getMST();
        Set<String> expectedReport = new HashSet<>(Arrays.asList("a", "b", "d"));
        assertEquals(expectedReport, graph.getReport("a", mst));
    }

    @Test
    public void testLoadLargeGraph() throws Exception {
        graph.load(SuperlongfilePath);
        Assertions.assertEquals(10001, graph.getVertexCount(), "Vertex count should be 10,001");
        Assertions.assertEquals(10000, graph.getEdgeCount(), "Edge count should be 10,000");
    }

    @Test
    public void testMSTOnLargeGraph() throws Exception {
        graph.load(SuperlongfilePath);
        List<String> mst = graph.getMST();
        Assertions.assertNotNull(mst, "MST should not be null");
        Assertions.assertEquals(20000, mst.size(), "MST should include 20,000 entries (10,000 edges each represented by two vertices)");
    }

    @Test
    public void testShortestPathsPerformanceAndCorrectness() throws Exception{
        graph.load(SuperlongfilePath);
        long startTime = System.currentTimeMillis();
        Map<String, Integer> shortestPaths = graph.getShortestPaths("0");
        long endTime = System.currentTimeMillis();

        Assertions.assertEquals(10000, shortestPaths.get("10000").intValue(), "The shortest path from 0 to 10000 should be 10,000");
        Assertions.assertTrue((endTime - startTime) < 5000, "Shortest path computation should be efficient");
    }

    @Test
    public void testReportFunctionality() throws Exception{
        graph.load(SuperlongfilePath);
        List<String> subgraph = List.of("0", "1", "1", "2", "2", "3", "3", "4");
        Set<String> report = graph.getReport("0", subgraph);
        
        Assertions.assertTrue(report.contains("0"), "Report should include the source vertex 0");
        Assertions.assertTrue(report.contains("1"), "Report should include vertex 1");
        Assertions.assertTrue(report.contains("2"), "Report should include vertex 2");
        Assertions.assertTrue(report.contains("3"), "Report should include vertex 3");
        Assertions.assertTrue(report.contains("4"), "Report should include vertex 4");
    }
    
}
