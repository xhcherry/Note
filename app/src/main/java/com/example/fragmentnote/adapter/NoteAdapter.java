package com.example.fragmentnote.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragmentnote.R;
import com.example.fragmentnote.listner.HttpCallback;
import com.example.fragmentnote.util.HttpUtils;
import com.example.fragmentnote.vo.ArticleDto;

import java.io.InputStream;
import java.util.List;

import androidx.annotation.NonNull;

public class NoteAdapter extends BaseAdapter {
    private List<ArticleDto> news;
    private Context mContext;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            notifyDataSetChanged();
            return false;
        }
    });


    public NoteAdapter(List<ArticleDto> news, Context mContext) {
        this.news = news;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int i) {
        return news.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view;
        ViewHolder holder;
        if(convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.note_list_item,viewGroup,false);
            holder = new ViewHolder();
            holder.title = view.findViewById(R.id.note_title);
            holder.image = view.findViewById(R.id.note_image);
            holder.passtime = view.findViewById(R.id.note_createtime);
            holder.readNum = view.findViewById(R.id.note_read_num);
            holder.goodValueNum = view.findViewById(R.id.note_goodvalue_num);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        //1找到需要显示的那一条数据
        ArticleDto noteDetail = news.get(i);
        //2找到这条数据需要显示的样式（布局文件）
        if(noteDetail.getBitmap()==null){
            HttpUtils.requestApi(noteDetail.getAvatar(), "GET", null,1, new HttpCallback() {
                @Override
                public void onSuccess(InputStream in) {
                    news.get(i).setBitmap(BitmapFactory.decodeStream(in));
                    handler.sendMessage(new Message());
                }

                @Override
                public void onFailure(InputStream error) {

                }

            });
        }else{
            holder.image.setImageBitmap(noteDetail.getBitmap());
        }
        holder.title.setText(noteDetail.getTitle());
        if(noteDetail.getCreateTime()!=null){
            holder.passtime.setText(noteDetail.getCreateTime());
        }
        //3将数据的值填充到布局文件的对应的控件中
        return view;
    }
    public class ViewHolder{
        TextView title;
        ImageView image;
        TextView passtime;
        TextView readNum;
        TextView goodValueNum;
    }
}
