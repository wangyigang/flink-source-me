package com.wangyg.leetcode;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class Leetcode225 {
    @Test
    public void test(){

    }
    //队列实现栈
    class MyStack {
        //使用两个队列, 先将元素添加到一个栈中，然后再将元素倒入另一个栈中，这样顺序就还原正常
        private Queue<Integer> a = new LinkedList<>();
        private Queue<Integer> b = new LinkedList<>();

        /** Initialize your data structure here. */
        public MyStack() {

        }

        /** Push element x onto stack. */
        /**
         * 通过总结： 有循环向两个容器交换数据的逻辑，所以可以交换两个容器的指向，这样效率更高，聪明
         * @param x
         */
        public void push(int x) {
            a.offer(x);
            // 将b队列中元素全部转给a队列
            while(!b.isEmpty())
                a.offer(b.poll());
            // 交换a和b,使得a队列没有在push()的时候始终为空队列
            Queue temp = a;
            a = b;
            b = temp;
        }

        /** Removes the element on top of the stack and returns that element. */
        public int pop() {
            return b.poll();
        }

        /** Get the top element. */
        public int top() {
            return b.peek();
        }

        /** Returns whether the stack is empty. */
        public boolean empty() {
            return b.isEmpty();
        }
    }

/**
 * Your MyStack object will be instantiated and called as such:
 * MyStack obj = new MyStack();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.top();
 * boolean param_4 = obj.empty();
 */
}
