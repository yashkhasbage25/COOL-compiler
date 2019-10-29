package cool;

import java.util.HashMap;

public class IRClass {
	public String name;
	public String parent = null;
	public HashMap<String, AST.attr> alist;
	public HashMap<String, AST.method> mlist;

	IRClass(String nm, String pr, HashMap<String, AST.attr> al, HashMap<String, AST.method> ml) {
		name = new String(nm);
		if (pr != null)
			parent = new String(pr);
		alist = new HashMap<String, AST.attr>();
		alist.putAll(al);
		mlist = new HashMap<String, AST.method>();
		mlist.putAll(ml);
	}
}
