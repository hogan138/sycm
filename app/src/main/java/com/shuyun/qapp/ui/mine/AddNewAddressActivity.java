package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.AddressListBeans;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.ProvinceBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.net.UserAddressBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 新增收货地址
 */
public class AddNewAddressActivity extends BaseActivity implements View.OnClickListener, OnRemotingCallBackListener<Object> {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.et_address_deatail)
    EditText etAddressDeatail;
    @BindView(R.id.tv_add_address)
    TextView tvAddAddress;
    @BindView(R.id.iv_default)
    ImageView ivDefault;

    private boolean is_default = false;

    private Context mContext;

    private List<ProvinceBean> options1Items = new ArrayList<>(); //省
    private List<List<ProvinceBean>> options2Items = new ArrayList<>(); //市
    private List<List<List<ProvinceBean>>> options3Items = new ArrayList<>(); //区

    String province = ""; //省代码
    String city = ""; //市代码
    String county = ""; //区县代码
    String provinceName = ""; //省份名称
    String cityName = ""; //城市名称
    String countyName = ""; //区县名称

    private Long isDefault = 0L; //是否默认

    private String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;

        tvCommonTitle.setText("添加收货地址");

        ivBack.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);
        ivDefault.setOnClickListener(this);
        rlAddress.setOnClickListener(this);

        from = getIntent().getStringExtra("from");
        if (from.equals("modify")) {
            //修改用户地址
            AddressListBeans addressListBeans = getIntent().getParcelableExtra("address");
            etName.setText(addressListBeans.getUserName());
            etNumber.setText(addressListBeans.getUserPhone());
            StringBuilder sb = new StringBuilder();
            sb.append(addressListBeans.getProvinceName());
            if (!StringUtil.isBlank(addressListBeans.getCityName())) {
                sb.append(" ").append(addressListBeans.getCityName());
            }
            if (!StringUtil.isBlank(addressListBeans.getCountyName())) {
                sb.append(" ").append(addressListBeans.getCountyName());
            }
            String address = sb.toString();
            tvAddress.setText(address);
            tvAddress.setTextColor(getResources().getColor(R.color.color_1));
            etAddressDeatail.setText(addressListBeans.getDetail());
            Long Default = addressListBeans.getIsDefault();
            if (Default == 1) {
                ivDefault.setImageResource(R.mipmap.default_checked_logo);
                is_default = true;
                isDefault = 1L;
            } else {
                ivDefault.setImageResource(R.mipmap.default_no_checked_logo);
                is_default = false;
                isDefault = 0L;
            }
            province = addressListBeans.getProvince();
            city = addressListBeans.getCity();
            county = addressListBeans.getCounty();
            provinceName = addressListBeans.getProvinceName();
            cityName = addressListBeans.getCityName();
            countyName = addressListBeans.getCountyName();

        }


        //获取城市列表
        loadProvinceInfo();
    }

    /**
     * 获取城市列表
     */
    public void loadProvinceInfo() {
        RemotingEx.doRequest("provinceInfo", ApiServiceBean.getProvinceList(), null, this);
    }

    /**
     * 增加用户地址
     */
    public void addAddress(UserAddressBean userAddressBean) {
        String inputbean = JSON.toJSONString(userAddressBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        RemotingEx.doRequest("addAddress", ApiServiceBean.addAddress(), new Object[]{body}, this);
    }

    /**
     * 修改用户地址
     */
    public void modifyAddress(UserAddressBean userAddressBean) {
        String inputbean = JSON.toJSONString(userAddressBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        RemotingEx.doRequest("addAddress", ApiServiceBean.modifyAddress(), new Object[]{body}, this);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_add_new_address;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_address:
                //省市区选择器
                chooseProvince();
                break;
            case R.id.iv_default:
                if (!is_default) {
                    ivDefault.setImageResource(R.mipmap.default_checked_logo);
                    is_default = true;
                    isDefault = 1L;
                } else {
                    ivDefault.setImageResource(R.mipmap.default_no_checked_logo);
                    is_default = false;
                    isDefault = 0L;
                }
                break;
            case R.id.tv_add_address:
                //确定
                enter();
                break;
            default:
                break;
        }
    }

    //确定
    private void enter() {
        String userName = etName.getText().toString();
        String userPhone = etNumber.getText().toString();
        String detail = etAddressDeatail.getText().toString();

        UserAddressBean userAddressBean = new UserAddressBean();
        userAddressBean.setUserName(userName);
        userAddressBean.setUserPhone(userPhone);
        userAddressBean.setIsDefault(isDefault);
        userAddressBean.setProvince(province);
        userAddressBean.setCity(city);
        userAddressBean.setCounty(county);
        userAddressBean.setProvinceName(provinceName);
        userAddressBean.setCityName(cityName);
        userAddressBean.setCountyName(countyName);
        userAddressBean.setDetail(detail);

        if (from.equals("add")) {
            //添加用户地址
            addAddress(userAddressBean);
        } else if (from.equals("modify")) {
            //修改用户地址
            modifyAddress(userAddressBean);
        }

    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }
        if (action.equals("provinceInfo")) {
            options1Items.clear();
            options2Items.clear();
            options3Items.clear();

            //省市区数据
            List<ProvinceBean> provinceBeanList = (List<ProvinceBean>) response.getDat();
            for (int i = 0; i < provinceBeanList.size(); i++) {
                ProvinceBean bean = provinceBeanList.get(i);
                options1Items.add(bean);
                //取出省份对应的市、
                List<ProvinceBean> list = bean.getChilds();
                if (list == null || list.isEmpty()) {
                    list = new ArrayList<>();
                    ProvinceBean item = new ProvinceBean();
                    item.setCode("");
                    item.setName("");
                    list.add(item);
                }
                options2Items.add(list);

                //添加市对应的区
                List<List<ProvinceBean>> childs = new ArrayList<>();
                for (ProvinceBean parent : list) {
                    List<ProvinceBean> child = parent.getChilds();
                    if (child == null || child.isEmpty()) {
                        child = new ArrayList<>();
                        ProvinceBean item = new ProvinceBean();
                        item.setCode("");
                        item.setName("");
                        child.add(item);
                    }
                    childs.add(child);
                }
                options3Items.add(childs);
            }
        } else if (action.equals("addAddress")) {
            finish();
        }
    }

    //省市区选择器
    private void chooseProvince() {
        //条件选择器
        OptionsPickerView optionsPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                province = options1Items.get(options1).getCode();
                city = options2Items.get(options1).get(option2).getCode();
                county = options3Items.get(options1).get(option2).get(options3).getCode();
                provinceName = options1Items.get(options1).getName();
                cityName = options2Items.get(options1).get(option2).getName();
                countyName = options3Items.get(options1).get(option2).get(options3).getName();

                tvAddress.setText(provinceName + " " + cityName + " " + countyName);
                tvAddress.setTextColor(Color.parseColor("#333333"));
            }
        }).setCancelColor(Color.parseColor("#333333")).isRestoreItem(true).build();
        optionsPickerView.setPicker(options1Items, options2Items, options3Items);
        optionsPickerView.show();
    }
}
