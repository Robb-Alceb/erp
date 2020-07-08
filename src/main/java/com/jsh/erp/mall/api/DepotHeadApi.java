package com.jsh.erp.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.DepotHeadVo4Body;
import com.jsh.erp.service.depotHead.DepotHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：stephen
 * @date ：Created in 2020/7/1 13:58
 * @description：TODO
 */
@RestController
@RequestMapping("/api/depothead")
public class DepotHeadApi {
    @Autowired
    private DepotHeadService depotHeadService;

    /**
     * 新增单据主表及单据子表信息
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/addDepotHeadAndDetail")
    public Object addDepotHeadAndDetail(@RequestBody DepotHeadVo4Body body, HttpServletRequest request) throws  Exception{
        JSONObject result = ExceptionConstants.standardSuccess();
        String beanJson = body.getInfo();
        String inserted = body.getInserted();
        String deleted = body.getDeleted();
        String updated = body.getUpdated();
        Long tenantId = Long.parseLong(request.getHeader("tenantId"));
        String number = depotHeadService.addDepotHeadAndDetail(beanJson, inserted, deleted, updated, tenantId, request);
        result.put("number", number);
        return result;
    }

    /**
     * 获取可用编号
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/buildNumber")
    public Object buildNumber(HttpServletRequest request)throws Exception {
        return depotHeadService.buildOnlyNumber();
    }
}
