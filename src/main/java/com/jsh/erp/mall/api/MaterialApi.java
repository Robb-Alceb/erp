package com.jsh.erp.mall.api;

import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.service.CommonQueryManager;
import com.jsh.erp.service.material.MaterialService;
import com.jsh.erp.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jsh.erp.utils.ResponseJsonUtil.returnJson;

/**
 * @author ：stephen
 * @date ：Created in 2020/7/1 13:57
 * @description：TODO
 */
@RestController
@RequestMapping("/api/material")
public class MaterialApi {
    @Autowired
    private MaterialService materialService;
    @Resource
    private CommonQueryManager configResourceManager;

    @GetMapping("/list")
    public Object list(String apiName,
                       @RequestParam(value = Constants.PAGE_SIZE, required = false) Integer pageSize,
                       @RequestParam(value = Constants.CURRENT_PAGE, required = false) Integer currentPage,
                       @RequestParam(value = Constants.SEARCH, required = false) String search,
                       HttpServletRequest request) throws Exception {
        Map<String, String> parameterMap = ParamUtils.requestToMap(request);
        parameterMap.put(Constants.SEARCH, search);
        PageQueryInfo queryInfo = new PageQueryInfo();
        Map<String, Object> objectMap = new HashMap<String, Object>();
        if (pageSize != null && pageSize <= 0) {
            pageSize = 10;
        }
        String offset = ParamUtils.getPageOffset(currentPage, pageSize);
        if (StringUtil.isNotEmpty(offset)) {
            parameterMap.put(Constants.OFFSET, offset);
        }
        List<?> list = configResourceManager.select(apiName, parameterMap);
        objectMap.put("page", queryInfo);
        if (list == null) {
            queryInfo.setRows(new ArrayList<Object>());
            queryInfo.setTotal(BusinessConstants.DEFAULT_LIST_NULL_NUMBER);
            return returnJson(objectMap, "查找不到数据", ErpInfo.OK.code);
        }
        queryInfo.setRows(list);
        queryInfo.setTotal(configResourceManager.counts(apiName, parameterMap));
        return queryInfo;
    }
}
