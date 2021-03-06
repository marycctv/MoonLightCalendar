package com.example.administrator.moonlightcalendar.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.moonlightcalendar.MainApplication;
import com.example.administrator.moonlightcalendar.Util.myUtil.DateUtil;
import com.example.administrator.moonlightcalendar.Util.myUtil.MoonLightDBUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15 0015.
 * 用于管理用户账单的类，包含app，cycleProject以及固定花费初始资金等
 */

public class Person {

    private static SharedPreferences mPreferences = null;
    private static Person mPerson;

    private float payEachDay = 0;//每日固定花费
    private float originWealth = 0;//初始资金
    private Date firstDate;//初始日期
    private List<CycleProject> cycleProjects = new ArrayList<>();
    private List<App> apps = new ArrayList<>();

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public List<CycleProject> getCycleProjects() {
        return cycleProjects;
    }

    public void setCycleProjects(List<CycleProject> cycleProjects) {
        this.cycleProjects = cycleProjects;
    }

    public float getOriginWealth() {
        return originWealth;
    }

    public void setOriginWealth(float originWealth) {
        this.originWealth = originWealth;
        mPreferences.edit().putFloat("originWealth", originWealth).apply();
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
        mPreferences.edit().putLong("firstDate", firstDate.getTime()).apply();
    }

    public float getPayEachDay() {
        return payEachDay;
    }

    public void setPayEachDay(float payEachDay) {
        this.payEachDay = payEachDay;
        mPreferences.edit().putFloat("payEachDay", payEachDay).apply();
    }

    private Person() {

    }

    public static Person getInstance() {
        if (mPreferences == null) {
            mPreferences = MainApplication.getContext().getSharedPreferences("person", Context.MODE_PRIVATE);
            MoonLightDBUtil.init(MainApplication.getContext());
//            MoonLightDBUtil.clear();
        }
        if (mPerson == null) {
            mPerson = new Person();
            mPerson.init();
            DataSource.init();
        }
        return mPerson;
    }

    private void init() {
        long fd = mPreferences.getLong("firstDate", System.currentTimeMillis());
        firstDate = new java.sql.Date(fd);
        payEachDay = mPreferences.getFloat("payEachDay", 0);
        originWealth = mPreferences.getFloat("originWealth", 0);
        apps.addAll(MoonLightDBUtil.queryApp(null, null));
        cycleProjects.addAll(MoonLightDBUtil.queryCycleProject(null, null));
//        MoonLightDBUtil.clear();
    }

    public void createCycleProject(String name, float price, int day, boolean out) {
        for (CycleProject project : cycleProjects) {
            if (project.getName().equals(name)) {
                return;
            }
        }
        CycleProject cycleProject = new CycleProject();
        cycleProject.day = day;
        cycleProject.name = name;
        cycleProject.price = price;
        cycleProject.out = out;
        cycleProjects.add(cycleProject);
        cycleProject.save();
        //刷新数据源
        DataSource.getInstance().refreshFinance();
    }

    //生活花销
    public void createLifeCost(boolean out, float price, String name, Date date) {
        Bill bill = new Bill();
        bill.out = out;
        bill.pID = -1;
        bill.price = price;
        bill.from = name;
        bill.fromApp = "今日收支";
        bill.date = DateUtil.date2String(date);
        bill.type = Bill.TYPE_SPEND;
        bill.time = date.getTime();
        bill.save();
        //刷新数据源
        DataSource.getInstance().refreshFinance();
    }

    public App createNewApp(String name, int createBillDay, int payBillDay) {
        App app = new App(name, createBillDay, payBillDay);
        apps.add(app);
        app.save();
        return app;
    }

    public static class CycleProject {
        private int id;
        private String name;
        private float price;
        private int day;
        private boolean out;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public void setOut(boolean out) {
            this.out = out;
        }

        public boolean isOut() {
            return out;
        }

        public String getName() {
            return name;
        }

        public float getPrice() {
            return price;
        }

        public int getDay() {
            return day;
        }

        public void save() {
            MoonLightDBUtil.insert(this);
        }
    }
}
