package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.LookAnswerResultBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.Base64Utils;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.RSAUtils;

import java.security.PrivateKey;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunxiao on 2018/4/25.
 * 奖品适配器
 */

public class AnswerHistoryAdapter extends RecyclerView.Adapter<AnswerHistoryAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    public AnswerHistoryAdapter() {
    }

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


                if (questionsBean.getResult() == 1) {
                    holder.ivIsCorrect.setImageResource(R.mipmap.duihuao);
                } else {
                    //回答错误
                    holder.ivIsCorrect.setImageResource(R.mipmap.cuohao);
                }

                for (int i = 0; i < optionsBeans.size(); i++) {

                    String title = "";
                    //私钥解密
                    try {
                        PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                        byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(optionsBeans.get(i).getTitle()), private_key);
                        title = new String(decryptByte); //选项名称
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (!EncodeAndStringTool.isStringEmpty(oks)) {
                        if (oks.equals(optionsBeans.get(i).getId())) {
                            holder.tvRightAnswer1.setText("正确答案:" + title);//正确答案
                        }
                    }

                    if (!EncodeAndStringTool.isStringEmpty(questionsBean.getAnswer())) {
                        if (questionsBean.getAnswer().equals(optionsBeans.get(i).getId())) {
                            if (questionsBean.getAnswer().equals(oks)) {
                                holder.tvYourAnswer.setText("您的答案:" + title);//回答正确
                                holder.tvYourAnswer.setTextColor(context.getResources().getColor(R.color.color_4));
                            } else {
                                holder.tvYourAnswer.setText("您的答案:" + title);//回答错误
                                holder.tvYourAnswer.setTextColor(context.getResources().getColor(R.color.color_20));
                            }
                        } else if (questionsBean.getAnswer().equals("0")) {
                            holder.tvYourAnswer.setText("您的答案:超时未答或中途异常");
                            holder.tvYourAnswer.setTextColor(context.getResources().getColor(R.color.color_20));
                        } else if (questionsBean.getResult() == -1) {
                            holder.tvYourAnswer.setText("您的答案:超时未答或中途异常");
                            holder.tvYourAnswer.setTextColor(context.getResources().getColor(R.color.color_20));
                        }
                    } else {
                        holder.tvYourAnswer.setText("您的答案:超时未答或中途异常");
                        holder.tvYourAnswer.setTextColor(context.getResources().getColor(R.color.color_20));
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
        @BindView(R.id.tv_right_answer1)
        TextView tvRightAnswer1;
        @BindView(R.id.tv_your_answer)
        TextView tvYourAnswer;
        @BindView(R.id.iv_is_correct)
        ImageView ivIsCorrect;

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
