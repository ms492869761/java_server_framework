package com.game.core.common.tree.rbtree;

public class RBTree <K extends Comparable<K>,V>{
	
	private RBTNode<K,V> root;
	
	public void insert(K key,V value) {
		RBTNode<K,V> node = new RBTNode<K,V>(key,value);
		insert(node);
	}
	

	
	
	private void leftRotate(RBTNode<K,V> x) {
		RBTNode<K,V> y = x.getRight();
		x.setRight(y.getLeft());
		if(y.getLeft()!=null) {
			y.getLeft().setParent(x);
		}
		y.setParent(x.getParent());
		if(x.getParent()==null) {
			this.root=y;
		} else {
			if(x.getParent().getLeft()==x) {
				x.getParent().setLeft(y);
			} else {
				x.getParent().setRight(y);
			}
		}
		y.setLeft(x);
		x.setParent(y);
	}
	
	private void rightRotate(RBTNode<K,V> y) {
		RBTNode<K, V> x = y.getLeft();
		y.setLeft(x.getRight());
		if(x.getRight()!=null) {
			x.getRight().setParent(y);
		}
		x.setParent(y.getParent());
		if(y.getParent()==null) {
			this.root=x;
		} else {
			if(y==y.getParent().getRight()) {
				y.getParent().setRight(x);
			} else {
				y.getParent().setLeft(x);
			}
		}
		x.setRight(y);
		y.setParent(x);
	}
	
	private void insert(RBTNode<K,V> node) {
		int cmp;
		RBTNode<K,V> y=null;
		RBTNode<K,V> x=this.root;
		
		while(x!=null) {
			y=x;
			cmp=node.getKey().compareTo(x.getKey());
			if(cmp<0) {
				x=x.getLeft();
			}else {
				x=x.getRight();
			}
		}
		node.setParent(y);
		if(y!=null) {
			cmp=node.getKey().compareTo(y.getKey());
			if(cmp<0) {
				y.setLeft(node);
			} else {
				y.setRight(node);
			}
		} else {
			this.root=node;
		}
		node.setType(RBTType.RED);
		insertFixUp(node);
	}
	
	private void insertFixUp(RBTNode<K,V> node) {
		RBTNode<K,V> parent,gparent;
		while((parent=node.getParent())!=null&&parent.getType()==RBTType.RED) {
			gparent=parent.getParent();
			if(parent==gparent.getLeft()) {
				RBTNode<K, V> uncle = gparent.getRight();
				if(uncle!=null && uncle.getType()==RBTType.RED) {
					uncle.setType(RBTType.BLACK);
					parent.setType(RBTType.BLACK);
					gparent.setType(RBTType.RED);
					node=gparent;
					continue;
				}
				if(parent.getRight()==node) {
					leftRotate(parent);
					RBTNode<K, V> tmp = parent;
					parent=node;
					node=tmp;
				}
				parent.setType(RBTType.BLACK);
				gparent.setType(RBTType.RED);
				rightRotate(gparent);
			} else {
				RBTNode<K, V> uncle = gparent.getLeft();
				if(uncle!=null && uncle.getType()==RBTType.RED) {
					uncle.setType(RBTType.BLACK); 
					parent.setType(RBTType.BLACK);
					gparent.setType(RBTType.RED);
					node=gparent;
					continue;
				}
				if(parent.getLeft()==node) {
					rightRotate(parent);
					RBTNode<K, V> tmp = parent;
					parent=node;
					node=tmp;
				}
				parent.setType(RBTType.BLACK);
				gparent.setType(RBTType.RED);
				leftRotate(gparent);
			}
		} 
		this.root.setType(RBTType.BLACK);
	}
	
