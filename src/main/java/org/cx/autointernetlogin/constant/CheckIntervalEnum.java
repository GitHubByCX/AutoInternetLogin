package org.cx.autointernetlogin.constant;

/**
 * 检测间隔枚举
 */
public enum CheckIntervalEnum {
    FIVE_SEC(0, "5秒", 1000L * 5),
    TEN_SEC(1, "10秒", 1000L * 10),
    ONE_MIN(2, "1分钟", 1000L * 60),
    FIVE_MIN(3, "5分钟", 1000L * 60 * 5),
    TEN_MIN(4, "10分钟", 1000L * 60 * 10),
    ;

    private final int index;
    private final String text;
    private final long millis;

    private CheckIntervalEnum(int index, String text, long millis) {
        this.index = index;
        this.text = text;
        this.millis = millis;
    }

    /**
     * 获取枚举下标
     *
     * @return 下标值
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取枚举显示文本
     *
     * @return 显示文本
     */
    public String getText() {
        return text;
    }

    /**
     * 获取枚举间隔时间
     *
     * @return 间隔时间
     */
    public long getMillis() {
        return millis;
    }

    /**
     * 根据传入的 index 值查找对应的枚举, 未找到时返回 null.
     *
     * @param index 目标 index 值
     * @return 目标 index 值对应的枚举
     */
    public static CheckIntervalEnum query(int index) {
        if (index > -1) {
            CheckIntervalEnum[] values = CheckIntervalEnum.values();
            for (CheckIntervalEnum result : values) {
                if (result.getIndex() == index) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 根据传入的 text 值查找对应的枚举, 未找到时返回 null.
     *
     * @param text 目标 text 值
     * @return 目标 text 值对应的枚举
     */
    public static CheckIntervalEnum query(String text) {
        if (text != null) {
            CheckIntervalEnum[] values = CheckIntervalEnum.values();
            for (CheckIntervalEnum result : values) {
                if (result.getText().equals(text)) {
                    return result;
                }
            }
        }
        return null;
    }

}
