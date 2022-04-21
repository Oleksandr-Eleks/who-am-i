package com.eleks.academy.whoami.utility.impl;

import com.eleks.academy.whoami.utility.Tree;

public class DecisionTree implements Tree{

	private Tree left;//No
	private Tree right;//Yes
	private String data;
	
	public DecisionTree(String data) {
		this.data = data;
		this.left = null; 
		this.right = null;
	}
	
	public DecisionTree(String data, Tree left, Tree right) {
		this.data = data;
		this.left = left; 
		this.right = right;
	}
	
	public DecisionTree(Tree tree) {
		this.data = tree.getData();
		this.right = tree.getRight();
		this.left = tree.getLeft();		
	}
	
	public Tree getNode(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecisionTree getLeft() {
		return (DecisionTree)left;
	}

	public void setLeft(Tree left) {
		this.left = left;
	}
	
	public void setLeft(String data) {
		this.left = new DecisionTree(data);
	}

	@Override
	public DecisionTree getRight() {
		return (DecisionTree)right;
	}

	public void setRight(Tree right) {
		this.right = right;
	}
	
	public void setRight(String data) {
		this.right = new DecisionTree(data);
	}

	@Override
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
