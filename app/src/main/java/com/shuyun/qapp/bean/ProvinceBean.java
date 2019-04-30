package com.shuyun.qapp.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

import lombok.Data;

/**
 * @Package: com.shuyun.qapp.bean
 * @ClassName: ProvinceBean
 * @Description: 省市区bean
 * @Author: ganquan
 * @CreateDate: 2019/4/29 16:29
 */
@Data
public class ProvinceBean implements IPickerViewData {
    /**
     * code : 11
     * name : 北京市
     * childs : [{"code":"110100","name":"北京市","childs":[{"code":"110101","name":"东城区"},{"code":"110102","name":"西城区"},{"code":"110105","name":"朝阳区"},{"code":"110106","name":"丰台区"},{"code":"110107","name":"石景山区"},{"code":"110108","name":"海淀区"},{"code":"110109","name":"门头沟区"},{"code":"110111","name":"房山区"},{"code":"110112","name":"通州区"},{"code":"110113","name":"顺义区"},{"code":"110114","name":"昌平区"},{"code":"110115","name":"大兴区"},{"code":"110116","name":"怀柔区"},{"code":"110117","name":"平谷区"},{"code":"110228","name":"密云县"},{"code":"110229","name":"延庆县"}]}]
     */
    private String code;
    private String name;
    private List<ProvinceBean> childs;

    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
