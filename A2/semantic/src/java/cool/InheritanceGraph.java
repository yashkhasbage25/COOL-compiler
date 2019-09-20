package cool;

import java.util.*;

public class Vertex {

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

	public add_neighbour(Vertex node){
		children.add(node);
	}

	public void clear() {
		// clear children list
	}
}

public static class InheritanceGraph {
	private Map<String, Vertex> name2Vertex;
	private List<Vertex> vertices;
	private Map<String, String> parentNameMap;

	private String root;

	public InheritanceGraph() {
		name2Vertex = new HashMap<String, Vertex>();
		parentNameMap = new HashMap<String, String>();
		vertices = new ArrayList<Vertex>();
		root = "Object";
	}

	public boolean checkClass(String classname) {
		return name2Index.containsKey(classnames);
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
		Vertex u = getVertex(start);
		Vertex v = getVertex(end);
		addVertexToMap(start, u);
		addVertexToMap(end, v);
		u.add_neighbour(v);
		addParent(start, end);
		parentNameMap.put(end, start);
	}

	public boolean cycle_present() {
		Map<Vertex, Int> colour = new HashMap<Vertex, Int>();
		for (Vertex v : vertices)
			colour.put(v, 0);
		for (Vertex v : vertices) {
			if (dfs(v, colour) == true)
				return true;
		}
		return false;
	}

	public boolean dfs(Vertex v, Map<Vertex, Int> colour) {
		colour.replace(v, 1);
		for (Vertex k : v.children) {
			if (colour.get(k) == 1)
				return false;
			if (colour.get(k) == 0 && dfs(k, colour))
				return true;
		}
		colour.replace(v, 2);
		return false;
	}

	public String LowestCommonAncestor(String u, String v, String rootVertex) {
		// get path from root to vertex u and vertex v
		// once we have the paths get the lca
		HashSet<Vertex> path_to_u;
		HashSet<Vertex> path_to_v;
		Vertex u1 = name2vertex.get(u);
		Vertex v1 = name2vertex.get(v);
		Vertex root = name2vertex.get(rootVertex);
		path_to_u = get_path(root, u1, path_to_u);
		path_to_v = get_path(root, v1, path_to_v);
		Vertex prev = root;
		for (Vertex it : path_to_u)
			if (path_to_v.contains(it) == false)
				return prev.getName();
			else
				prev = it;
	}

	public boolean conforms(String class1, String class2) {
		if (class1.equals("no_type"))
			return true;
		if (class2 == null)
			return false;
		while (class1 != null) {
			if (class1 == class2) {
				return true;
			}
			class1 = parentNameMap.get(class1);
		}
		return false;
	}

	public HastSet<Vertex> get_path(Vertex u, Vertex target, HastSet<Vertex> path) { // root to vertex path
		path.add(u);
		if (u.equals(target))
			return path;
		for (Vertex k : u.children)
			get_path(k, target, path);
	}

}
