	import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RegNet {
    private static int[][] stops;

    //creates a regional network
    //G: the original graph
    //max: the budget
    public static Graph run(Graph G, int max) {
        Graph MST = kruskalMST(G);
        stops = new int[MST.V()][MST.V()];
        ArrayList<int[]> stopsList = new ArrayList<int[]>();

        //MST.setStopsList(stops);
        int mstTotalWeight = MST.totalWeight();
        while (MST.totalWeight() > max) {
            ArrayList<Edge> sort = MST.sortedEdges();

            Edge e = MST.sortedEdges().get(MST.sortedEdges().size() - 1);
            String u = e.u;
            String v = e.v;

            MST.sortedEdges().remove(e);
            if (MST.deg(u) >= 1 || MST.deg(v) >= 1) {
                ArrayList<Edge> sort2 = MST.sortedEdges();
                MST.removeEdge(e);
            }
        }
        String[] codes = MST.getCodes();
        for (String s : codes) {
            BFS(MST, s);
            /*for (int i = 0; i < MST.V(); i++) {
                for (int j = 0; j < MST.V(); j++) {
                    if (stops[i][j] > 0) {
                        stopsList.add(new int[]{i, j, stops[i][j]});
                    }
                }
           */
        }
        for (int i = 0; i < MST.V(); i++) {
            for (int j = 0; j < MST.V(); j++) {
                System.out.print(stops[i][j] + " ");
                if (stops[i][j] > 0) {

                    stopsList.add(new int[]{i, j, stops[i][j]});
                }

            }
            System.out.println();
        }

        bubbleSort(stopsList);
        distanceSort(stopsList,G);
        int totalWeight = MST.totalWeight();
        System.out.println(MST.toString());
        System.out.println(MST.totalWeight());
        while ((totalWeight <= max) && stopsList.size() > 0) {
            Edge e = G.getEdge(stopsList.get(0)[0],stopsList.get(0)[1]);
            if (!MST.edges().contains(e)) {
                if (totalWeight + e.w <= max) {
                    MST.addEdge(e);
                    totalWeight += e.w;

                }

            }
            stopsList.remove(0);

        }



        return MST.connGraph();

        //To be implemented
    }

    public static Graph kruskalMST(Graph G) {
        ArrayList<Edge> sortedEdges = G.sortedEdges();
        Graph MST = new Graph(G.V());
        MST.setCodes(G.getCodes());

        UnionFind uf = new UnionFind(G.V());

        while (MST.E() < G.V() - 1) {
            Edge e = sortedEdges.get(0);
            sortedEdges.remove(0);
            if (uf.find(e.ui()) != uf.find(e.vi())) {
                MST.addEdge(e);
                uf.union(e.ui(),e.vi());
            }
        }
        return MST;
    }

    public static void BFS(Graph G, String s) {
        ArrayList<Integer> vertices = new ArrayList<Integer>();
        int EdgeTo[] = new int[G.V()];
        for (int i = 0; i < EdgeTo.length; i++) {
            EdgeTo[i] = -1;
        }
        boolean marked[] = new boolean[G.V()];
        int vx = G.index(s);
        vertices.add(vx);

        while (!(vertices.isEmpty())) {
            int v = vertices.remove(vertices.size() - 1);
            marked[v] = true;
            for (int i : G.adj(v)) {
                if (marked[i] == false) {
                    vertices.add(i);
                    marked[i] = true;
                    EdgeTo[i] = v;
                }
            }
        }
        //stops = G.getStopsList();




        for(int i = 0; i < EdgeTo.length; i ++) {
            if (i != G.index(s)) {
                stops[G.index(s)][i] = getStops(EdgeTo, i);
            } else {
                stops[G.index(s)][i] = 0;
            }

        }





    }

    public static int getStops(int[] arr, int v) {
        int stops = 0;
        int traceNo = arr[v];
        while (traceNo != -1) {
            traceNo = arr[traceNo];
            stops++;
        }
        return stops -1;
    }

    public static void bubbleSort(ArrayList<int[]> arr) {
        int n = arr.size();
        for (int i = 0; i <n-1; i++) {
            for (int j = 1; j < n-i-1; j++) {
                if (arr.get(j)[2]> arr.get(j-1)[2]) {
                    int temp[] = arr.get(j-1);
                    arr.set(j-1, arr.get(j));
                    arr.set(j,temp);
                }
            }
        }

    }

    public static void distanceSort(ArrayList<int[]> arr, Graph G) {
        int n = arr.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (arr.get(j)[2] == arr.get(j+1)[2]) {
                    if (G.getEdge(arr.get(j)[0],arr.get(j)[1]).w > G.getEdge(arr.get(j+1)[0],arr.get(j+1)[1]).w) {
                        int temp[] = arr.get(j);
                        arr.set(j,arr.get(j+1));
                        arr.set(j+1,temp);
                    }
                }
            }
        }

    }


}