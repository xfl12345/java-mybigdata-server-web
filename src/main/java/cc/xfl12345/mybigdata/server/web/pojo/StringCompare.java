package cc.xfl12345.mybigdata.server.web.pojo;

import java.util.LinkedList;

public class StringCompare {
    private static class LeftAndRight {
        public int left;
        public int right;

        public LeftAndRight(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    public static boolean compare(String a, String b, int startIndex, int endIndex, int stepSize) {
        // 长度都不够，还比啥？
        if (b.length() <= startIndex || b.length() <= endIndex ||
            a.length() <= startIndex || a.length() <= endIndex) {
            return false;
        }

        LinkedList<LeftAndRight> branches = new LinkedList<>();
        LinkedList<LeftAndRight> branches4consume = new LinkedList<>();

        boolean result = true;
        int totalWork = endIndex - startIndex + 1;
        int count = 0;
        branches4consume.add(new LeftAndRight(startIndex, endIndex));

        while (result && count < totalWork) {
            while (result && !branches4consume.isEmpty()) {
                LeftAndRight branch = branches4consume.pollFirst();
                int currentStepSize = 1;

                // 比较指定步长是否超界
                if (branch.left + (stepSize - 1) < branch.right - (stepSize - 1)) {
                    currentStepSize = stepSize;
                } else {
                    // 超界，则取 区间长度的一半（不保留小数） 为步长
                    currentStepSize = (branch.right - branch.left + 1) / 2;
                }

                for (int i = 0; i < currentStepSize; i++) {
                    int compareLeft = branch.left + i;
                    int compareRight = branch.right - i;

                    if (a.indexOf(compareLeft) != b.indexOf(compareLeft)) {
                        count++;
                        result = false;
                        break;
                    }
                    if (a.indexOf(compareRight) != b.indexOf(compareRight)) {
                        count++;
                        result = false;
                        break;
                    }
                }
                branch.left += currentStepSize;
                branch.right -= currentStepSize;
                // 区间结束了，或者还差 1 个位没比较。
                if (branch.right <= branch.left) {
                    // 如果下次比较的下标相等，立刻比较，减少一次循环
                    if (branch.left == branch.right) {
                        if (a.indexOf(branch.left) != b.indexOf(branch.left)) {
                            count++;
                            result = false;
                            break;
                        }
                    }
                } else {
                    // 未完待续
                    branches.add(branch);

                    // 如果区间剩余空间大于四倍步长，则二分
                    if (branch.right - branch.left + 1 > stepSize * 4) {
                        int mid = (branch.left + branch.right) / 2;
                        branches.add(new LeftAndRight(mid + 1, branch.right));
                        branch.right = mid;
                    }
                }
            }

            // 为避免产生垃圾、构造新对象产生的性能损耗。直接使用交换大法。
            LinkedList<LeftAndRight> tmp = branches4consume;
            branches4consume = branches;
            branches = tmp;
        }

        return result;
    }

}
