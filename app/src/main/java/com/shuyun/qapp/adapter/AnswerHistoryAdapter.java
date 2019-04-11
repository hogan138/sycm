package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.LookAnswerResultBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.Base64Utils;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.NumberFormatUtil;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.RSAUtils;

import java.security.PrivateKey;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 答题历史适配器
 */

public class AnswerHistoryAdapter extends RecyclerView.Adapter<AnswerHistoryAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<LookAnswerResultBean.QuestionsBean> questionsBeans;

    public AnswerHistoryAdapter(Context context, List<LookAnswerResultBean.QuestionsBean> questionsBeans) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.questionsBeans = questionsBeans;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.answer_history_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (!EncodeAndStringTool.isListEmpty(questionsBeans) && !EncodeAndStringTool.isObjectEmpty(questionsBeans.get(position))) {
            LookAnswerResultBean.QuestionsBean questionsBean = questionsBeans.get(position);

            //题目index
            String index = NumberFormatUtil.formatInteger(position + 1);
            holder.tvIndex.setText("- 第" + index + "题 -");


            //图片题目主图
            Long type = questionsBean.getType();
            if (type == 2) {
                holder.ivMainImg.setVisibility(View.VISIBLE);
                GlideUtils.LoadImage1(context, questionsBean.getPicture(), holder.ivMainImg);
            } else {
                holder.ivMainImg.setVisibility(View.GONE);
            }

            if (!EncodeAndStringTool.isListEmpty(questionsBean.getOptions())) {
                List<LookAnswerResultBean.QuestionsBean.OptionsBean> optionsBeans = questionsBean.getOptions();

                //私钥解密
                try {
                    PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                    byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(questionsBean.getTitle()), private_key);
                    holder.tvQuestion2.setText(new String(decryptByte));//题目标题
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String oks = "";
                //私钥解密
                try {
                    PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                    byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(questionsBean.getOks()), private_key);
                    oks = new String(decryptByte);  //正确选项
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (questionsBean.getResult() == 1) { //回答正确
                    holder.ivResultLogo.setImageResource(R.mipmap.anwser_logo_right);
                } else if (questionsBean.getResult() == 0) {  //回答错误
                    holder.ivResultLogo.setImageResource(R.mipmap.anwser_logo_error);
                } else { //超时未答
                    holder.ivResultLogo.setImageResource(R.mipmap.anwser_logo_no);
                }

                for (int i = 0; i < optionsBeans.size(); i++) {
                    LookAnswerResultBean.QuestionsBean.OptionsBean optionsBean = optionsBeans.get(i);
                    String title = "";
                    //私钥解密
                    try {
                        PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                        byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(optionsBean.getTitle()), private_key);
                        title = new String(decryptByte); //选项名称
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //显示正确答案
                    if (optionsBean.getId().equals(oks)) {

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        //正确答案图片
                        if (type == 2) {
                            holder.ivRightImg.setVisibility(View.VISIBLE);
                            GlideUtils.LoadImage1(context, optionsBean.getPicture(), holder.ivRightImg);
                            lp.setMargins(ConvertUtils.dp2px(22), 0, 0, ConvertUtils.dp2px(3));
                            holder.tvRightAnswer.setLayoutParams(lp);
                        } else {
                            holder.tvRightAnswer.setText("正确答案: " + title);
                            holder.ivRightImg.setVisibility(View.GONE);
                            lp.setMargins(ConvertUtils.dp2px(22), 0, 0, ConvertUtils.dp2px(22));
                            holder.tvRightAnswer.setLayoutParams(lp);
                        }
                    }


                }
            }
        }
        /**
         * 同时不为null才可以点击
         */
        if (!EncodeAndStringTool.isObjectEmpty(mOnItemClickListener)) {
            holder.tvFeedback.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.tvFeedback, position);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return questionsBeans == null ? 0 : questionsBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_question2)
        TextView tvQuestion2;
        @BindView(R.id.tv_feedback)
        TextView tvFeedback;
        @BindView(R.id.tv_right_answer)
        TextView tvRightAnswer;
        @BindView(R.id.iv_result_logo)
        ImageView ivResultLogo;
        @BindView(R.id.iv_main_img)
        ImageView ivMainImg;
        @BindView(R.id.iv_right_img)
        ImageView ivRightImg;
        @BindView(R.id.tv_index)
        TextView tvIndex;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    GroupTreeAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitsener(GroupTreeAdapter.OnItemClickListener mOnItemClickLitsener) {
        this.mOnItemClickListener = mOnItemClickLitsener;
    }
}