	private void remove(RBTNode<K,V> node) {
		RBTNode<K,V> child, parent;
		RBTType color;
		if(node.getLeft()!=null&&node.getRight()!=null) {
			RBTNode<K, V> replace = node;
			replace=replace.getRight();
			while(replace.getLeft()!=null) {
				replace=replace.getLeft();
			}
			if(node.getParent()!=null) {
				if(node.getParent().getLeft()==node) {
					node.getParent().setLeft(replace);
				} else {
					node.getParent().setRight(replace);
				}
			} else {
				this.root=replace;
			}
			child = replace.getRight();
			parent=replace.getParent();
			color=replace.getType();
			if(parent==node) {
				parent=replace;
			} else {
				if(child!=null) {
					child.setParent(parent);
				}
				parent.setLeft(child);
				replace.setRight(node.getRight());
				node.getRight().setParent(replace);
			}
			
			replace.setParent(node.getParent());
			replace.setType(node.getType());
			replace.setLeft(node.getLeft());
			node.getLeft().setParent(replace);
			if(color==RBTType.BLACK) {
				removeFixUp(child, parent);
			}
			node=null;
			return ;
		}
		if(node.getLeft()!=null) {
			child=node.getLeft();
		} else {
			child=node.getRight();
		}
		parent=node.getParent();
		color=node.getType();
		if(child!=null) { 
			child.setParent(parent);
		}
		if(parent!=null) {
			if(parent.getLeft()==node) {
				parent.setLeft(child);
			} else {
				parent.setRight(child);
			}
		} else {
			root=child;
		}
		node=null;
	}
	
	private void removeFixUp(RBTNode<K,V> node,RBTNode<K,V> parent) {
		RBTNode<K,V> other;
		while((node==null||node.getType()==RBTType.BLACK)&&node!=root) {
			if(parent.getLeft()==node) {
				other=parent.getRight();
				if(other.getType()==RBTType.RED) {
					other.setType(RBTType.BLACK);
					parent.setType(RBTType.RED);
					leftRotate(parent);
					other=parent.getRight();
				}
				if((other.getLeft()==null||other.getLeft().getType()==RBTType.BLACK) && (other.getRight()==null||other.getRight().getType()==RBTType.BLACK)) {
					other.setType(RBTType.RED);
					node=parent;
					parent=node.getParent();
				} else {
					if(other.getRight()==null || other.getRight().getType()==RBTType.BLACK) {
						other.getLeft().setType(RBTType.BLACK);
						other.setType(RBTType.RED);
						rightRotate(other);
						other=parent.getRight();
					}
					other.setType(parent.getType());
					parent.setType(RBTType.BLACK);
					other.getRight().setType(RBTType.BLACK);
					leftRotate(parent);
					node=root;
					break;
				}
			} else {
				other=parent.getLeft();
				if(other.getType()==RBTType.RED) {
					other.setType(RBTType.BLACK);
					parent.setType(RBTType.RED);
					rightRotate(parent);
					other=parent.getLeft();
				}
				if((other.getLeft()==null||other.getLeft().getType()==RBTType.BLACK)&&(other.getRight()==null||other.getRight().getType()==RBTType.BLACK)) {
					other.setType(RBTType.RED);
					node=parent;
					parent=node.getParent();
				} else {
					if(other.getLeft()==null || other.getLeft().getType()==RBTType.BLACK) {
						other.getRight().setType(RBTType.BLACK);
						other.setType(RBTType.RED);
						leftRotate(other);
						other=parent.getLeft();
					}
					other.setType(parent.getType());
					parent.setType(RBTType.BLACK);
					other.getLeft().setType(RBTType.BLACK);
					rightRotate(parent);
					node=root;
					break;
				}
			}
		}
		if(node!=null) {
			node.setType(RBTType.BLACK);
		}
	}
	
	
}


class RBTNode<K,V> {
	
	private K key;
	
	private V value;
	
	private RBTNode<K,V> parent;
	
	private RBTNode<K,V> left;
	
	private RBTNode<K,V> right;
	
	private RBTType type;
	
	public RBTNode(K key,V value) {
		this.key=key;
		this.value=value;
	}
	
	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public RBTNode<K, V> getParent() {
		return parent;
	}

	public void setParent(RBTNode<K, V> parent) {
		this.parent = parent;
	}

	public RBTNode<K, V> getLeft() {
		return left;
	}

	public void setLeft(RBTNode<K, V> left) {
		this.left = left;
	}

	public RBTNode<K, V> getRight() {
		return right;
	}

	public void setRight(RBTNode<K, V> right) {
		this.right = right;
	}

	public RBTType getType() {
		return type;
	}

	public void setType(RBTType type) {
		this.type = type;
	}
	
	
	
	
	
	
}