package com.example.fragmentnote.vo;

import android.graphics.Bitmap;
import android.nfc.Tag;

import java.util.List;

public class ArticleDto {
    //主键id
    private Integer id;
    //文章标题
    private String title;
    //文章内容
    private String contentMd;
    //文章封面地址
    private String avatar;
    //是否置顶 0否 1是
    private Integer isStick;
    //时间
    private String createTime;
    //类别
    private String categoryName;
    //类别
    private String categoryId;
    //标签名集合
    private List<Tag> tagVOList;
    private Bitmap bitmap;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentMd() {
        return contentMd;
    }

    public void setContentMd(String contentMd) {
        this.contentMd = contentMd;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getIsStick() {
        return isStick;
    }

    public void setIsStick(Integer isStick) {
        this.isStick = isStick;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<Tag> getTagVOList() {
        return tagVOList;
    }

    public void setTagVOList(List<Tag> tagVOList) {
        this.tagVOList = tagVOList;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
