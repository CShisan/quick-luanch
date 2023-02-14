package com.quick.common.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
public class RelationUtil {
    /**
     * 是否拥有key  或者  key对应node的父node的key
     */
    public static boolean hasKey(List<String> owns, List<String> required, Map<String, Relation> relation) {
        List<String> fullNodeKeys = fullNodeArgs(required, relation, Relation::getKey);
        return fullNodeKeys.retainAll(owns);
    }

    /**
     * 获取关系中的所有节点arg
     */
    public static <T> List<T> fullNodeArgs(List<T> args, Map<T, Relation> relationMap, Function<Relation, T> function) {
        if (CheckUtil.isEmpty(args) || CheckUtil.isNull(relationMap)) {
            return new ArrayList<>();
        }
        // 遍历node获取关系中的所有key
        return args.stream().flatMap(item -> {
            List<T> full = new ArrayList<>();
            Relation relation = relationMap.get(item);
            LinkedList<Relation> queue = new LinkedList<>(Collections.singletonList(relation));
            while (!queue.isEmpty()) {
                Relation pop = queue.pop();
                if (CheckUtil.isNull(pop)) {
                    continue;
                }
                full.add(function.apply(pop));
                Optional.ofNullable(pop.relations).ifPresent(queue::addAll);
            }
            return full.stream();
        }).collect(Collectors.toList());
    }

    /**
     * 反转map的对应关系
     */
    public static <T> Map<T, Relation> reverse(Map<T, Relation> relationMap, Function<Relation, T> function) {
        if (CheckUtil.isNull(relationMap)) {
            return Collections.emptyMap();
        }
        // 构造reverseMap
        Map<T, Relation> reverse = relationMap.values().stream().collect(Collectors.toMap(function, relation -> {
            Relation copy = BeanUtil.convert(relation, Relation::new);
            copy.setRelationIds(new ArrayList<>());
            copy.setRelations(new ArrayList<>());
            return copy;
        }));
        // 填充reverseMap的对应关系
        LinkedList<Relation> queue = new LinkedList<>(reverse.values());
        while (!queue.isEmpty()) {
            Relation pop = queue.pop();
            T relationKey = function.apply(pop);
            List<Relation> relations = Optional.ofNullable(relationMap.get(relationKey))
                    .map(Relation::getRelations).orElse(new ArrayList<>());
            for (Relation relation : relations) {
                T reverseKey = function.apply(relation);
                Relation parent = reverse.get(reverseKey);
                parent.getRelationIds().add(pop.getId());
                parent.getRelations().add(pop);
            }
        }
        return reverse;
    }

    /**
     * id继承映射
     */
    public static Map<Long, Relation> idInherit(List<Relation> all) {
        return inheritFill(all);
    }

    /**
     * key继承映射
     */
    public static Map<String, Relation> keyInherit(List<Relation> all) {
        return inheritFill(all).values().stream().collect(Collectors.toMap(Relation::getKey, Function.identity()));
    }

    /**
     * 继承数据填充
     */
    public static Map<Long, Relation> inheritFill(List<Relation> all) {
        if (CheckUtil.isEmpty(all)) {
            return Collections.emptyMap();
        }
        // 添加root节点
        all.add(Relation.builder().id(0L).key("root").relationIds(new ArrayList<>()).relations(new ArrayList<>()).build());
        // 构造映射(筛选对应类型)
        Map<Long, Relation> relationMap = all.stream().collect(Collectors.toMap(Relation::getId, Function.identity()));

        for (Relation relation : relationMap.values()) {
            // 获取关系node
            List<Long> relationIds = Optional.ofNullable(relation.getRelationIds()).orElse(new ArrayList<>());
            List<Relation> relations = relationIds.stream().map(relationMap::get).filter(Objects::nonNull).collect(Collectors.toList());

            // 获取当前node
            Long id = relation.getId();
            Relation self = relationMap.get(id);
            Optional.ofNullable(self).ifPresent(item -> {
                item.setRelationIds(relationIds);
                item.setRelations(relations);
            });
        }
        return relationMap;
    }

    /**
     * id绑定映射
     */
    public static Map<Long, Relation> idBind(List<Relation> primary, Map<Long, List<Long>> bindMap, List<Relation> secondary) {
        return bindFill(primary, bindMap, secondary);
    }

    /**
     * key绑定映射
     */
    public static Map<String, Relation> keyBind(List<Relation> primary, Map<Long, List<Long>> bindMap, List<Relation> secondary) {
        return bindFill(primary, bindMap, secondary).values().stream().collect(Collectors.toMap(Relation::getKey, Function.identity()));
    }

    /**
     * 绑定数据填充
     */
    public static Map<Long, Relation> bindFill(List<Relation> primary, Map<Long, List<Long>> bindMap, List<Relation> secondary) {
        if (CheckUtil.anyEmpty(primary, secondary)) {
            return Collections.emptyMap();
        }
        // 构造对应关系映射
        Map<Long, Relation> secondaryMap = secondary.stream().collect(Collectors.toMap(Relation::getId, Function.identity()));
        Map<Long, List<Relation>> bindSecondaryMap = bindMap.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey, entry -> {
                    List<Long> bindIds = Optional.ofNullable(entry.getValue()).orElse(new ArrayList<>());
                    return bindIds.stream().map(secondaryMap::get).filter(Objects::nonNull).collect(Collectors.toList());
                }
        ));

        // 填充对应的关系
        return primary.stream().peek(item -> {
            Long id = item.getId();
            List<Relation> binds = Optional.ofNullable(bindSecondaryMap.get(id)).orElse(new ArrayList<>());
            List<Long> bindIds = binds.stream().map(Relation::getId).collect(Collectors.toList());
            item.setRelationIds(bindIds);
            item.setRelations(binds);
        }).collect(Collectors.toMap(Relation::getId, Function.identity()));
    }

    /**
     * 计算增多了的项
     *
     * @param sourceList 原来的列表(一般为数据库查询出来)
     * @param targetList 待改变的项的列表
     * @return 增加了的项的列表
     */
    public static <T> List<T> increaseList(List<T> sourceList, List<T> targetList) {
        List<T> targets = new ArrayList<>(targetList);
        targets.removeAll(sourceList);
        return targets;
    }

    /**
     * 计算减少了的项
     *
     * @param sourceList 原来的列表(一般为数据库查询出来)
     * @param targetList 待改变的项的列表
     * @return 减少了的项的列表
     */
    public static <T> List<T> reduceList(List<T> sourceList, List<T> targetList) {
        List<T> sources = new ArrayList<>(sourceList);
        sources.removeAll(targetList);
        return sources;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Relation implements Serializable {
        private Long id;
        private String key;
        private String name;
        private Integer type;
        private Boolean leaf;

        /**
         * 关系ids
         */
        private List<Long> relationIds;

        /**
         * 关系entity列表
         */
        private List<Relation> relations;
    }

    @FunctionalInterface
    public interface ThreeFunction<T, U, E, R> {
        /**
         * 应用
         *
         * @param t t
         * @param u u
         * @param e e
         * @return r
         */
        R apply(T t, U u, E e);
    }
}
