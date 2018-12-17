package indi.hiro.common.ds.generic;

import indi.hiro.common.ds.util.DataStructureException;
import indi.hiro.common.lang.IntegerArrays;

import java.util.Random;
import java.util.function.ToIntFunction;

public class OrderedArray<T> extends ArrayWrap<T> {

    private static final long serialVersionUID = 0xE5C39D2E4B89CDE8L;

    private static final int DEFAULT_CAPACITY = 8;

    public static void classTest() {
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            OrderedArray<Integer> array = new OrderedArray<>(Integer.class, Integer::intValue);
            int len = random.nextInt(75);
            for (int j = 0; j < len; j++) {
                array.add(random.nextInt(100));
            }
            if (!IntegerArrays.increasing(array.valueArray())) {
                throw new DataStructureException();
            }
            if (len > 0) {
                int target = array.get(random.nextInt(len));
                if (target != array.get(array.firstIndexOf(target))) {
                    throw new DataStructureException();
                }
                if (target != array.get(array.lastIndexOf(target))) {
                    throw new DataStructureException();
                }
            }
        }
    }

    protected ToIntFunction<T> evaluator;

    public OrderedArray(Class<T> classT, ToIntFunction<T> evaluator, int initialCapacity) {
        super(classT);
        this.evaluator = evaluator;
        array = constructArray(Math.max(1, initialCapacity));
    }

    public OrderedArray(Class<T> classT, ToIntFunction<T> evaluator) {
        this(classT, evaluator, DEFAULT_CAPACITY);
    }

    public int[] valueArray() {
        int[] valueArray = new int[size];
        for (int i = 0; i < size; i++) {
            valueArray[i] = evaluator.applyAsInt(array[i]);
        }
        return valueArray;
    }

    public void add(T t) {
        insert(t, findInsertIndex(evaluator.applyAsInt(t)));
    }

    private int findInsertIndex(int val) {
        int iL = 0, iR = size;
        while (iL < iR) {
            int iM = (iL + iR) / 2;
            if (val < evaluator.applyAsInt(array[iM])) {
                iR = iM;
            } else if (iL != iM) {
                iL = iM;
            } else {
                break;
            }
        }
        return iR;
    }

    private void insert(T t, int index) {
        ensureCapacity(size + 1);
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = t;
        size++;
    }

    public boolean remove(T t) {
        int index = firstIndexOf(t);
        if (index >= 0) {
            System.arraycopy(array, index + 1, array, index, size - index - 1);
            size--;
            array[size] = null;
            return true;
        }
        return false;
    }

    @Override
    public int firstIndexOf(T t, int start) {
        rangeCheck(start);
        int val = evaluator.applyAsInt(t);
        int iL = start, iR = size;
        while (iL < iR) {
            int iM = (iL + iR) / 2;
            if (val <= evaluator.applyAsInt(array[iM])) {
                iR = iM;
            } else if (iL != iM) {
                iL = iM;
            } else {
                break;
            }
        }
        while (iR < size) {
            T at = array[iR];
            if (at == t) {
                return iR;
            }
            if (val != evaluator.applyAsInt(at)) {
                return -1;
            }
            iR++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(T t, int start) {
        rangeCheck(start);
        int val = evaluator.applyAsInt(t);
        int iL = 0, iR = start + 1;
        while (iL < iR) {
            int iM = (iL + iR) / 2;
            if (val < evaluator.applyAsInt(array[iM])) {
                iR = iM;
            } else if (iL != iM) {
                iL = iM;
            } else {
                break;
            }
        }
        while (iL >= 0) {
            T at = array[iL];
            if (at == t) {
                return iL;
            }
            if (val != evaluator.applyAsInt(at)) {
                return -1;
            }
            iL--;
        }
        return -1;
    }
}
