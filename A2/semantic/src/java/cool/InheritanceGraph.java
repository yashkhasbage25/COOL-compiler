package cool;

import java.util.*;
import cool.CoolUtils;

class Vertex {

	private String name;
	private Vertex parent;
	public List<Vertex> children;

	public Vertex(String node_name) {
		this.name = node_name;
		this.children = new ArrayList<>();
		this.parent = null;
	}

	public String getName() {
		return name;
	}

	public boolean equals(Vertex node) {
		return this.name == node.getName();
	}

	public void add_neighbour(Vertex node) {
		children.add(node);
	}

	public void clear() {
		// clear children list
	}
}

public class InheritanceGraph {
	private Map<String, Vertex> name2Vertex;
	public Set<Vertex> vertices;
	public Map<String, String> parentNameMap;

	public String Root;

	public InheritanceGraph() {
		name2Vertex = new HashMap<String, Vertex>();
		parentNameMap = new HashMap<String, String>();
		vertices = new HashSet<Vertex>();
		Root = "Object";
	}

	public boolean checkClass(String classname) {
		return name2Vertex.containsKey(classname);
	}

	private Vertex getVertex(String name) {
		return name2Vertex.get(name);
	}

	private void addVertexToMap(String name, Vertex v) {
		name2Vertex.put(name, v);
	}

	private void addParentToMap(String name, String par_name) {
		parentNameMap.put(name, par_name);
	}

	public void ClearGraph() {
		for (Vertex v : vertices) {
			v.clear(); // clear the adjacency list for each Vertex
		}
	}

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

	public boolean cyclePresent() {
		Map<Vertex, Integer> colour = new HashMap<Vertex, Integer>();
		for (Vertex v : vertices) {
			colour.put(v, 0);
		}
		for (Vertex v : vertices) {
			if (dfs(v, colour) == true)
				return true;
		}
		return false;
	}

	public boolean dfs(Vertex v, Map<Vertex, Integer> colour) {
		colour.replace(v, 1);
		for (Vertex k : v.children) {
			if (colour.get(k) == 1)
				return true;
			else if (colour.get(k) == 0 && dfs(k, colour))
				return true;
		}
		colour.replace(v, 2);
		return false;
	}

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
			if (!path_to_v.empty() && !path_to_u.empty() && path_to_v.peek() == path_to_u.peek()) {
				prev = path_to_v.peek();
				path_to_v.pop();
				path_to_u.pop();
			} else
				return prev;
		}
	}

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
