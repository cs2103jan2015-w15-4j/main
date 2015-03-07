package planner;

public class SplitLogic {
    
    public static void splitByTentative(TaskList input, TaskList confirmed, TaskList tentative){
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).isFloating()) {
                tentative.add(input.get(i));
            }
            else {
                confirmed.add(input.get(i));
            }
        }
    }
    
    public static void splitByDone(TaskList input, TaskList done, TaskList notDone) {
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).isDone()) {
                done.add(input.get(i));
            }
            else {
                notDone.add(input.get(i));
            }
        }
    }
}
