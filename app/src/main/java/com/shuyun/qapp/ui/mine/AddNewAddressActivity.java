package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.blankj.utilcode.util.KeyboardUtils;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.AddressListBeans;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.ProvinceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.RegularTool;
import com.shuyun.qapp.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 新增收货地址
 */
public class AddNewAddressActivity extends BaseActivity implements View.OnClickListener, OnRemotingCallBackListener {

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
    @BindView(R.id.tv_delete)
    TextView tvDelete;

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

    private Long id = 0L;//地址id

    //省市区默认选中
    private int selectItemOne = 0;
    private int selectItemTwo = 0;
    private int selectItemThree = 0;

    AddressListBeans userAddressBean;

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
        tvDelete.setOnClickListener(this);

        from = getIntent().getStringExtra("from");
        if (from.equals("modify")) {
            tvCommonTitle.setText("编辑收货地址");
            tvDelete.setVisibility(View.VISIBLE);
            //编辑收货地址
            AddressListBeans addressListBeans = getIntent().getParcelableExtra("address");
            etName.setText(addressListBeans.getUserName());
            etName.setSelection(addressListBeans.getUserName().length());
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

            id = Long.valueOf(addressListBeans.getId());

        }


        //获取城市列表
        loadProvinceInfo();
    }

    /**
     * 获取城市列表
     */
    public void loadProvinceInfo() {
        RemotingEx.doRequest("provinceInfo", RemotingEx.Builder().getProvinceList(), this);
    }

    /**
     * 增加用户地址
     */
    public void addAddress(AddressListBeans userAddressBean) {
        String inputbean = JSON.toJSONString(userAddressBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        RemotingEx.doRequest("addAddress", RemotingEx.Builder().addAddress(body), this);
    }

    /**
     * 修改用户地址
     */
    public void modifyAddress(AddressListBeans userAddressBean) {
        String inputbean = JSON.toJSONString(userAddressBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        RemotingEx.doRequest("modifyAddress", RemotingEx.Builder().modifyAddress(body), this);
    }

    /**
     * 删除用户地址
     */
    public void deleteAddress() {
        RemotingEx.doRequest("deleteAddress", RemotingEx.Builder().deleteAddress(id), this);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_add_new_address;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //隐藏键盘
                KeyboardUtils.hideSoftInput(this);
                finish();
                break;
            case R.id.rl_address:
                //设置默认选中项
                selectDefault();
                //隐藏键盘
                KeyboardUtils.hideSoftInput(this);
                //省市区选择器
                chooseProvince();
                break;
            case R.id.iv_default:
                //是否设置为默认地址
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
            case R.id.tv_delete:
                //删除收货地址
                deleteDialog();
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

        String userName = etName.getText().toString().trim();
        String userPhone = etNumber.getText().toString().trim();
        String detail = etAddressDeatail.getText().toString().trim();
        if (!EncodeAndStringTool.isStringEmpty(userName)) {
            if (!EncodeAndStringTool.isStringEmpty(userPhone)) {
                if (RegularTool.isMobileExact(userPhone)) {
                    if (!EncodeAndStringTool.isStringEmpty(province) || !EncodeAndStringTool.isStringEmpty(city) || !EncodeAndStringTool.isStringEmpty(county)) {
                        if (!EncodeAndStringTool.isStringEmpty(detail)) {

                            userAddressBean = new AddressListBeans();
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
                                userAddressBean.setId(id);
                                modifyAddress(userAddressBean);

                            }
                        } else {
                            ToastUtil.showToast(mContext, "请输入详细地址");
                        }
                    } else {
                        ToastUtil.showToast(mContext, "请选择所在地区");
                    }
                } else {
                    ToastUtil.showToast(mContext, "请输入正确的手机号码");
                }
            } else {
                ToastUtil.showToast(mContext, "请输入手机号码");
            }
        } else {
            ToastUtil.showToast(mContext, "请输入收货人姓名");
        }

    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse response) {
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
                //取出省份对应的市
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
            ToastUtil.showToast(mContext, "添加成功");
            finish();
        } else if (action.equals("modifyAddress")) {
            //修改用户地址
            if (!EncodeAndStringTool.isObjectEmpty(AppConst.getAddressListBeans())) {
                //若商品详情选择地址相同,则更新
                AddressListBeans addressListBeans = AppConst.getAddressListBeans();
                if (id.equals(addressListBeans.getId())) {
                    AppConst.setAddressListBeans(userAddressBean);
                }
            }
            ToastUtil.showToast(mContext, "修改成功");
            finish();
        } else if (action.equals("deleteAddress")) {
            if (!EncodeAndStringTool.isObjectEmpty(AppConst.getAddressListBeans())) {
                //若商品详情选择地址相同,则更新
                AddressListBeans addressListBeans = AppConst.getAddressListBeans();
                if (id.equals(addressListBeans.getId())) {
                    AppConst.setAddressListBeans(null);
                }
            }
            ToastUtil.showToast(mContext, "删除地址成功");
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
                tvAddress.setTextColor(ContextCompat.getColor(mContext, R.color.color_1));
            }
        }).setCancelColor(ContextCompat.getColor(mContext, R.color.color_1))
                .isRestoreItem(true)
                .setSelectOptions(selectItemOne, selectItemTwo, selectItemThree)
                .build();
        optionsPickerView.setPicker(options1Items, options2Items, options3Items);
        optionsPickerView.show();
    }

    //默认选中项
    private void selectDefault() {
        selectItemOne = 0;
        selectItemTwo = 0;
        selectItemThree = 0;
        for (int i = 0; i < options1Items.size(); i++) {
            if (province.equals(options1Items.get(i).getCode())) {
                //省
                selectItemOne = i;
                List<ProvinceBean> list = options2Items.get(i);
                for (int j = 0; j < list.size(); j++) {
                    if (city.equals(list.get(j).getCode())) {
                        //市
                        selectItemTwo = j;
                        List<ProvinceBean> list2 = options3Items.get(i).get(j);
                        for (int k = 0; k < list2.size(); k++) {
                            if (county.equals(list2.get(k).getCode())) {
                                //区
                                selectItemThree = k;
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    //删除弹框
    private void deleteDialog() {
        new CircleDialog.Builder(this)
                .setTitle("提示")
                .setText("你确定要删除吗？")
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#9B9B9B");
                    }
                })
                .setPositive("确定", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        //删除收货地址
                        deleteAddress();
                    }
                })
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.animStyle = R.style.popwin_anim_style;
                    }
                })
                .show();
    }
}
