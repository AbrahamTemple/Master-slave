package com.spring.msadmin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class msOrder implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @NonNull
    private String name;

    @NonNull
    private String pid;

    @NonNull
    private Integer spend;

    @TableField(fill = FieldFill.INSERT)
    private Date created;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updated;

    @TableLogic//逻辑删除
    @TableField(fill = FieldFill.INSERT)
    private int deleted;
}
