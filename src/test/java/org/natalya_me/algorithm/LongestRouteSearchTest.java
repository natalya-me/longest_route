package org.natalya_me.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.natalya_me.util.ImmutablePair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class LongestRouteSearchTest {

    private Graph<String> graph;

    @BeforeEach
    void initializeGraph() {
        graph = new Graph<>();
    }

    @Test
    void testFindLongestRouteWhenEmptyGraph() {
        assertEquals(Collections.emptyList(), LongestRouteSearch.findLongestRoute(graph));
    }

    @Test
    void testFindLongestRouteWhenSingleNode() {
        graph.addNode("1");
        List<ImmutablePair<String, String>> expected = Collections.singletonList(new ImmutablePair<>("1", null));
        List<ImmutablePair<String, String>> result = LongestRouteSearch.findLongestRoute(graph);
        assertEquals(expected, result);
    }

    @RepeatedTest(5)
    void testFindLongestRouteWhenMultipleNotConnectedNodes() {
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        List<ImmutablePair<String, String>> result = LongestRouteSearch.findLongestRoute(graph);
        assertEquals(1, result.size());
        assertTrue(Arrays.asList("1", "2", "3").contains(result.get(0).getKey()));
    }

    @Test
    void testFindLongestRouteWhenOneHeadOneConnectedComponent() {
        graph.addArc("1", "2");
        graph.addArc("2", "3");
        assertEquals(Arrays.asList("1", "2", "3"), longestRouteIds(graph));
    }

    @Test
    void testFindLongestRouteWhenMultipleHeadsOneConnectedComponent() {
        graph.addArc("1", "2");
        graph.addArc("2", "3");
        graph.addArc("4", "3");
        graph.addArc("3", "5");
        assertEquals(Arrays.asList("1", "2", "3", "5"), longestRouteIds(graph));
    }

    @RepeatedTest(5)
    void testFindLongestRouteWhenOneConnectedComponentMultipleRoutes() {
        graph.addArc("1", "2");
        graph.addArc("2", "3");
        graph.addArc("4", "3");
        graph.addArc("3", "5");
        graph.addArc("6", "4");
        List<String> result = longestRouteIds(graph);
        assertTrue(Arrays.asList("1", "2", "3", "5").equals(result) ||
                            Arrays.asList("6", "4", "3", "5").equals(result));
    }

    @Test
    void testFindLongestRouteWhenMultipleConnectedComponentsOneRoute() {
        graph.addArc("1", "2");
        graph.addArc("2", "5");
        graph.addArc("3", "4");
        assertEquals(Arrays.asList("1", "2", "5"), longestRouteIds(graph));
    }

    @Test
    void testFindLongestRouteWhenMultipleConnectedComponentsMultipleRoutes() {
        graph.addArc("1", "2");
        graph.addArc("3", "4");
        List<String> result = longestRouteIds(graph);
        assertTrue(Arrays.asList("1", "2").equals(result) ||
                Arrays.asList("3", "4").equals(result));
    }

    static List<String> longestRouteIds(Graph<?> graph) {
        return LongestRouteSearch.findLongestRoute(graph).stream()
                .map(ImmutablePair::getKey)
                .collect(Collectors.toList());
    }

}
