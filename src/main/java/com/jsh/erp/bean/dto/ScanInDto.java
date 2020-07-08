package com.jsh.erp.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ：stephen
 * @date ：Created in 2020/7/6 15:12
 * @description：TODO
 */
@Data
public class ScanInDto {
    @NotNull
    private Long id;
    @NotNull
    private Integer number;
    @NotNull
    private String serialNumber;
    @NotNull
    private Long depotheadId;
    @NotNull
    private Long materialId;
}
