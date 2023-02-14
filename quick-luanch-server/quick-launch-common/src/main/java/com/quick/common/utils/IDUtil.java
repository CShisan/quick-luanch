package com.quick.common.utils;

import java.util.Random;

/**
 * @author CShisan
 */
public class IDUtil {
    /**
     * 偏移51位(用于生成17位ID)
     */
    private static final int ID_LENGTH_17_OFFSET = 51;
    /**
     * 偏移58位(用于生成19位ID)
     */
    private static final int ID_LENGTH_19_OFFSET = 58;
    /**
     * 管理员ID前缀
     */
    private static final int TYPE_ADMIN = 16;
    /**
     * 用户ID前缀
     */
    private static final int TYPE_USER = 18;
    /**
     * 业务ID前缀
     */
    private static final int TYPE_SERVICE = 21;

    public static long uid() {
        return create19(TYPE_USER);
    }

    public static long adminId() {
        return create19(TYPE_ADMIN);
    }

    public static long serviceId() {
        return create19(TYPE_SERVICE);
    }

    /**
     * 生成长度为17的ID
     *
     * @see IDUtil#create(int, int, int)
     */
    private static long create17(int prefixConstant) {
        int random = new Random().nextInt(90) + 10;
        return create(prefixConstant, ID_LENGTH_17_OFFSET, random);
    }

    /**
     * 生成长度为19的ID
     *
     * @see IDUtil#create(int, int, int)
     */
    private static long create19(int prefixConstant) {
        int random = new Random().nextInt(9000) + 1000;
        return create(prefixConstant, ID_LENGTH_19_OFFSET, random);
    }

    /**
     * 根据前缀常量左移58位再与(时间戳+随机数)按位与得到id
     * 由于超过前端Number类型的17位,所以要处理精度丢失问题
     * 方案1：@JsonSerialize(using = ToStringSerializer.class) 和 @Schema(type = "string")
     * 方案2: 手动转String
     *
     * @param prefixConstant 前缀常量
     * @param offset         偏移量
     * @param random         随机数
     * @return id
     */
    private static long create(int prefixConstant, int offset, int random) {
        prefixConstant = Math.max(prefixConstant, 16);
        prefixConstant = Math.min(prefixConstant, 31);
        // 前缀常量左移
        long prefix = (long) prefixConstant << offset;

        // 时间戳+ 随机数
        String time = String.valueOf(System.currentTimeMillis());
        time = time.concat(String.valueOf(random));

        return prefix | Long.parseLong(time);
    }

    /*
        16    ---> 31       10000 ---> 11111
        1651059251313  -->  当前时间戳
          31536000000  -->  一年增长的时间戳

        业务ID生成
        时间戳          随机数
        1651059251313   1000
        9999999999999   9999
        111110000000000000000000000000000000000000000000000000000000000
        000000101100011010001010111100001011101100010100000000000000000
        9035141660703064000   31前缀+时间戳最大值+随机数9999的长度19,刚好是Long类型最大长度

        UID生成
        时间戳          随机数
        1651059251313     10
        9999999999999     99
        11111000000000000000000000000000000000000000000000000000
        00000011100011010111111010100100110001100111111111111111
        70805794224242690  31前缀+时间戳最大值+随机数99的长度17,刚好是前端Number最大长度


        72057594037927940
     */
}
