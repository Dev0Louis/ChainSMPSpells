package dev.louis.chainsmpspells.scheduler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.ArrayList;
import java.util.Collection;

public class TaskExecutor {
    public static Collection<RepeatingTask> repeatingTasks = new ArrayList<>();
    private static Collection<RepeatingTask> timersToAdd = new ArrayList<>();
    private static Collection<RepeatingTask> timersToRemove = new ArrayList<>();

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            repeatingTasks.addAll(timersToAdd);
            timersToAdd.clear();
            for (RepeatingTask repeatingTask : timersToRemove) {
                repeatingTasks.remove(repeatingTask);
            }
            timersToRemove.clear();
            for(RepeatingTask repeatingTask : repeatingTasks) {
                repeatingTask.internalRun();
            }

        });
    }

    public static void removeTimer(RepeatingTask repeatingTask) {
        timersToRemove.add(repeatingTask);
    }

    public static void addTimer(RepeatingTask repeatingTask) {
        timersToAdd.add(repeatingTask);
    }

    public static int getTimerCount(){
        return repeatingTasks.size() + timersToAdd.size() - timersToRemove.size();
    }
}
