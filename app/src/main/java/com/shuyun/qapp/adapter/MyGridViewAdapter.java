package com.shuyun.qapp.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.ProductListBean;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.OvalImageView;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

/**
 * 项目名称：QMGJ
 * 创建人：gq
 * 创建日期：2018/6/8 11:44
 * GridView加载数据adapter
 */
public class MyGridViewAdapter extends BaseAdapter implements CommonPopupWindow.ViewInterface {

    private List<ProductListBean> listData;
    private LayoutInflater inflater;
    private Context context;
    private int mIndex;//页数下标，表示第几页，从0开始
    private int mPagerSize;//每页显示的最大数量
    RelativeLayout rlIntegralDraw;
    String LongPicture, title, con;

    public MyGridViewAdapter(Context context, List<ProductListBean> listData, int mIndex, int mPagerSize, RelativeLayout rlIntegralDraw) {
        this.context = context;
        this.listData = listData;
        this.mIndex = mIndex;
        this.mPagerSize = mPagerSize;
        this.rlIntegralDraw = rlIntegralDraw;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 先判断数据集的大小是否足够显示满本页？listData.size() > (mIndex + 1)*mPagerSize
     * 如果满足，则此页就显示最大数量mPagerSize的个数
     * 如果不够显示每页的最大数量，那么剩下几个就显示几个 (listData.size() - mIndex*mPagerSize)
     */
    @Override
    public int getCount() {
        return listData.size() > (mIndex + 1) * mPagerSize ? mPagerSize : (listData.size() - mIndex * mPagerSize);
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position + mIndex * mPagerSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex * mPagerSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview, parent, false);
            holder = new ViewHolder();
            holder.proName = (TextView) convertView.findViewById(R.id.proName);
            holder.imgUrl = (RoundImageView) convertView.findViewById(R.id.imgUrl);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //重新确定position（因为拿到的是总的数据源，数据源是分页加载到每页的GridView上的，为了确保能正确的点对不同页上的item）
        final int pos = position + mIndex * mPagerSize;//假设mPagerSize=8，假如点击的是第二页（即mIndex=1）上的第二个位置item(position=1),那么这个item的实际位置就是pos=9
        ProductListBean bean = listData.get(pos);
        holder.proName.setText(bean.getName());
        ImageLoaderManager.LoadImage(context, bean.getMainImage(), holder.imgUrl, R.mipmap.zw01);
        //添加item监听
        convertView.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
//                LongPicture = listData.get(pos).getLongImage();
//                title = listData.get(pos).getName();
//                con = listData.get(pos).getPurpose();
//                showpopuwindows();
            }
        });
        return convertView;
    }

    private CommonPopupWindow popupWindow;

    public void showpopuwindows() {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(context).inflate(R.layout.open_box_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.open_box_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(false)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAtLocation(rlIntegralDraw, Gravity.CENTER, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.open_box_popupwindow:
                TextView tv_title = view.findViewById(R.id.tv_title);
                tv_title.setText(title);
                TextView tv_content = view.findViewById(R.id.tv_content);
                tv_content.setText(con);
                OvalImageView iv_box = view.findViewById(R.id.iv_box);
                ImageLoaderManager.LoadImage(context, LongPicture, iv_box, R.mipmap.zw01);
                ImageView iv_close = view.findViewById(R.id.iv_close);
                iv_close.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }

    class ViewHolder {
        private TextView proName;
        private RoundImageView imgUrl;
    }
}
