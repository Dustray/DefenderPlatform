package cn.dustray.webfilter;

public class PatternMatching {
    /**
     * Sunday模式匹配算法
     *
     * @param sourse 源
     * @param target 目标
     * @return
     */
    public boolean isBelongToSource(String target, String sourse) {
        if (sunday(sourse.toCharArray(), target.toCharArray(), 0, 0)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean sunday(char[] sourseArray, char[] targetArray, int j, int k) {
        for (int i = j; i < j + targetArray.length; i++) {
            if (targetArray[i - j] == sourseArray[i]) {
                k++;
                continue;
            } else {
                break;
            }
        }
        //k-j代表本次比较的次数，如果和目标字符串的长度相等，则说明每个字符都对比成功，即在源字符串中找到了目标字符串
        if (k - j == targetArray.length) return true;//成功

        k = j + targetArray.length;
        if (k < (sourseArray.length - 1)) {
            int value = check(sourseArray[k], targetArray);
            int step = -value;
            j = k + step;
            return sunday(sourseArray, targetArray, j, j);
        } else {
            return false;
        }
    }

    /**
     * 检查目标字符串tempT是否包含c
     *
     * @param c
     * @param tempT
     * @return
     */
    private int check(char c, char[] tempT) {
        for (int i = tempT.length - 1; i >= -1; i--) {
            if (i == -1 || tempT[i] == c) {
                return i;
            } else {
                continue;
            }
        }
        return 0;
    }

}
