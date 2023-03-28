package dev.louis.chainsmpspells.scheduler;

public abstract class RepeatingTask implements Runnable {
    boolean cancelled = false;
    int everyNTicks;
    int ticksLived = 0;

    public RepeatingTask() {
        this.everyNTicks = 1;
        TaskExecutor.addTimer(this);
    }
    public RepeatingTask(int everyNTicks) {
        this.everyNTicks = everyNTicks;
        TaskExecutor.addTimer(this);
    }

    public void cancel(){
        cancelled = true;
        TaskExecutor.removeTimer(this);
    }

    public void internalRun() {
        if(!cancelled){

            if(ticksLived % everyNTicks == 0){
                run();
            }
            ticksLived++;
        }else {
            cancel();
        }
    }
}
