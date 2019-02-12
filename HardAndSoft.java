import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
public class HardAndSoft {
    static int min_weights = Integer.MAX_VALUE; 
    static List<Character> best_order = new ArrayList<>();
    public static void  main(String[] args) {

        int[][] weights = new int[8][8];
        int count = 1 ;
        
        List<Integer> currentOrder = new ArrayList<Integer>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j ++){
                weights[i][j] = count ;
                //count++;
            }
            // currentOrder.add(i+1);

        }
        


        // String[] tooNearPenalties = {"AD18", "BD12", "CD2"};



        HashSet<String> forbidden = new HashSet<String>();
        HashMap<Character, Character> forced_partial_assignments = new HashMap<>();
       
        forbidden.add("6,F");
        


        System.out.println("forbidden:" + forbidden);
        HashSet<String> tooNearHardSet = new HashSet<>();
       // tooNearHardSet.add("GH");
       // tooNearHardSet.add("HG");
    



        forced_partial_assignments.put('1','A');
        forced_partial_assignments.put('2', 'B');
        forced_partial_assignments.put('3','C');
        forced_partial_assignments.put('4','D');
        forced_partial_assignments.put('5','E');
        //forced_partial_assignments.put('6','F');
        //forced_partial_assignments.put('6' , 'F');

        
        List<Integer> machinesLeft = new ArrayList<Integer>();
        ArrayList<Character> curr_order = new ArrayList<>();
        for(int i = 1; i < 9; i ++){
            machinesLeft.add(i);
            curr_order.add('-');
        }
        
        String[] tooNear = {"EG3", "GH7", "FH7", "HG9", "FG8", "FA7", "GA8"};
        //String[] tooNear = {};
        HashMap<String, Integer> tooNearMap = new HashMap<>();
        for(int i =0; i < tooNear.length; i++){
            String constraint = tooNear[i];
            tooNearMap.put(constraint.substring(0,2), Character.getNumericValue(constraint.charAt(2)));
            System.out.println(constraint.substring(0,2) + constraint.charAt(2));
            System.out.println(tooNearMap);
        }

        Character[] tmpArr = new Character[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'  };
        ArrayList<Character> tasksLeft = new ArrayList<>();
        for(int i = 0; i < tmpArr.length; i ++){
            tasksLeft.add(tmpArr[i]);
        }


        //softConstraints(weights, tooNearPenalties, currentOrder);
        applyForced(weights, new ArrayList(machinesLeft), new ArrayList(tasksLeft), 0, curr_order, forbidden, forced_partial_assignments, tooNearMap, tooNearHardSet);

        //findBestCombo(weights, new ArrayList(machinesLeft), new ArrayList(machinesLeft), 0, new ArrayList<Integer>(8), forbidden, new HashSet(forced_partial_assignments), tooNearMap);
        System.out.println("******************************************************\nBest solution found at end of function call"); 
        System.out.println(best_order);                    
        if(best_order.size() == 0){
            System.out.println("no solution found");
            return;
        }
        for(int i = 0; i < machinesLeft.size(); i++){
            System.out.println(best_order.get(i));
        }
        System.out.println(min_weights);
    }


    // once the partial assignments are complete, find best weight with remaining machines and tasks 


   
    static void applyForced(int[][] weights, List<Integer> machinesLeft, ArrayList<Character> tasksLeft,int weight_so_far, List<Character> curr_order, HashSet<String> forbidden, HashMap<Character,Character> forced_partial_assignments, HashMap<String,Integer> tooNearMap, HashSet<String> tooNearHardSet){
        int size = machinesLeft.size();
        int i = 0;
        System.out.println("entered applyForced");
        System.out.println(machinesLeft);
        System.out.println(tasksLeft);
        System.out.println(curr_order);
        i = 0;
        while(i < size){
            if(forced_partial_assignments.containsKey( (char)(machinesLeft.get(i) + '0') )){
                int j = 0;
                while(tasksLeft.get(j) != forced_partial_assignments.get((char)(machinesLeft.get(i) + '0'))){
                    j++;
                    if(j == size){
                        System.out.println("\n\n\n Invalid forced Assignment, exiting");
                        return;
                    }
                }
                System.out.println("I:" + i + "j" + j);
                curr_order.set(machinesLeft.get(i)-1, tasksLeft.get(j));
                if(is_too_near_hard(tooNearHardSet, curr_order, machinesLeft.get(i))){
                    System.out.println("in applyForced we exited because too near hard constraint failed");
                    return;

                }
                int constraintVal = softConstraint2(tooNearMap, curr_order, machinesLeft.get(i));
                weight_so_far += weights[machinesLeft.get(i)][tasksLeft.get(j) - 'A'] + constraintVal;
                machinesLeft.remove(i);
                i--;
                size--;
                tasksLeft.remove(j);

            }
            i++;
        }

        System.out.println("after applying forced:");
        System.out.println(curr_order);
        System.out.println(machinesLeft);
        System.out.println(tasksLeft);
        System.out.println(weight_so_far);

        System.out.println("calling findbestCombo");
        findBestCombo(weights, new ArrayList<Integer>(machinesLeft), new ArrayList<Character>(tasksLeft), weight_so_far, new ArrayList<Character>(curr_order), forbidden, tooNearMap, tooNearHardSet);

    }



    static void findBestCombo(int[][] weights, List<Integer> machinesLeft, List<Character> tasksLeft,int weight_so_far, List<Character> curr_order, HashSet<String> forbidden,  HashMap<String,Integer> tooNearMap, HashSet<String> tooNearHardSet){
        System.out.println("\n\n\nEntered findBestCombo");
        System.out.println("machinesLeft" + machinesLeft + "tasksLeft" + tasksLeft);
        //System.out.println("function called: \t" + curr_order);
        if(weight_so_far >= min_weights){
            //System.out.println("Found less optimal solution");

            return ;
        }
        if(machinesLeft.size() == 0 && tasksLeft.size() == 0){
            //System.out.println("found solution");

            System.out.println("found new optimal solution" + weight_so_far);
            System.out.println(curr_order );
            best_order = new ArrayList(curr_order);
            min_weights = weight_so_far;
            return ;
        }

        if(machinesLeft.size() == 0 && tasksLeft.size() != 0){
            System.out.println("0 machines left, but tasks left ");
            return;
        }
            

        int machine = machinesLeft.remove(0);

        // first recursively only carry 

        // try all other possible assignments 
        int size = tasksLeft.size();
        for(int i = 0; i < size; i ++){
            System.out.println(i);
            System.out.println(tasksLeft);
            
            Character task = tasksLeft.get(0);

            tasksLeft.remove(0);
     
            //System.out.println("current task" + task);
            String current_assignment = "" + machine + "," + task;
            System.out.println("current assignment: "  + current_assignment);
            if(forbidden.contains(current_assignment)){
                System.out.println("\n\n\nforbidden" + current_assignment);
            }
            // if the current assignment is forbidden, do not perform this assignment 
            if( !forbidden.contains(current_assignment) ){
                curr_order.set(machine -1, task);  // add a new task
                boolean isTooNear  = is_too_near_hard(tooNearHardSet, curr_order, machine);
                if(!isTooNear){
                    System.out.println("not  and not too near");
                    int penaltyValue = softConstraint2(tooNearMap, curr_order, machine);
                    if(penaltyValue != 0){
                        System.out.println("machine:\t" + machine + "task:\t" + task + "penaltyValue:\t" + penaltyValue);
                        System.out.println(curr_order);

                    }

                    findBestCombo(weights, new ArrayList<Integer>(machinesLeft), tasksLeft, penaltyValue + weight_so_far + weights[machine-1][task- 'A'],new ArrayList<Character>(curr_order), forbidden, tooNearMap, tooNearHardSet);
                    
                }
             
            }
            tasksLeft.add(task);


        }
    }

    static int softConstraint2(HashMap<String, Integer> tooNearMap, List<Character> curr_order, int machine){
        HashMap<Integer, String> task_num = new HashMap<>();
        task_num.put(1,"A");
        task_num.put(2,"B");
        task_num.put(3,"C");
        task_num.put(4,"D");
        task_num.put(5,"E");
        task_num.put(6,"F");
        task_num.put(7,"G");
        task_num.put(8,"H");
        task_num.put(-1,"Z");
        String curr_pair1;
        String curr_pair2;
        if(machine == 1){
            //curr_pair1 = task_num.get(curr_order.get(0)) + task_num.get(curr_order.get(1));
            curr_pair1 = "" + curr_order.get(0) + curr_order.get(1);
            //curr_pair2 = task_num.get(curr_order.get(7)) + task_num.get(curr_order.get(0));
            curr_pair2 = "" + curr_order.get(7) + curr_order.get(0);

        }
        else{
            //curr_pair1 = task_num.get(curr_order.get(machine -2)) + task_num.get(curr_order.get(machine -1)); // 7, 8
            curr_pair1 = "" + curr_order.get(machine-2) + curr_order.get(machine-1);
            //curr_pair2 = task_num.get(curr_order.get(machine -1)) + task_num.get(curr_order.get(machine % 8));
            curr_pair2 = "" + curr_order.get(machine-1) + curr_order.get(machine%8);
        }

        int sum = 0;
        if(tooNearMap.containsKey(curr_pair1)){
            //System.out.println("found too near");

            sum += tooNearMap.get(curr_pair1);
        }
        if(tooNearMap.containsKey(curr_pair2)){
            sum += tooNearMap.get(curr_pair2);
        }
        
        return sum;

    }


      static boolean is_too_near_hard(HashSet<String> tooNearHardSet, List<Character> curr_order, int machine){
        String curr_pair1;
        String curr_pair2;
        System.out.println("entered toonear_hard");
        if(machine == 1){
            //curr_pair1 = task_num.get(curr_order.get(0)) + task_num.get(curr_order.get(1));
            curr_pair1 = "" + curr_order.get(0) + curr_order.get(1);
            //curr_pair2 = task_num.get(curr_order.get(7)) + task_num.get(curr_order.get(0));
            curr_pair2 = "" + curr_order.get(7) + curr_order.get(0);

        }
        else{
            //curr_pair1 = task_num.get(curr_order.get(machine -2)) + task_num.get(curr_order.get(machine -1)); // 7, 8
            curr_pair1 = "" + curr_order.get(machine-2) + curr_order.get(machine -1);
            //curr_pair2 = task_num.get(curr_order.get(machine -1)) + task_num.get(curr_order.get(machine % 8));
            curr_pair2 = "" +  curr_order.get(machine-1) + curr_order.get(machine % 8 );
       
        }
        System.out.println("curr_pairs" + curr_pair1 + curr_pair2);
        if(tooNearHardSet.contains(curr_pair1)){
            //System.out.println("found too near");
            System.out.println("exiting toonear_hard");
            return true;
        }
        if(tooNearHardSet.contains(curr_pair2)){
            System.out.println("exiting toonear_hard");
            return true;
        }
        
        return false;


    }

}