package cool;

import java.util.*;

public class IRClass {
	public String name;
	public String parent = null;
	public HashMap<String, AST.attr> alist;
	public HashMap<String, AST.method> mlist;
	public HashMap<String, Integer> attrIndex;

	IRClass(String nm, String pr, HashMap<String, AST.attr> al, HashMap<String, AST.method> ml) {
		name = new String(nm);
		if (pr != null)
			parent = new String(pr);
		alist = new HashMap<String, AST.attr>();
		alist.putAll(al);
		mlist = new HashMap<String, AST.method>();
		mlist.putAll(ml);
		int ind=0;
		System.out.println(alist);
		attrIndex = new HashMap<String, Integer>();
		if(alist.size() > 0)
			for(Map.Entry<String,AST.attr> entry: alist.entrySet())
				attrIndex.put(entry.getKey(),ind++);
	}
}
