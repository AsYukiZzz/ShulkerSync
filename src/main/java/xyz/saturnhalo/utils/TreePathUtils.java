package xyz.saturnhalo.utils;

/**
 * 层级标签处理工具类
 */
public class TreePathUtils {

    // 层级分隔符
    private static final String SEPARATOR = "/";

    /**
     * 创建标签路径（根节点）
     *
     * @param curId 当前节点 Id
     * @return 当前节点标签路径
     */
    public static String buildPath(Long curId) {
        // 1. 空值检测
        if (curId == null) {
            throw new IllegalArgumentException("当前节点 Id 不能为空值");
        }

        // 2. 返回结果
        return SEPARATOR + curId + SEPARATOR;
    }

    /**
     * 创建路径标签（子节点）
     *
     * @param curId      当前节点 Id
     * @param parentPath 当前节点父节点 Path
     * @return 当前节点标签路径
     */
    public static String buildPath(Long curId, String parentPath) {
        // 1. 空值检测
        if (curId == null) {
            throw new IllegalArgumentException("当前节点 Id 不能为空值");
        }

        if (parentPath == null || parentPath.isBlank()) {
            throw new IllegalArgumentException("父节点 Path 不能为空值，或去除空格后不能为空值");
        }

        // 2. 拼接返回
        return parentPath + curId + SEPARATOR;
    }
}