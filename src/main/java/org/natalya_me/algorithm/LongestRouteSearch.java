package org.natalya_me.algorithm;

import org.natalya_me.util.ImmutablePair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс, содержащий алгоритм поиска самого длинного маршрута в графе.
 * Если в графе содержится несколько маршрутов одинаковой длины, то возвращается первый найденный.
 */
public class LongestRouteSearch {

    private LongestRouteSearch() {
        throw new UnsupportedOperationException(String.format("Инстанцирование класса %s запрещено.", LongestRouteSearch.class.getName()));
    }

    /**
     * Находит самый длинный маршрут в графе.
     * Граф обходится в обратном порядке начиная с висячих вершин. Во избежание применения рекурсии и повторного посещения
     * вершин используются вспомогательные структуры данных - стек для обхода графа в ширину и словарь вычисленных длин
     * маршрутов для посещенных вершин.
     *
     * @param graph   экземпляр графа
     * @return        список пар (id, data) для вершин найденного маршрута в правильном порядке, либо пустой список, если граф пуст
     * @param <T>     тип данных, содержащихся в вершине
     */
    public static <T> List<ImmutablePair<String, T>> findLongestRoute(Graph<T> graph) {
        if (graph.isEmpty()) return Collections.emptyList();

        // длина текущего самого длинного найденного маршрута
        int maxLength = 0;
        // ссылка на исходную вершину самого длинного маршрута
        Graph.Node<T> longestRouteHead = null;

        // стек вершин для посещения
        Deque<Graph.Node<T>> stack = new ArrayDeque<>();
        // длины маршрутов, исходящих из посещенных вершин
        Map<String, Integer> lengths = new HashMap<>();

        for (Graph.Node<T> leaf: graph.getLeafNodes()) {
            Graph.Node<T> current = leaf;
            lengths.put(current.getId(), 1);
            stack.addAll(leaf.getPreviousSet());
            while (!stack.isEmpty()) {
                current = stack.pop();
                int length = lengths.get(current.getNext().getId()) + 1;
                lengths.put(current.getId(), length);
                // текущая вершина является головой некоторого подграфа
                if (current.getPreviousSet().isEmpty() && length > maxLength) {
                    maxLength = length;
                    longestRouteHead = current;
                } else {
                    stack.addAll(current.getPreviousSet());
                }
            }

        }
        return fillResultList(longestRouteHead);
    }

    private static <T> List<ImmutablePair<String, T>> fillResultList(Graph.Node<T> head) {
        List<ImmutablePair<String, T>> result = new ArrayList<>();
        while (head != null) {
            result.add(new ImmutablePair<>(head.getId(), head.getData()));
            head = head.getNext();
        }
        return result;
    }
}
