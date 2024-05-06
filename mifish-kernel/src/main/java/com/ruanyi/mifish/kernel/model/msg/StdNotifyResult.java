package com.ruanyi.mifish.kernel.model.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2019-09-04 17:29
 */
@Setter
@Getter
public class StdNotifyResult {

    /** msgId */
    @JsonProperty("msg_id")
    private String msgId;

    /** code */
    @JsonProperty("code")
    private String code;

    /** message */
    @JsonProperty("message")
    private String message;

    /** success */
    @JsonProperty("success")
    private boolean success;

    /** items */
    private List<Map<String, Object>> items = new ArrayList<>();

    /**
     * addAllItems
     *
     * @param items
     * @return
     */
    public StdNotifyResult addAllItems(List<Map<String, Object>> items) {
        if (items != null && !items.isEmpty()) {
            this.items.addAll(items);
        }
        return this;
    }

    /**
     * isItemsEmpty
     *
     * @return
     */
    public boolean isItemsEmpty() {
        return ((this.items == null) || this.items.isEmpty());
    }

    /**
     * addItem
     *
     * @param item
     * @return
     */
    public StdNotifyResult addItem(Map<String, Object> item) {
        if (item != null) {
            this.items.add(item);
        }
        return this;
    }

    /**
     * toBeautyFiles
     *
     * @return
     */
    public List<OutputFile> toOutputFiles() {
        if (isItemsEmpty()) {
            return Lists.newArrayList();
        }
        List<OutputFile> ofs = new ArrayList<>(this.items.size());
        for (Map<String, Object> item : this.items) {
            OutputFile bf = from(item, this.msgId);
            if (bf != null) {
                ofs.add(bf);
            }
        }
        return ofs;
    }

    /**
     * toOutputFiles
     *
     * @param cmd
     * @return
     */
    public List<OutputFile> toOutputFiles(String cmd) {
        if (isItemsEmpty()) {
            return Lists.newArrayList();
        }
        List<OutputFile> ofs = new ArrayList<>(this.items.size());
        for (Map<String, Object> item : this.items) {
            OutputFile bf = from(item, this.msgId);
            if (bf != null) {
                if (StringUtils.isNotBlank(cmd)) {
                    bf.setCmd(cmd);
                }
                ofs.add(bf);
            }
        }
        return ofs;
    }

    /**
     * toFailureFiles
     *
     * @param cmd
     * @return
     */
    public OutputFile toFailureFile(String cmd) {
        OutputFile fof = new OutputFile();
        fof.setCode(this.code);
        fof.setCmd(cmd);
        return fof;
    }

    /**
     * from
     *
     * @param item
     * @param msgId
     * @return
     */
    private static OutputFile from(Map<String, Object> item, String msgId) {
        if (item != null && !item.isEmpty()) {
            OutputFile of = new OutputFile();
            Object code = item.get("code");
            of.setCode(code + "");
            Map<String, Object> extra = (Map<String, Object>)item.get("extra");
            if (extra != null) {
                extra.put("msg_id", msgId);
            } else {
                extra = new HashMap<>();
                extra.put("msg_id", msgId);
            }
            of.setExtra(extra);
            of.setKey(item.get("key") + "");
            of.setBucket(item.get("bucket") + "");
            return of;
        }
        return null;
    }
}
