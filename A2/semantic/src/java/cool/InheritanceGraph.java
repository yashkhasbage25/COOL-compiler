package cool;

import java.util.*;
import cool.CoolUtils;

// Vertex Class (Vertex of InheritanceGraph)
class Vertex {

	private String name;
	private Vertex parent;
	public List<Vertex> children;

	// constructor for vertex class with name of class
	public Vertex(String node_name) {
		this.name = node_name;
		this.children = new ArrayList<>();
		this.parent = null;
	}

	// returns name of vertex
	public String getName() {
		return name;
	}

	// retursns if the name of node is same as this vertex
	public boolean equals(Vertex node) {
		return this.name == node.getName();
	}

	// add neighbours to the adjacency list of this vertex
	public void add_neighbour(Vertex node) {
		children.add(node);
	}
}

// Inheritance Graph Class
public class InheritanceGraph {
	private Map<String, Vertex> name2Vertex; // a map from vertex name to vertex
	public Set<Vertex> vertices; // set of all vertices in the graph
	public Map<String, String> parentNameMap; // map of name of parent given a vertex

	public String Root; // root of the InheritanceGraph which is "Object"

	// Constructor for InheritanceGraph
	public InheritanceGraph() {
		name2Vertex = new HashMap<String, Vertex>();
		parentNameMap = new HashMap<String, String>();
		vertices = new HashSet<Vertex>();
		Root = "Object";
	}

	// checks whether class is present
	public boolean checkClass(String classname) {
		return name2Vertex.containsKey(classname);
	}

	// given the name of vertex returns the vertex
	private Vertex getVertex(String name) {
		return name2Vertex.get(name);
	}

	// add vertex to map
	private void addVertexToMap(String name, Vertex v) {
		name2Vertex.put(name, v);
	}

	// adds parent to map
	private void addParentToMap(String name, String par_name) {
		parentNameMap.put(name, par_name);
	}

	// add edge between 2 vertices in graph
	public void addEdge(String start, String end) {
		Vertex u, v;
		if (name2Vertex.containsKey(start))
			u = getVertex(start);
		else
			u = new Vertex(start);

		if (name2Vertex.containsKey(end))
			v = getVertex(end);
		else
			v = new Vertex(end);

		vertices.add(u);
		vertices.add(v);
		addVertexToMap(start, u);
		addVertexToMap(end, v);
		u.add_neighbour(v);
		parentNameMap.put(end, start);
	}

	// checks whether cycle is present in inheritance graph
	public Set<String> cyclePresent() {
		Set<String> nodes = new HashSet<String>();
		Map<Vertex, Integer> colour = new HashMap<Vertex, Integer>();
		for (Vertex v : vertices) {
			colour.put(v, 0);
		}
		for (Vertex v : vertices) {
			if (dfs(v, colour) == true) {
				Set<Vertex> nodesInCycle = colour.keySet();
				for (Vertex k : nodesInCycle) {
					if (colour.get(k) == 1)
						nodes.add(k.getName());
				}
			}
		}
		// for (String l : nodes)
		// System.out.println(l);
		return nodes;
	}

	// we use colors to get cycle in directed graph
	public boolean dfs(Vertex v, Map<Vertex, Integer> colour) {
		colour.replace(v, 1);
		for (Vertex k : v.children) {
			if (k.getName().equals(v.getName())) {
				return true;
			}
			if (colour.get(k) == 1)
				return true;
			else if (colour.get(k) == 0 && dfs(k, colour))
				return true;
		}
		colour.replace(v, 2);
		return false;
	}

	// gives the lowestCommonAncestor between 2 vertices
	public String LowestCommonAncestor(String u, String v) {

		Stack<String> path_to_u = new Stack<String>();
		Stack<String> path_to_v = new Stack<String>();
		String iter = u;
		while (iter != null) {
			path_to_u.push(iter);
			iter = parentNameMap.get(iter);
		}
		iter = v;
		while (iter != null) {
			path_to_v.push(iter);
			iter = parentNameMap.get(iter);
		}
		String prev = Root;
		while (true) {
			if (!path_to_v.empty() && !path_to_u.empty() && (path_to_v.peek().equals(path_to_u.peek()))) {
				prev = path_to_v.peek();
				path_to_v.pop();
				path_to_u.pop();
			} else
				return prev;
		}
	}

	// checks if classes conforms
	public boolean conforms(String class1, String class2) {
		if (class1.equals("no_type"))
			return true;
		if (class2 == null)
			return false;
		while (class1 != null) {
			if (class1.equals(class2)) {
				return true;
			}
			class1 = parentNameMap.get(class1);
		}
		return false;
	}
}
