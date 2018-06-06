package com.game.core.common.wordfilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * 敏感词检查服务
 * @author 
 *
 */
public class WordFilterService {
	private List<String> strs=new ArrayList<>();
	private volatile List<String> arrt = new ArrayList<String>();// 文字
	private volatile Node rootNode = new Node('R');
	public WordFilterService(String words,char split){
		try {
			strs.add(";");
			strs.add("|");
			strs.add(",");
			strs.add(".");
			strs.add("/");
			strs.add(":");
			strs.add("_");
			strs.add("[");
			strs.add("]");
			strs.add("(");
			strs.add(")");
			strs.add("~");
			strs.add("!");
			strs.add("@");
			strs.add("#");
			strs.add("$");
			strs.add("%");
			strs.add("^");
			strs.add("&");
			strs.add("*");
			strs.add("(");
			strs.add(")");
			strs.add("-");
			strs.add("'");
			strs.add("\"");
			strs.add("+");
			strs.add("=");
			strs.add("<");
			strs.add(">");
			rootNode = new Node('R');
			arrt.clear();
			
			String keys = words;
			if (keys == null) {
				return;
			}
			String tempstr[] = keys.split(String.valueOf(split));
			for (int i = 0; i < tempstr.length; i++) {
				String ss = tempstr[i];
				arrt.add(ss.replace("\"", "").trim());
			}
			createTree();
		} catch (Exception e) {
		}
		
	}
	
	/**
	 * 过滤敏感词
	 * @param content 内容
	 * @param replaceChar 替换的字符串
	 * @return
	 */
	public String badWordstFilterStr(String content,char replaceChar) {
		char[] chars = content.toCharArray();
		Node node = rootNode;
		StringBuffer buffer = new StringBuffer();
		List<String> badList = new ArrayList<String>();
		int a = 0;
		while (a < chars.length) {
			node = findNode(node, chars[a]);
			if (node == null) {
				node = rootNode;
				a = a - badList.size();
				if (badList.size() > 0) {
					badList.clear();
				}
				buffer.append(chars[a]);
			} else if (node.flag == 1) {
				badList.add(String.valueOf(chars[a]));
				for (int i = 0; i < badList.size(); i++) {
					buffer.append(replaceChar);
				}
				node = rootNode;
				badList.clear();
			} else {
				badList.add(String.valueOf(chars[a]));
				if (a == chars.length - 1) {
					for (int i = 0; i < badList.size(); i++) {
						buffer.append(badList.get(i));
					}
				}
			}
			a++;
		}
		return buffer.toString();
	}

	public static void main(String[] a) {

	}

	private void createTree() {
		for (String str : arrt) {
			char[] chars = str.toCharArray();
			if (chars.length > 0)
				insertNode(rootNode, chars, 0);
		}
	}

	private void insertNode(Node node, char[] cs, int index) {
		Node n = findNode(node, cs[index]);
		if (n == null) {
			n = new Node(cs[index]);
			node.nodes.put(String.valueOf(cs[index]), n);
		}

		if (index == (cs.length - 1))
			n.flag = 1;

		index++;
		if (index < cs.length)
			insertNode(n, cs, index);
	}

	/**
	 * 是否有坏字
	 * 
	 * @param content
	 * @return true有坏字 false 没有
	 */
	public boolean hashBadWords(String content) {
		char[] chars = content.toCharArray();
		Node node = rootNode;
		StringBuffer buffer = new StringBuffer();
		List<String> word = new ArrayList<String>();
		int a = 0;
		while (a < chars.length) {
			node = findNode(node, chars[a]);
			if (node == null) {
				node = rootNode;
				a = a - word.size();
				buffer.append(chars[a]);
				word.clear();
			} else if (node.flag == 1) {
				node = null;
				return true;
			} else {
				word.add(String.valueOf(chars[a]));
			}
			a++;
		}
		chars = null;
		word.clear();
		word = null;
		return false;
	}
	
	public boolean hasSensitiveSymbol(String str){
		for (String string: strs) {
			if(str.contains(string)){
				return true;
			}
		}
		return false;
	}

	private Node findNode(Node node, char c) {
		return node.nodes.get(String.valueOf(c));
	}

	private static class Node {
		@SuppressWarnings("unused")
		public char c;
		public int flag;
		public HashMap<String, Node> nodes = new HashMap<String, Node>();

		public Node(char c) {
			this.c = c;
			this.flag = 0;
		}
		@SuppressWarnings("unused")
		public Node(char c, int flag) {
			this.c = c;
			this.flag = flag;
		}
	}


}
