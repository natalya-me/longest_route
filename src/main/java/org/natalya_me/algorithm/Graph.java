package org.natalya_me.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Класс ориентированного ациклического графа специального вида, в котором исходящая степень любой вершины не превосходит 1.
 * Вершины графа содержат идентификатор и некоторые данные.
 * @param <T> тип данных, содержащихся в вершине графа.
 */
public final  class Graph<T> {

    private final Map<String, Node<T>> nodeCache = new HashMap<>();
    private final Set<Node<T>> leafNodes = new HashSet<>();

    /**
     * Добавляет вершину в граф с идентификатором id и данными data.
     * Если вершина с таким идентификатором уже содержится в графе, то для нее обновляется поле данных.
     *
     * @param id     идентификатор вершины
     * @param data   данные
     */
    public void addOrUpdateNode(String id, T data) {
        addOrFindNode(id).setData(data);
    }

    /**
     * Добавляет вершину в граф с идентификатором id. Если вершина с таким идентификатором существует, то новая вершина не создается.
     *
     * @param id идентификатор вершины
     */
    public void addNode(String id) {
        addOrFindNode(id);
    }

    /**
     * Устанавливает дугу между вершинами с указанными идентификаторами.
     * Если для вершины idFrom уже задана исходящая дуга, то новая дуга не устанавливается.
     * При попытке установить дугу, формирующую цикл в графе, возникает {@link IllegalArgumentException}.
     *
     * @param idFrom   идентификатор вершины, из которой исходит дуга
     * @param idTo     идентификатор вершины, в которую входит дуга
     * @return true, если удалось установить дугу; false, если дуга уже существует
     */
    public boolean addArc(String idFrom, String idTo) {
        Node<T> nodeFrom = addOrFindNode(idFrom);
        Node<T> nodeTo = addOrFindNode(idTo);
        if (nodeFrom.setNext(nodeTo)) {
            leafNodes.remove(nodeFrom);
            return true;
        }
        return false;
    }

    /**
     * Проверяет, пуст ли граф.
     *
     * @return true, если в графе нет ни одной вершины.
     */
    public boolean isEmpty() {
        return nodeCache.isEmpty();
    }

    Collection<Node<T>> getLeafNodes() {
        return new ArrayList<>(leafNodes);
    }

    private Node<T> addOrFindNode(String id) {
        return nodeCache.computeIfAbsent(id, key -> {
            Node<T> node = new Node<>(key);
            leafNodes.add(node);
            return node;
        });
    }

    /**
     * Класс вершины графа. Каждая вершина содержит ссылку на следующую вершину и множество ссылок на предыдущие вершины.
     * @param <T> тип данных, которые содержатся в вершине
     */
    static class Node<T> {

        private final String id;
        private T data;
        private Node<T> next;
        private Set<Node<T>> previousSet = Collections.emptySet();

        public Node(String id) {
            this.id = id;
        }

        public Node(String id, T data) {
            this.id = id;
            this.data = data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public String getId() {
            return id;
        }

        public T getData() {
            return data;
        }

        // Package private методы для алгоритмических целей

        Node<T> getNext() {
            return next;
        }

        Collection<Node<T>> getPreviousSet() {
            return previousSet;
        }

        /**
         * Установление дуги this -> other, а именно сохранение ссылки other как {@link #next} для текущей вершины
         * и добавление ссылки на текущую вершину в множестве {@link #previousSet} вершины other.
         * В процессе добавления выполняется проверка на возникновение цикла; повторное добавление или замена дуги невозможны.
         *
         * @param other   следующая вершина
         * @return         true, если дуга установлена успешно, иначе false
         */
        private boolean setNext(Node<T> other) {
            if (other == null || next != null) {
                return false;
            }
            if (makesCycle(other))  {
                throw new IllegalArgumentException(String.format("Добавление дуги %s -> %s приводит к появлению цикла в графе.", this.getId(), other.getId()));
            }
            if (other.addPrevious(this)) {
                this.next = other;
                return true;
            }
            return false;
        }

        /**
         * Добавление ссылки на предыдущую вершину при добавлении дуги other -> this.
         * Вызывается только внутри метода {@link #setNext(Node)}, вызов где-либо еще приведет к нарушению инварианта класса и состояния графа.
         *
         * @param other   предыдущая вершина
         * @return        true, если удалось добавить ссылку, иначе false.
         */
        private boolean addPrevious(Node<T> other) {
            if (other == null || previousSet.contains(other)) {
                return false;
            }
            if (previousSet.isEmpty()) {
                previousSet = new HashSet<>();
            }
            previousSet.add(other);
            return true;
        }

        /**
         * Проверяет, создает ли цикл в графе добавление дуги this -> next.
         *
         * @param next   следующая вершина
         * @return       true, если текущая вершина достижима из вершины next, иначе false
         */
        private boolean makesCycle(Node<T> next) {
            // первое сравнение - this и next, если добавляемая дуга является петлей
            Node<T> current = next;
            while (current != null) {
                if (current == this) {
                    return true;
                }
                current = current.next;
            }
            return false;
        }
    }
}
