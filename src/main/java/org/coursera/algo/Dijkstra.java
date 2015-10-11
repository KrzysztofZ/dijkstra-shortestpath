package org.coursera.algo;

import java.io.*;
import java.util.*;
import java.util.zip.ZipFile;

class Vertex implements Comparable<Vertex> {
         public final String name;
         public Edge[] adjacencies;
         public double minDistance = Double.POSITIVE_INFINITY;
         public Vertex previous;
         public Vertex(String argName) { name = argName; }
         public String toString() { return name; }
         public int compareTo(Vertex other)
         {
             return Double.compare(minDistance, other.minDistance);
         }
    }


     class Edge  {
         public final Vertex target;
         public final double weight;
         public Edge(Vertex argTarget, double argWeight)
         { target = argTarget; weight = argWeight; }
     }

     public class Dijkstra {

         private static Vertex[] vertices;

         public static void computePaths(Vertex source)
         {
             source.minDistance = 0.;
            PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
            vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

                 // Visit each edge exiting u
                 for (Edge e : u.adjacencies)
                 {
                     Vertex v = e.target;
                     double weight = e.weight;
                     double distanceThroughU = u.minDistance + weight;
                     if (distanceThroughU < v.minDistance) {
                        vertexQueue.remove(v);

                        v.minDistance = distanceThroughU ;
                        v.previous = u;
                        vertexQueue.add(v);
                     }
                 }
             }
         }

         public static List<Vertex> getShortestPathTo(Vertex target)
         {
             List<Vertex> path = new ArrayList<Vertex>();
             for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
                 path.add(vertex);

             Collections.reverse(path);
             return path;
         }

         private static Vertex readGraph( String relPath ) throws FileNotFoundException {
             vertices = new Vertex[200];
             for(int i=0; i<200; i++) {
                 vertices[i] = new Vertex(String.valueOf(i+1));
             }
             FileReader fr;
             try {
                 fr = new FileReader( relPath );
                 BufferedReader br = new BufferedReader( fr );
                 String line;
                 while( ( line = br.readLine() ) != null ) {
                     String[] split = line.trim().split( "(\\s)+" );
                     List<Edge> adjList = new ArrayList<Edge>();
                     for(int i = 1; i < split.length; i++) {
                         Integer targetVertex = Integer.parseInt(split[i].substring(0,split[i].indexOf(",")));
                         Integer targetWeight = Integer.parseInt(split[i].substring(split[i].indexOf(",")+1));
                         Edge edge = new Edge(vertices[targetVertex-1], targetWeight);
                         adjList.add( edge );
                     }
                     vertices[Integer.parseInt(split[0])-1].adjacencies = adjList.toArray(new Edge[]{});
                 }
             } catch ( Exception e ) {
                 e.printStackTrace();
             }

             return vertices[0];
         }

         private static Vertex example1() {

             Vertex v0 = new Vertex("Harrisburg");
             Vertex v1 = new Vertex("Baltimore");
             Vertex v2 = new Vertex("Washington");
             Vertex v3 = new Vertex("Philadelphia");
             Vertex v4 = new Vertex("Binghamton");
             Vertex v5 = new Vertex("Allentown");
             Vertex v6 = new Vertex("New York");
             v0.adjacencies = new Edge[]{ new Edge(v1,  79.83),
                     new Edge(v5,  81.15) };
             v1.adjacencies = new Edge[]{ new Edge(v0,  79.75),
                     new Edge(v2,  39.42),
                     new Edge(v3, 103.00) };
             v2.adjacencies = new Edge[]{ new Edge(v1,  38.65) };
             v3.adjacencies = new Edge[]{ new Edge(v1, 102.53),
                     new Edge(v5,  61.44),
                     new Edge(v6,  96.79) };
             v4.adjacencies = new Edge[]{ new Edge(v5, 133.04) };
             v5.adjacencies = new Edge[]{ new Edge(v0,  81.77),
                     new Edge(v3,  62.05),
                     new Edge(v4, 134.47),
                     new Edge(v6,  91.63) };
             v6.adjacencies = new Edge[]{ new Edge(v3,  97.24),
                     new Edge(v5,  87.94) };
             vertices = new Vertex[]{ v0, v1, v2, v3, v4, v5, v6 };
             return v0;
         }

         private static Vertex example2() throws FileNotFoundException {
             long start = System.currentTimeMillis();
             Vertex g = readGraph("dijkstraData.txt" );

             System.out.println( "Read from input file: " + ( System.currentTimeMillis() - start ) );
             System.out.println( "Graph: " + vertices.length+ " vertices. Calculating distances from: "+g.name);
             return g;
         }

         public static void main(String[] args) throws FileNotFoundException {
//            computePaths(example1());
            computePaths(example2());

            for (Vertex v : vertices) {
                System.out.println("Distance to " + v + ": " + v.minDistance);
                List<Vertex> path = getShortestPathTo(v);
                System.out.println("Path: " + path);
            }
         }
     }

