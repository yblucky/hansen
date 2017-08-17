package com.base.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BaseScheduleTask {
    protected final Log logger = LogFactory.getLog(getClass());

    public void doTask() {
        doScheduleTask();
    }

    protected abstract void doScheduleTask();
}
