package com.skmj.server.area;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lc
 */
@Data
public class AreaTree implements Serializable {

    private Long id;
    private String label;
    private String value;
    private List<AreaTree> children;
}
