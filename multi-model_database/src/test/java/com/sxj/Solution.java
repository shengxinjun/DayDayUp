package com.sxj;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Solution {
    /**
     * 
     * @param root TreeNode类 the root of binary tree
     * @return int整型二维数组
     */
    public int[][] threeOrders (TreeNode root) {
        // write code here
        int b[] =preOrder(root);
        int[][] a=new int[3][b.length];
        a[0]=b;
        a[1]=midOrder(root);
        a[2]=lateOrder(root);
        return a;
    }
    int[] preOrder(TreeNode root){
        Stack<TreeNode> s = new Stack();
        List<Integer> list = new ArrayList<>();
        s.add(root);
        while(!s.isEmpty()){
        	root=s.pop();
        	list.add(root.val);
            if(root.right!=null)
                s.push(root.right);
            if(root.left!=null)
                s.push(root.left);
        }
        int[] a=new int[list.size()];
        int index=0;
        for(Integer i:list){
            a[index++]=i;
        }
        return a;
    }
    int[] midOrder(TreeNode root){
        Stack<TreeNode> s = new Stack();
        List<Integer> list = new ArrayList<>();
        while(root!=null||!s.isEmpty()){
            while(root!=null){
            	s.push(root);
            	root=root.left;
            }
            root=s.pop();
            list.add(root.val);
            root=root.right;
        	
        }
        int[] a=new int[list.size()];
        int index=0;
        for(Integer i:list){
            a[index++]=i;
        }
        return a;
    }
    int[] lateOrder(TreeNode root){
        TreeNode preNode=null;
        Stack<TreeNode> s = new Stack();
        List<Integer> list = new ArrayList<>();
        while(root!=null||s.size()!=0){
            while(root!=null){
                s.add(root);
                root=root.left;
            }
            TreeNode temp=s.peek();
            if(temp.right!=null&&temp.right!=preNode)
                root=temp.right;
            else{
            	temp=s.pop();
                list.add(temp.val);
                preNode=temp;
            }
        }
        int[] a=new int[list.size()];
        int index=0;
        for(Integer i:list){
            a[index++]=i;
        }
        return a;
    }
    TreeNode create(int a[]){
    	TreeNode root = new TreeNode();
    	Queue<TreeNode> queue = new LinkedBlockingQueue<>();
    	int index = 0;
    	if(a.length==0)return null;
    	root.val=a[index++];
    	root.left=null;
    	root.right=null;
    	queue.add(root);
    	while(!queue.isEmpty()&&index<a.length){
    		TreeNode temp = queue.poll();
    		if(index<a.length){
    			int val=a[index++];
    			if(val!=0){
    				TreeNode left = new TreeNode();
    				left.left=null;
    				left.right=null;
    				left.val=val;
    				queue.add(left);
    				temp.left=left;
    			}
    		}
    		if(index<a.length){
    			int val=a[index++];
    			if(val!=0){
    				TreeNode right = new TreeNode();
    				right.left=null;
    				right.right=null;
    				right.val=val;
    				queue.add(right);
    				temp.right=right;
    			}
    		}
    	}
    	return root;
    }
}