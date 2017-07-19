package Services;

import android.content.Context;

import com.ase.Bean.TaskDetailsBean;

/**
 * Created by Amuthan on 04/01/2017.
 */

public interface ShowOrCancelProgress {

    public void ShowProgress(Context context);
    public void CancellProgress();
    public void StartAlarmManager(TaskDetailsBean detailsBean);
}
