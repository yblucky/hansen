package hansen.tradecurrency.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BaseScheduleTask
{
  protected final Log logger = LogFactory.getLog(getClass());

  protected boolean devmode = true;

  public void doTask() {
    if (this.devmode)
    	doScheduleTask();
  }

  protected abstract void doScheduleTask();
}
