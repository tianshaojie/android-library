package cn.skyui.app.library.utils.threadpool;

import java.util.Comparator;

/**
 * @author tianshaojie
 * @date 2017/12/19
 */
public class PriorityCompare<T extends PriorityRunnable> implements Comparator<T> {

    @Override
    public int compare(T lhs, T rhs) {
        return rhs.getPriority() - lhs.getPriority();
    }

}
