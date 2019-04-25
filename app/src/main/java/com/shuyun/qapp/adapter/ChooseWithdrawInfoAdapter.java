package com.shuyun.qapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.ui.mine.AddWithdrawInfoActivity;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择提现信息adapter
 */

public class ChooseWithdrawInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<MineBean.WithdrawBaseBean> withdrawBaseBeanList;

    public ChooseWithdrawInfoAdapter(List<MineBean.WithdrawBaseBean> withdrawBaseBeanList, Context mContext) {
        this.withdrawBaseBeanList = withdrawBaseBeanList;
        this.mContext = mContext;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.item_choose_withdraw_info, parent, false);
        RecyclerView.ViewHolder holder = new MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MineBean.WithdrawBaseBean withdrawBaseBean = withdrawBaseBeanList.get(position);
        try {
            final Long bankType = withdrawBaseBean.getBankType();
            String title = withdrawBaseBean.getTitle().replaceAll(" ", "");
            Long status = withdrawBaseBean.getStatus();
            boolean isselected = withdrawBaseBean.isSelected();
            String[] args = title.split("[|]");

            if (bankType == 1) {//支付宝
                ((MyViewHolder) holder).ivLogo.setBackgroundResource(R.mipmap.alipay_logo);
                ((MyViewHolder) holder).tvTitle.setText("支付宝");
                if (status == 3) { //已绑定支付宝提现信息
                    String str = args[1] + " | " + args[2];
                    ((MyViewHolder) holder).tvContent.setText(str);
                    ((MyViewHolder) holder).ivMore.setVisibility(View.GONE);
                    ((MyViewHolder) holder).ivSelect.setVisibility(View.VISIBLE);
                    if (isselected) {
                        ((MyViewHolder) holder).ivSelect.setBackgroundResource(R.mipmap.checked_logo_add_info);
                    } else {
                        ((MyViewHolder) holder).ivSelect.setBackgroundResource(R.mipmap.no_checked_logo_add_info);
                    }
                    ((MyViewHolder) holder).rlWithdrawInfo.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            //设置选中
                            setSelect(withdrawBaseBean);
                            int position = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(((MyViewHolder) holder).rlWithdrawInfo, position);
                        }
                    });
                } else { //未绑定支付宝提现信息
                    ((MyViewHolder) holder).tvContent.setText("请先完善支付宝信息");
                    ((MyViewHolder) holder).ivMore.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).ivSelect.setVisibility(View.GONE);
                    ((MyViewHolder) holder).rlWithdrawInfo.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            //完善支付宝信息
                            mContext.startActivity(new Intent(mContext, AddWithdrawInfoActivity.class));
                        }
                    });
                }
            } else if (bankType == 2) { //微信
                ((MyViewHolder) holder).ivLogo.setBackgroundResource(R.mipmap.wx_logo);
                ((MyViewHolder) holder).tvTitle.setText("微信");
                if (status == 3) {//已绑定微信提现信息
                    String str = args[1];
                    ((MyViewHolder) holder).tvContent.setText(str);
                    ((MyViewHolder) holder).ivMore.setVisibility(View.GONE);
                    ((MyViewHolder) holder).ivSelect.setVisibility(View.VISIBLE);
                    if (isselected) {
                        ((MyViewHolder) holder).ivSelect.setBackgroundResource(R.mipmap.checked_logo_add_info);
                    } else {
                        ((MyViewHolder) holder).ivSelect.setBackgroundResource(R.mipmap.no_checked_logo_add_info);
                    }
                    ((MyViewHolder) holder).rlWithdrawInfo.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            //设置选中
                            setSelect(withdrawBaseBean);
                            int position = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(((MyViewHolder) holder).rlWithdrawInfo, position);
                        }
                    });
                } else { //未绑定微信提现信息
                    ((MyViewHolder) holder).tvContent.setText("请先绑定微信");
                    ((MyViewHolder) holder).ivMore.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).ivSelect.setVisibility(View.GONE);
                    ((MyViewHolder) holder).rlWithdrawInfo.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            //拉起微信
                            SaveUserInfo.getInstance(mContext).setUserInfo("bind_weixin_addwithdraw", "add");
                            wxLogin();
                        }
                    });
                }
            }

            //隐藏下划线
            if (position == withdrawBaseBeanList.size() - 1) {
                ((MyViewHolder) holder).viewLine.setVisibility(View.GONE);
            }

        } catch (Exception e) {

        }
    }

    //选中按钮
    private void setSelect(MineBean.WithdrawBaseBean withdrawBaseBean) {
        for (int i = 0; i < withdrawBaseBeanList.size(); i++) {
            withdrawBaseBeanList.get(i).setSelected(false);
        }
        withdrawBaseBean.setSelected(true);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (withdrawBaseBeanList == null) ? 0 : withdrawBaseBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.iv_more)
        ImageView ivMore;
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.view_line)
        View viewLine;
        @BindView(R.id.rl_withdraw_info)
        RelativeLayout rlWithdrawInfo;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    OnItemClickListener mOnItemClickListener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitsener(OnItemClickListener mOnItemClickLitsener) {
        this.mOnItemClickListener = mOnItemClickLitsener;
    }

    /**
     * 拉起微信授权页,调用微信登录界面
     */
    private void wxLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        SykscApplication.mWxApi.sendReq(req);
    }

}
