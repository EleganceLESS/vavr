/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Option;

/**
 * Interface for immutable, indexed sequences.
 * <p>
 * Efficient random access is characteristic for indexed sequences.
 *
 * @param <T> component type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface IndexedSeq<T> extends Seq<T> {

    // -- Adjusted return types of Seq methods

    @Override
    IndexedSeq<T> append(T element);

    @Override
    IndexedSeq<T> appendAll(java.lang.Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> clear();

    @Override
    IndexedSeq<Tuple2<T, T>> crossProduct();

    @Override
    IndexedSeq<IndexedSeq<T>> crossProduct(int power);

    @Override
    <U> IndexedSeq<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that);

    @Override
    IndexedSeq<? extends IndexedSeq<T>> combinations();

    @Override
    IndexedSeq<? extends IndexedSeq<T>> combinations(int k);

    @Override
    IndexedSeq<T> distinct();

    @Override
    IndexedSeq<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> IndexedSeq<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    IndexedSeq<T> drop(int n);

    @Override
    IndexedSeq<T> dropRight(int n);

    @Override
    IndexedSeq<T> dropWhile(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> filter(Predicate<? super T> predicate);

    @Override
    <U> IndexedSeq<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    <U> IndexedSeq<U> flatten();

    @Override
    <C> Map<C, ? extends IndexedSeq<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    IndexedSeq<T> init();

    @Override
    Option<? extends IndexedSeq<T>> initOption();

    @Override
    IndexedSeq<T> insert(int index, T element);

    @Override
    IndexedSeq<T> insertAll(int index, java.lang.Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> intersperse(T element);

    @Override
    default T last() {
        if (isEmpty()) {
            throw new NoSuchElementException("last of empty IndexedSeq");
        } else {
            return get(length() - 1);
        }
    }
    
    @Override
    default int lastIndexWhere(Predicate<? super T> p, int end) {
        int i = Math.min(end, length()-1);
        while(i >= 0 && !p.test(this.get(i))) i--;
        return i;
    }
    
    @Override
    <U> IndexedSeq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    IndexedSeq<T> padTo(int length, T element);

    @Override
    IndexedSeq<T> patch(int from, java.lang.Iterable<? extends T> that, int replaced);

    @Override
    Tuple2<? extends IndexedSeq<T>, ? extends IndexedSeq<T>> partition(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> peek(Consumer<? super T> action);

    @Override
    IndexedSeq<? extends IndexedSeq<T>> permutations();

    @Override
    IndexedSeq<T> prepend(T element);

    @Override
    IndexedSeq<T> prependAll(java.lang.Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> remove(T element);

    @Override
    IndexedSeq<T> removeFirst(Predicate<T> predicate);

    @Override
    IndexedSeq<T> removeLast(Predicate<T> predicate);

    @Override
    IndexedSeq<T> removeAt(int index);

    @Override
    IndexedSeq<T> removeAll(T element);

    @Override
    IndexedSeq<T> removeAll(java.lang.Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> replace(T currentElement, T newElement);

    @Override
    IndexedSeq<T> replaceAll(T currentElement, T newElement);

    @Override
    IndexedSeq<T> retainAll(java.lang.Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> reverse();
    
    @Override
    default Iterator<T> reverseIterator() {
        return new AbstractIterator<T>() {
            private int i = length();            
            
            @Override
            public boolean hasNext() {
                return i > 0;
            }
            
            @Override
            public T next() {
                if(i > 0)
                    return IndexedSeq.this.get(--i);
                else
                    return Iterator.<T>empty().next();
            }
        };
    }

    @Override
    IndexedSeq<T> slice(int beginIndex, int endIndex);

    @Override
    IndexedSeq<T> sort();

    @Override
    IndexedSeq<T> sort(Comparator<? super T> comparator);

    @Override
    <U extends Comparable<? super U>> IndexedSeq<T> sortBy(Function<? super T, ? extends U> mapper);

    @Override
    <U> IndexedSeq<T> sortBy(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends IndexedSeq<T>, ? extends IndexedSeq<T>> span(Predicate<? super T> predicate);

    @Override
    default boolean startsWith(Iterable<? extends T> that, int offset) {
        if(offset < 0) return false;
        if(that instanceof IndexedSeq) {
            IndexedSeq<? extends T> dhat = (IndexedSeq<? extends T>) that;
            int i = offset;
            int j = 0;
            int thisLen = length();
            int thatLen = dhat.length();
            while(i < thisLen && j < thatLen && Objects.equals(this.get(i), dhat.get(j))) {
                i++;
                j++;
            }
            return j == thatLen;
        } else {
            int i = offset;
            int thisLen = length();
            java.util.Iterator<? extends T> thatElems = that.iterator();
            while(i < thisLen && thatElems.hasNext()) {
                if(!Objects.equals(this.get(i), thatElems.next()))
                    return false;
                i++;
            }
            return !thatElems.hasNext();
        }
    }
    
    @Override
    default boolean endsWith(Seq<? extends T> that) {
        if(that instanceof IndexedSeq) {
            int i = length() - 1;
            int j = that.length() - 1;
            if(j > i) 
                return false;
            else {
                while(j >= 0) {
                    if(!Objects.equals(this.get(i), that.get(j)))
                        return false;
                    i--;
                    j--;
                }
                return true;
            }
        } else {
            return Seq.super.endsWith(that);
        }
    }
    
    @Override
    IndexedSeq<T> subSequence(int beginIndex);

    @Override
    IndexedSeq<T> subSequence(int beginIndex, int endIndex);

    @Override
    IndexedSeq<T> tail();

    @Override
    Option<? extends IndexedSeq<T>> tailOption();

    @Override
    IndexedSeq<T> take(int n);

    @Override
    IndexedSeq<T> takeRight(int n);

    @Override
    IndexedSeq<T> takeUntil(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <U> IndexedSeq<U> unit(java.lang.Iterable<? extends U> iterable);

    @Override
    <T1, T2> Tuple2<? extends IndexedSeq<T1>, ? extends IndexedSeq<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <T1, T2, T3> Tuple3<? extends IndexedSeq<T1>, ? extends IndexedSeq<T2>, ? extends IndexedSeq<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    IndexedSeq<T> update(int index, T element);

    @Override
    <U> IndexedSeq<Tuple2<T, U>> zip(java.lang.Iterable<U> that);

    @Override
    <U> IndexedSeq<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem);

    @Override
    IndexedSeq<Tuple2<T, Integer>> zipWithIndex();

}
