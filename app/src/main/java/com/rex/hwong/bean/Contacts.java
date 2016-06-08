package com.rex.hwong.bean;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/6/8
 * @time 上午9:40
 */

/**
 * 联系人
 */
public class Contacts implements Comparable<Contacts>{

    private int _id;
    /** 姓名 */
    private String name;
    /** 号码 */
    private String number;
    /** 姓名的拼音 */
    private String pinyinName = "#";

    public Contacts() {
    }

    public Contacts(int _id, String name, String number, String pinyinName) {
        this._id = _id;
        this.name = name;
        this.number = number;
        this.pinyinName = pinyinName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", pinyinName='" + pinyinName + '\'' +
                '}';
    }

    @Override
    public int compareTo(Contacts another) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (another.getPinyinName().equals("#")) {
            return 1;
        } else if (this.getPinyinName().equals("#")) {
            return -1;
        } else {
            return this.getPinyinName().compareTo(another.getPinyinName());
        }
    }
}
