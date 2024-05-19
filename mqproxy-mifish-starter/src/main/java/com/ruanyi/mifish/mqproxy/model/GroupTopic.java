package com.ruanyi.mifish.mqproxy.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-09-12 17:36
 */
public class GroupTopic implements Serializable, Comparable<GroupTopic> {

    /** group */
    private String group;

    /** topic */
    private String topic;

    /** priority */
    private int priority;

    /**
     * getGroup
     *
     * @return
     */
    public String getGroup() {
        return group;
    }

    /**
     * getTopic
     *
     * @return
     */
    public String getTopic() {
        return topic;
    }

    /**
     * getPriority
     * 
     * @return
     */
    public int getPriority() {
        return priority;
    }

    /**
     * equals
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupTopic that = (GroupTopic)o;
        return Objects.equals(group, that.group) && Objects.equals(topic, that.topic);
    }

    /**
     * hashCode
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(group, topic);
    }

    /**
     * toString
     *
     * @return
     */
    @Override
    public String toString() {
        return this.group + ":" + this.topic;
    }

    /**
     * build
     * 
     * @param group
     * @param topic
     * @return
     */
    public static GroupTopic build(String group, String topic) {
        checkArgument(StringUtils.isNotBlank(group), "group cannot be blank in GroupTopic");
        checkArgument(StringUtils.isNotBlank(topic), "topic cannot be blank in GroupTopic");
        GroupTopic groupTopic = new GroupTopic();
        groupTopic.group = group;
        groupTopic.topic = topic;
        return groupTopic;
    }

    /**
     * of
     *
     * @param content
     * @return
     */
    public static GroupTopic of(String content) {
        String[] gt = StringUtils.split(content, ":");
        if (gt == null || gt.length < 1) {
            return null;
        }
        GroupTopic groupTopic = new GroupTopic();
        if (gt.length == 1) {
            groupTopic.topic = gt[0];
            groupTopic.group = gt[0];
            groupTopic.priority = 5;
        } else if (gt.length == 2) {
            groupTopic.group = gt[0];
            groupTopic.topic = gt[1];
            groupTopic.priority = 5;
        } else if (gt.length > 2) {
            groupTopic.group = gt[0];
            groupTopic.topic = gt[1];
            groupTopic.priority = Integer.parseInt(gt[2]);
        }
        return groupTopic;
    }

    /**
     * parse2Map
     *
     * @param content
     * @return
     */
    public static Map<String, GroupTopic> parse2Map(String content) {
        String[] gts = StringUtils.split(content, ";");
        if (gts == null || gts.length < 1) {
            return new HashMap<>(0);
        }
        Map<String, GroupTopic> gtsmap = new HashMap<>(gts.length);
        for (String gt : gts) {
            GroupTopic groupTopic = of(gt);
            if (groupTopic != null) {
                gtsmap.put(gt, groupTopic);
            }
        }
        return gtsmap;
    }

    /**
     * parse2List
     *
     * @param content
     * @return
     */
    public static Set<GroupTopic> parse2Set(String content) {
        String[] gts = StringUtils.split(content, ";");
        if (gts == null || gts.length < 1) {
            return Sets.newHashSet();
        }
        Set<GroupTopic> gtSet = new HashSet<>(gts.length);
        for (String gt : gts) {
            GroupTopic groupTopic = of(gt);
            if (groupTopic != null) {
                gtSet.add(groupTopic);
            }
        }
        return gtSet;
    }

    /**
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(GroupTopic another) {
        return Integer.compare(this.priority, another.priority);
    }
}
