package org.natalya_me.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGraph {

    private Graph<Object> graph;

    @BeforeEach
    void initializeGraph() {
        graph = new Graph<>();
    }

    @Test
    void testAddNodeWhenIdNull() {
        assertThrowsExactly(IllegalArgumentException.class, () -> graph.addNode(null));
    }

    @Test
    void testAddNodeWhenIdNotNull() {
        graph.addNode("1");
        assertEquals(1, graph.size());
        assertTrue(graph.isLeaf("1"));
    }

    @Test
    void testAddNodeWhenSameNodeAddedTwice() {
        graph.addNode("1");
        assertEquals(1, graph.size());
        graph.addNode("1");
        assertEquals(1, graph.size());
        assertEquals(1, graph.getLeafNodes().size());
        assertTrue(graph.isLeaf("1"));
    }

    @Test
    void testAddNodeWhenMultipleNodesAdded() {
        graph.addNode("1");
        assertEquals(1, graph.size());
        graph.addNode("2");
        assertEquals(2, graph.size());
        assertEquals(2, graph.getLeafNodes().size());
        assertTrue(graph.isLeaf("1"));
        assertTrue(graph.isLeaf("2"));
    }

    @Test
    void testAddOrUpdateNodeWhenNullData() {
        assertDoesNotThrow(() -> graph.addOrUpdateNode("1", null));
        assertEquals(1, graph.size());
        assertTrue(graph.isLeaf("1"));
    }

    @Test
    void testAddOrUpdateNodeWhenNotNullData() {
        graph.addOrUpdateNode("1", "data");
        assertEquals(1, graph.size());
        assertEquals("data", graph.getData("1"));
    }

    @Test
    void testAddOrUpdateNodeWhenNodeUpdated() {
        graph.addOrUpdateNode("1", null);
        assertEquals(1, graph.size());
        graph.addOrUpdateNode("1", "data");
        assertEquals(1, graph.size());
        assertEquals("data", graph.getData("1"));
        assertEquals(1, graph.getLeafNodes().size());
        assertTrue(graph.isLeaf("1"));
    }

    @Test
    void testAddArcWhenFirstIdNull() {
        assertThrowsExactly(IllegalArgumentException.class, () -> graph.addArc(null, "2"));
        assertFalse(graph.arcExists(null, "2"));
        assertTrue(graph.isEmpty());
        assertTrue(graph.getLeafNodes().isEmpty());
    }
    @Test
    void testAddArcWhenSecondIdNull() {
        assertThrowsExactly(IllegalArgumentException.class, () -> graph.addArc("1", null));
        assertFalse(graph.arcExists("1", null));
        assertTrue(graph.isEmpty());
        assertTrue(graph.getLeafNodes().isEmpty());
    }

    @Test
    void testAddArcWhenNodesDoNotExist() {
        graph.addArc("1", "2");
        assertEquals(2, graph.size());
        assertTrue(graph.arcExists("1", "2"));
        assertFalse(graph.isLeaf("1"));
        assertTrue(graph.isLeaf("2"));
    }

    @Test
    void testAddArcWhenOneNodeExists() {
        graph.addNode("1");
        assertEquals(1, graph.getLeafNodes().size());
        graph.addArc("1", "2");
        assertEquals(2, graph.size());
        assertTrue(graph.arcExists("1", "2"));
        assertFalse(graph.isLeaf("1"));
        assertTrue(graph.isLeaf("2"));
    }

    @Test
    void testAddArcWhenBothNodesExist() {
        graph.addNode("1");
        graph.addOrUpdateNode("2", "data2");
        graph.addArc("1", "2");
        assertEquals(2, graph.size());
        assertTrue(graph.arcExists("1", "2"));
        assertEquals("data2", graph.getData("2"));
        assertFalse(graph.isLeaf("1"));
        assertTrue(graph.isLeaf("2"));
    }

    @Test
    void testAddArcWhenSameArcAddedTwice() {
        assertTrue(graph.addArc("1", "2"));
        assertFalse(graph.addArc("1", "2"));
        assertTrue(graph.arcExists("1", "2"));
        assertEquals(2, graph.size());
        assertFalse(graph.isLeaf("1"));
        assertTrue(graph.isLeaf("2"));
    }

    @Test
    void testAddArcWhenLoopArc() {
        assertThrowsExactly(IllegalArgumentException.class, () -> graph.addArc("1", "1"));
        assertTrue(graph.isEmpty());
    }

    @Test
    void testAddArcWhenCycle() {
        assertTrue(graph.addArc("1", "2"));
        assertFalse(graph.isLeaf("1"));
        assertTrue(graph.isLeaf("2"));
        assertTrue(graph.addArc("2", "3"));
        assertFalse(graph.isLeaf("1"));
        assertFalse(graph.isLeaf("2"));
        assertTrue(graph.isLeaf("3"));
        assertThrowsExactly(IllegalArgumentException.class, () -> graph.addArc("3", "1"));
        assertFalse(graph.arcExists("3", "1"));
        assertFalse(graph.isLeaf("1"));
        assertFalse(graph.isLeaf("2"));
        assertTrue(graph.isLeaf("3"));
    }

    @Test
    void testAddArcWhenMultipleTargetNodes() {
        assertTrue(graph.addArc("1", "2"));
        assertFalse(graph.addArc("1", "3"));
        assertFalse(graph.contains("3"));
        assertTrue(graph.arcExists("1", "2"));
        assertFalse(graph.arcExists("1", "3"));
    }
}
