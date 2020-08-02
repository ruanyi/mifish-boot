package com.ruanyi.mifish.common.model;

import java.util.Objects;

/**
 * Description:
 * <p>
 * 描述id跟名称的组合，在下拉框配置中
 *
 * @author: rls
 * @Date: 2019-02-19 16:19
 */
public final class IdAndName {

    /**
     * id
     */
    private long id;

    /**
     * name
     */
    private String name;

    /**
     * getId
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * setId
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * setName
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * of
     *
     * @param id
     * @param name
     * @return
     */
    public static IdAndName of(long id, String name) {
        IdAndName idAndName = new IdAndName();
        idAndName.id = id;
        idAndName.name = name;
        return idAndName;
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
        IdAndName idAndName = (IdAndName) o;
        return id == idAndName.id && Objects.equals(name, idAndName.name);
    }

    /**
     * hashCode
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
