package com.xtagwgj.base.view.stepview;

/**
 * Created by xtagwgj on 2017/12/19.
 */

public class StepBean {
    //未完成  undo step
    public static final int STEP_UNDO = -1;
    //正在进行 current step
    public static final int STEP_CURRENT = 0;
    //已完成 completed step
    public static final int STEP_COMPLETED = 1;

    private String name;
    private int state;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public StepBean()
    {
    }

    public StepBean(String name, int state)
    {
        this.name = name;
        this.state = state;
    }
}
