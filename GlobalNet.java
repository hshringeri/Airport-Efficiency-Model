import java.util.ArrayList;

public class GlobalNet
{
    //creates a global network
    //O : the original graph
    //regions: the regional graphs
    public static Graph run(Graph O, Graph[] regions)
    {
        Graph global = new Graph(O.V());
        global.setCodes(O.getCodes());
        for (int i = 0; i < regions.length; i++) {
            ArrayList<Edge> edges = regions[i].edges();
            for (int j = 0; j < edges.size(); j++) {
                if (edges.get(j) != null) {
                    Edge e = new Edge(edges.get(j).u, edges.get(j).v, edges.get(j).w);
                    global.addEdge(e);
                }
            }
            ArrayList<Edge>[] pathList = pathList(O, regions[i], regions);
            for (int m = 0; m < pathList.length; m++) {
                if (pathList[m] == null) {
                    continue;
                } else {
                    for (int n = 0; n < pathList[m].size(); n++) {
                        Edge e = pathList[m].get(n);
                        if (e!= null && global.getEdge(O.index(e.u), O.index(e.v)) == null) {
                            global.addEdge(e);
                        }
                    }
                }
            }
        }

        return global.connGraph();
    }

    public static int[][] dijkstrasShortestPath(Graph G, Graph region) {
        int dist[] = new int[G.V()];
        int prev[] = new int[G.V()];
        DistQueue Q = new DistQueue(G.V());

        String codeList[] = G.getCodes();
        for (String code : codeList) {
            if (region.index(code) != -1) {
                dist[G.index(code)] = 0;
            } else {
                dist[G.index(code)] = Integer.MAX_VALUE;
            }
            prev[G.index(code)] = -1;
            Q.insert(G.index(code), dist[G.index(code)]);
        }

        while (!(Q.isEmpty())) {
            int min = Q.delMin();
            for (int v : G.adj(min)) {
                if (Q.inQueue(v)) {
                    int alt = dist[min] + G.getEdge(min,v).w;
                    if (alt < dist[v]) {
                        dist[v] = alt;
                        prev[v] = min;
                        Q.set(v,alt);
                    }
                } else {
                    continue;
                }
            }
        }
        int[][] returnVal = new int[2][];
        returnVal[0] = dist;
        returnVal[1] = prev;
        return returnVal;
    }

    private static ArrayList<Edge> regionalPaths(Graph G, Graph start, Graph end, int dist[][]) {
        if (start != end) {
            String[] codes = end.getCodes();
            int d = Integer.MAX_VALUE;
            int lastV = -1;
            for (int i = 0; i < codes.length; i++) {
                if (dist[0][G.index(codes[i])] < d) {
                    d = dist[0][G.index(codes[i])];
                    lastV = G.index(codes[i]);
                }
            }
            ArrayList<Edge> pathList = new ArrayList<Edge>();
            int vertexOne = dist[1][lastV];
            int vertexTwo = vertexOne;
            while (vertexOne != -1) {
                pathList.add(0, G.getEdge(vertexOne, vertexTwo));
                vertexTwo = vertexOne;
                vertexOne = dist[1][vertexOne];
            }
            return pathList;
        }
        return null;
    }

    private static ArrayList<Edge>[] pathList(Graph G, Graph Region, Graph[] regions) {
        int[][] dL = dijkstrasShortestPath(G,Region);
        ArrayList<Edge>[] pathList = new ArrayList[regions.length];
        for (int k = 0; k < regions.length; k++) {
            pathList[k] = regionalPaths(G, Region, regions[k], dL);
        }
        return pathList;
    }
}


