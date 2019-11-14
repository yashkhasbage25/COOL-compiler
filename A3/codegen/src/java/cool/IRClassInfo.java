package cool;

import java.util.*;

public class IRClassInfo {
	public int size;
	public String name;
	public String parent;
	public HashMap<String, AST.attr> classAttrs;
	public HashMap<String, AST.method> classMethods;
	public HashMap<String, Integer> attrIndex;

	IRClassInfo(String className, String parentClassName, HashMap<String, AST.attr> classAttrs, HashMap<String, AST.method> classMethods, int classSize) {
		size = classSize;
		name = new String(className);
		if (parentClassName != null)
			parent = new String(parentClassName);
		else
			parent = null;
		this.classAttrs = new HashMap<String, AST.attr>(classAttrs);
		// alist.putAll(al);
		this.classMethods = new HashMap<String, AST.method>(classMethods);
		// mlist.putAll(ml);

		attrIndex = new HashMap<String, Integer>();
		int index = 1;
		if(this.classAttrs.size() > 0) {
			for(Map.Entry<String, AST.attr> entry: this.classAttrs.entrySet()) {
				this.attrIndex.put(entry.getKey(), index++);
			}
		}
	}
}
