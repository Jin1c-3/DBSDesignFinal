package com.yutech.back.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 1
 * </p>
 *
 * @author Jin1c-3
 * @since 2023-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Sysdiagrams implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField("principal_id")
    private Integer principalId;

    @TableId(value = "diagram_id", type = IdType.AUTO)
    private Integer diagramId;

    @TableField("version")
    private Integer version;

    @TableField("definition")
    private byte[] definition;


}
