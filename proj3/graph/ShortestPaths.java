package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.List;

/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author YunjieZhang
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        _path = new ArrayList<>();
        TreeSet<Integer> fringe = new TreeSet<>();
        TreeSet<Integer> marked = new TreeSet<>();
        setPredecessor(_source, 0);
        setWeight(_source, 0);
        fringe.add(_source);
        while (!fringe.isEmpty()) {
            Double bestWeightSoFar = Double.POSITIVE_INFINITY;
            int bestVertexSoFar = 0;
            for (int vertex:fringe) {
                double value = estimatedDistance(vertex) + getWeight(vertex);
                if (bestWeightSoFar > value) {
                    bestVertexSoFar = vertex;
                    bestWeightSoFar = value;
                }
            }
            fringe.remove(bestVertexSoFar);
            marked.add(bestVertexSoFar);
            if (bestVertexSoFar != _dest) {
                Iteration<Integer> successors = _G.successors(bestVertexSoFar);
                while (successors.hasNext()) {
                    int suc = successors.next();
                    if (!marked.contains(suc)) {
                        fringe.add(suc);
                    }
                    double value = getWeight(bestVertexSoFar, suc)
                            + getWeight(bestVertexSoFar);
                    if (value < getWeight(suc)) {
                        setWeight(suc, value);
                        setPredecessor(suc, bestVertexSoFar);
                    }
                }
            } else {
                break;
            }
        }
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        if (v == 0) {
            throw new IllegalArgumentException();
        }
        if (v != _dest && _dest != 0) {
            throw new IllegalStateException();
        }
        _path.add(_source);
        int curVertex = v;
        while (getPredecessor(curVertex) != 0) {
            _path.add(1, curVertex);
            curVertex = getPredecessor(curVertex);
        }
        return _path;
    }
    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }


    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    /** The list of path. */
    private ArrayList<Integer> _path;

}

