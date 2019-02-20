package com.bule.simple.face;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by lijianzhen1 on 2019/2/20.
 */
public class MyLinkedList<T> {

    transient Node<T> first;
    transient Node<T> last;
    transient int size = 0;


    public MyLinkedList() {
    }

    /**
     * 元素数量
     */
    public int size() {
        return size;
    }

    /**
     * 将指定的元素追加到列表的末尾.
     *
     * @param t element to be appended to this list
     */
    public void add(T t) {
        final Node<T> l = last;
        //设置下个元素指向为空
        final Node<T> newNode = new Node<T>(l, t, null);
        last = newNode;
        //未元素为空方首位，未元素不为空放到下一个位置
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }


    /**
     * 转置列表
     */
    public void reverse() {
        //如果列表中的元素为空返回为空
        if (first == null || first.next == null) {
            return;
        }
        //交换第一个元素和最后一个元素的位置
        Node<T> temp = first;
        first = last;
        last = temp;

        //当前第一个第一个元素的指针遍历反转链表。
        Node<T> node = first;
        while (node != null) {
            temp = node.next;
            node.next = node.prev;
            node.prev = temp;
            node = node.next;
        }

    }


    /**
     * 对应列表中元素
     *
     * @param <T>
     */
    private static class Node<T> {
        T item;
        //下个元素
        Node<T> next;
        //前指针指向元素
        Node<T> prev;

        Node(Node<T> prev, T element, Node<T> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }


    public Iterator<T> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<T> {

        int cursor = 0;
        int lastRet = -1;

        public boolean hasNext() {
            return cursor != size();
        }

        public T next() {
            try {
                //获得指定下标的元素
                T next = get(cursor);
                lastRet = cursor++;
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if (lastRet == -1)
                throw new IllegalStateException();
            throw new RuntimeException("remove is denied");
        }
    }

    /**
     * 获得指定位置的元素的值
     *
     * @param cursor
     * @return
     */
    private T get(int cursor) {
        if (cursor < 0 || cursor > size)
            throw new IndexOutOfBoundsException(String.format("Index out of bound %d", cursor));

        //判断如果是在列表的后半段从后往前遍历，如果是在前半段从前往后遍历寻找元素
        if (cursor < (size >> 1)) {
            Node<T> x = first;
            //循环到对应的指向元素
            for (int i = 0; i < cursor; i++)
                x = x.next;
            return x.item;
        } else {
            Node<T> x = last;
            for (int i = size - 1; i > cursor; i--)
                x = x.prev;
            return x.item;
        }
    }


    public static void main(String[] args) {
        MyLinkedList<String> myLinkedList = new MyLinkedList();
        myLinkedList.add("1");
        myLinkedList.add("2");
        myLinkedList.add("3");

        myLinkedList.reverse();
        Iterator<String> it=myLinkedList.iterator();
        while (it.hasNext()){
            String s=it.next();
            System.out.println(s);
        }
    }
}
