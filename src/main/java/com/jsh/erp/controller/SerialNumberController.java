package com.jsh.erp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.*;
import com.jsh.erp.exception.BusinessParamCheckingException;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.service.depot.DepotService;
import com.jsh.erp.service.depotHead.DepotHeadService;
import com.jsh.erp.service.depotItem.DepotItemService;
import com.jsh.erp.service.material.MaterialService;
import com.jsh.erp.service.serialNumber.SerialNumberService;
import com.jsh.erp.utils.BaseResponseInfo;
import com.jsh.erp.utils.StringUtil;
import com.sun.org.apache.xerces.internal.impl.dv.dtd.NOTATIONDatatypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @Author: cjl
 * @Date: 2019/1/22 10:29
 */
@RestController
public class SerialNumberController {
    private Logger logger = LoggerFactory.getLogger(SerialNumberController.class);

    @Resource
    private SerialNumberService serialNumberService;

    @Resource
    private MaterialService materialService;

    @Resource
    private DepotHeadService depotHeadService;

    @Resource
    private DepotItemService depotItemService;

    @Resource
    private DepotService depotService;
    /**
     * create by: cjl
     * description:
     *  检查序列号是否存在
     * create time: 2019/1/22 11:02
     * @Param: id
     * @Param: materialName
     * @Param: serialNumber
     * @Param: request
     * @return java.lang.Object
     */
    @PostMapping("/serialNumber/checkIsExist")
    @ResponseBody
    public Object checkIsExist(@RequestParam("id") Long id, @RequestParam("materialName") String materialName,
                               @RequestParam("serialNumber") String serialNumber, HttpServletRequest request) throws Exception{
        JSONObject result = ExceptionConstants.standardSuccess();
        if(StringUtil.isEmpty(serialNumber)){
            throw new BusinessParamCheckingException(ExceptionConstants.SERIAL_NUMBERE_NOT_BE_EMPTY_CODE,
                    ExceptionConstants.SERIAL_NUMBERE_NOT_BE_EMPTY_MSG);
        }
        serialNumberService.checkIsExist(id, materialName, serialNumber);
        return result;
    }
    /**
     * create by: cjl
     * description:
     *  新增序列号信息
     * create time: 2019/1/22 17:10
     * @Param: beanJson
     * @Param: request
     * @return java.lang.Object
     */
    @PostMapping("/serialNumber/addSerialNumber")
    @ResponseBody
    public Object addSerialNumber(@RequestParam("info") String beanJson)throws Exception{
        JSONObject result = ExceptionConstants.standardSuccess();
        SerialNumberEx sne= JSON.parseObject(beanJson, SerialNumberEx.class);
        serialNumberService.addSerialNumber(sne);
        return result;

    }
    /**
     * create by: cjl
     * description:
     *  修改序列号信息
     * create time: 2019/1/23 13:56
     * @Param: beanJson
     * @return java.lang.Object
     */
    @PostMapping("/serialNumber/updateSerialNumber")
    @ResponseBody
    public Object updateSerialNumber(@RequestParam("info") String beanJson)throws Exception{

        JSONObject result = ExceptionConstants.standardSuccess();
        SerialNumberEx sne= JSON.parseObject(beanJson, SerialNumberEx.class);
        serialNumberService.updateSerialNumber(sne);
        return result;

    }
    /**
     * create by: cjl
     * description:
     *批量添加序列号
     * create time: 2019/1/29 15:11
     * @Param: materialName
     * @Param: serialNumberPrefix
     * @Param: batAddTotal
     * @Param: remark
     * @return java.lang.Object
     */
    @PostMapping("/serialNumber/batAddSerialNumber")
    @ResponseBody
    public Object batAddSerialNumber(@RequestParam("materialName") String materialName, @RequestParam("serialNumberPrefix") String serialNumberPrefix,
                                     @RequestParam("batAddTotal") Integer batAddTotal,@RequestParam("remark") String remark)throws Exception{

        JSONObject result = ExceptionConstants.standardSuccess();
        serialNumberService.batAddSerialNumber(materialName,serialNumberPrefix,batAddTotal,remark);
        return result;

    }
    /**
     * create by: qiankunpingtai
     * website：https://qiankunpingtai.cn
     * description:
     *  逻辑删除序列号信息
     * create time: 2019/3/27 17:43
     * @Param: ids
     * @return java.lang.Object
     */
    @RequestMapping(value = "/serialNumber/batchDeleteSerialNumberByIds")
    public Object batchDeleteSerialNumberByIds(@RequestParam("ids") String ids) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        int i= serialNumberService.batchDeleteSerialNumberByIds(ids);
        if(i<1){
            logger.error("异常码[{}],异常提示[{}],参数,ids[{}]",
                    ExceptionConstants.SERIAL_NUMBERE_DELETE_FAILED_CODE,ExceptionConstants.SERIAL_NUMBERE_DELETE_FAILED_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.SERIAL_NUMBERE_DELETE_FAILED_CODE,
                    ExceptionConstants.SERIAL_NUMBERE_DELETE_FAILED_MSG);
        }
        return result;
    }

    /**
     * 根据订单编号和商品id统计序列号数量
     * @param depotheadId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/serialNumber/depotheadId")
    public Object countByDepothead(@NotNull @RequestParam("depotheadId") Long depotheadId, @NotNull @RequestParam("materialId") Long materialId) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        int i= serialNumberService.countByDepothead(depotheadId, materialId);
        res.code = 200;
        res.data = i;
        return res;
    }

    /**
     * 根据序列号获取商品信息和库存信息
     * @param number
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/serialNumber/materialInfo")
    public Object materialInfo(@NotNull @RequestParam("number") String number) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        /**
         * 1.获取基本信息
         */
        SerialNumber serialNumber= serialNumberService.findByNumber(number);
        map.put("serialNumber", serialNumber);
        try {
            /**
             * 2.获取商品信息
             */
            if(serialNumber != null){
                Material material = materialService.selectById(serialNumber.getMaterialId());
                map.put("material", material);
                DepotHead depotHead = depotHeadService.selectById(serialNumber.getDepotheadId());
                if(material != null && depotHead!= null){
                    DepotItem depotItem = depotItemService.getByHiAndMi(material.getId(), depotHead.getId());
                    /**
                     * 获取仓库信息
                     */
                    if(depotItem.getDepotid() != null){
                        Depot depot = depotService.getDepot(depotItem.getDepotid());
                        map.put("depot", depot);
                    }
                }
            }

            res.code = 200;
            res.data = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }

        return res;
    }
}
