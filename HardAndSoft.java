import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
public class HardAndSoft {
    static int min_weights = Integer.MAX_VALUE; 
    static List<Integer> best_order = new ArrayList<Integer>();
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
        HashSet<String> forced_partial_assignments = new HashSet<String>();
        forbidden.add("6,6");
        forced_partial_assignments.add("1,1");
        forced_partial_assignments.add("2,2");
        forced_partial_assignments.add("3,3");
        forced_partial_assignments.add("4,4");
        forced_partial_assignments.add("5,5");
        
        List<Integer> machinesLeft = new ArrayList<Integer>();
        for(int i = 1; i < 9; i ++){
            machinesLeft.add(i);
        }
        
        String[] tooNear = {"EG3", "GH7", "FH7", "HG9", "FG8", "FA7", "GA8"};
        HashMap<String, Integer> tooNearMap = new HashMap<>();
        for(int i =0; i < tooNear.length; i++){
            String constraint = tooNear[i];
            tooNearMap.put(constraint.substring(0,2), Character.getNumericValue(constraint.charAt(2)));
            System.out.println(constraint.substring(0,2) + constraint.charAt(2));
            System.out.println(tooNearMap);
        }

       
        //softConstraints(weights, tooNearPenalties, currentOrder);

        findBestCombo(weights, machinesLeft, new ArrayList(machinesLeft), 0, new ArrayList<Integer>(), forbidden, new HashSet(forced_partial_assignments), tooNearMap);
        System.out.println("******************************************************\nBest solution found at end of function call"); 
        System.out.println(best_order);                    

        HashMap<Integer, String> task_num = new HashMap<>();
        task_num.put(1,"A");
        task_num.put(2,"B");
        task_num.put(3,"C");
        task_num.put(4,"D");
        task_num.put(5,"E");
        task_num.put(6,"F");
        task_num.put(7,"G");
        task_num.put(8,"H");


        for(int i = 0; i < best_order.size(); i++){
            System.out.println(task_num.get(best_order.get(i)));
        }
        System.out.println(min_weights);
    }


    // once the partial assignments are complete, find best weight with remaining machines and tasks 
    static void findBestCombo(int[][] weights, List<Integer> machinesLeft, List<Integer> tasksLeft,int weight_so_far, List<Integer> curr_order, HashSet<String> forbidden, HashSet<String> forced_partial_assignments, HashMap<String,Integer> tooNearMap){
        

        


        //System.out.println("function called: \t" + curr_order);
        if(weight_so_far >= min_weights){
            //System.out.println("Found less optimal solution");

            return ;
        }
        if(machinesLeft.size() == 0 && tasksLeft.size() == 0){
            //System.out.println("found solution");
            if(weight_so_far < min_weights && forced_partial_assignments.size() == 0){
                System.out.println("found solution to solve hard constraints");
                System.out.println(curr_order);
                best_order = new ArrayList(curr_order);
                min_weights = weight_so_far;
                            
            }
            return ;
        }

        if(machinesLeft.size() == 0 && tasksLeft.size() != 0){
            //System.out.println("0 machines left, but tasks left ");
            return;
        }
            

        int machine = machinesLeft.remove(0);

        // first recursively only carry 

        // try all other possible assignments 
        int size = tasksLeft.size();
        for(int i = 0; i < size; i ++){
            System.out.println(i);
            System.out.println(tasksLeft);
            
            int task = tasksLeft.get(0);

            tasksLeft.remove(0);
     
            //System.out.println("current task" + task);
            String current_assignment = "" + machine + "," + task;

            // if the current assignment is forbidden, do not perform this assignment 
            if( !forbidden.contains(current_assignment)){
                curr_order.add(task);  // add a new task
                int penaltyValue = softConstraint2(tooNearMap, curr_order);
                
                 
                boolean forced_partial_seen = false; 
                if(forced_partial_assignments.contains(current_assignment)){
                    forced_partial_seen = true; 
                    forced_partial_assignments.remove(current_assignment);
                }
                findBestCombo(weights, machinesLeft, tasksLeft, penaltyValue + weight_so_far + weights[machine-1][task-1],curr_order, forbidden, forced_partial_assignments , tooNearMap);

                if(forced_partial_seen == true){
                    forced_partial_assignments.add(current_assignment);
                }
                
                curr_order.remove(curr_order.size()-1);         // remove the item just added to current order 

            }
            tasksLeft.add(task);


        }
    }

    static int softConstraint2(HashMap<String, Integer> tooNearMap, List<Integer> curr_order){
        HashMap<Integer, String> task_num = new HashMap<>();
        task_num.put(1,"A");
        task_num.put(2,"B");
        task_num.put(3,"C");
        task_num.put(4,"D");
        task_num.put(5,"E");
        task_num.put(6,"F");
        task_num.put(7,"G");
        task_num.put(8,"H");
        if(curr_order.size() > 1 && curr_order.size() < 8){
            String curr_pair = task_num.get(curr_order.get(curr_order.size()-2)) + task_num.get(curr_order.get(curr_order.size()-1));
            //System.out.println("current_pairing " + curr_pair);
            if(tooNearMap.containsKey(curr_pair)){
                //System.out.println("found too near");
                return tooNearMap.get(curr_pair);
            }

        }
        if(curr_order.size() == 8){
            String curr_pair = task_num.get(curr_order.get(7)) + task_num.get(curr_order.get(0));
            //System.out.println("8 pairing:" + curr_pair);
            if(tooNearMap.containsKey(curr_pair)){
                //System.out.println("found too near");
                return tooNearMap.get(curr_pair);
            }

        }
        
        return 0;

    }

    static int softConstraints(int[][] machinePenalties, String[] tooNear, List<Integer> curr_order){
        int totalWeight = 0;
        String charNum = "ABCDEFGH";
        for(int i = 0; i < curr_order.size(); i++){
            int machineWeight = machinePenalties[i][(curr_order.get(i)-1)];
            totalWeight += machineWeight;
            
        }

        for(int j = 0; j < tooNear.length; j++){
            int first = charNum.indexOf((char)tooNear[j].charAt(0));
            int second = charNum.indexOf((char)tooNear[j].charAt(1));
            int tooNearPen = Integer.parseInt(tooNear[j].substring(2));
            System.out.println("tooNearPen: "+tooNearPen);
            System.out.println("Curr_order"+curr_order);
            System.out.println("first: "+first);
            System.out.println("second: "+curr_order.indexOf(second));
            if(curr_order.indexOf(first) == curr_order.indexOf(second) - 1){
                totalWeight += tooNearPen;
            }
            if(curr_order.indexOf(first) == 7 && curr_order.indexOf(second) == 0){
                totalWeight += tooNearPen;
            }
        }
        System.out.println("Soft constraint penalties: \t"+totalWeight);
        return totalWeight;

    }
}