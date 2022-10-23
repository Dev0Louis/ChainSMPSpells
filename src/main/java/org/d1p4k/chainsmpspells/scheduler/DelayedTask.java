package org.d1p4k.chainsmpspells.scheduler;

public abstract class DelayedTask extends RepeatingTask {

    int lastingTicks;

    public DelayedTask(int lastingTicks) {
        this.lastingTicks = lastingTicks;
    }
    @Override
    public void internalRun() {
        if(cancelled){
            cancel();
        }else {
            if(ticksLived >= lastingTicks){
                run();
                cancel();
            }
            ticksLived++;
        }
    }
}
