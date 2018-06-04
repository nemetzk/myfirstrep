package meas_data_logger_v10;

public class timer {
	static int timer_cnt = 0;
	int curr_timer_idx = 0;
	//static int[] timer_cvs;
	static final int TIMER_MAX_DEFINED_IDX = 20;
	static  int[] timer_cvs = new int[TIMER_MAX_DEFINED_IDX];
	static int timer_max_idx=0;

	static final int TIMER_TIME_BASE_MS=10;
	
	timer (){	
		curr_timer_idx=timer_max_idx;
		if (curr_timer_idx==0){
			//timer_cvs = new int[] {0,0,0,0,0,0,0,0,0,0};
			//for (int i=0;i<TIMER_MAX_DEFINED_IDX+1;i++)
			//timer_cvs[i]=0;
			Thread timer_thread = new Thread() {
				@Override
				public void run() {
					while(true){
						timerOperation();
						try {
							Thread.sleep(TIMER_TIME_BASE_MS);
						} catch (InterruptedException e) {
								e.printStackTrace();
						} //catch
					} //while
				}//run
			};//thread
			timer_thread.start();
		}//if

		timer_cvs[curr_timer_idx]=0;

		timer_max_idx++;		
	}
	
	void setTimer(int timer_set_value){
		timer_cvs[curr_timer_idx]=timer_set_value;
	}
	
	boolean timerElapsed(){
		boolean timer_completed_flg = false;

		if (timer_cvs[curr_timer_idx]==0)
			timer_completed_flg=true;
		else
			timer_completed_flg=false;
		
		return timer_completed_flg;
	}
	
	int seeTimer(){
		return timer_cvs[curr_timer_idx];
	}
	
	static void timerOperation(){
		for (int timer_idx=0;timer_idx<=timer_max_idx;timer_idx++){
			if (timer_cvs[timer_idx]>0)
				timer_cvs[timer_idx]--;
		}
		
	}
}
