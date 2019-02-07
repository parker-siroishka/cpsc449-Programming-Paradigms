
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
public class A1a {
    static int min_weights = Integer.MAX_VALUE; 
    static List<Integer> best_order = new ArrayList<Integer>();
    public static void  main(String[] args) {

        int[][] weights = new int[8][8];
        int count = 1 ;
        
        
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j ++){
                weights[i][j] = count ;
                count +=1 ;
            }
        }
        
        HashSet<String> forbidden = new HashSet<String>();
        HashSet<String> forced_partial_assignments = new HashSet<String>();
        forbidden.add("1,1");
        forced_partial_assignments.add("3,4");
        
        List<Integer> machinesLeft = new ArrayList<Integer>();
        for(int i = 0; i < 8; i ++){
            machinesLeft.add(i+1);
        }
        
        findBestCombo(weights, machinesLeft, new ArrayList(machinesLeft), 0, new ArrayList<Integer>(), forbidden, new HashSet(forced_partial_assignments));
        System.out.println("******************************************************\nBest solution found at end of function call");
        System.out.println(best_order);
        
    }


    // once the partial assignments are complete, find best weight with remaining machines and tasks 
    static void findBestCombo(int[][] weights, List<Integer> machinesLeft, List<Integer> tasksLeft,int weight_so_far, List<Integer> curr_order, HashSet<String> forbidden, HashSet<String> forced_partial_assignments){
        System.out.println("function called: \t" + curr_order);
        if(weight_so_far >= min_weights){
            System.out.println("Found less optimal solution");
            return ;
        }
        if(machinesLeft.size() == 0 && tasksLeft.size() == 0){
            System.out.println("found solution");
            if(weight_so_far < min_weights && forced_partial_assignments.size() == 0){
                System.out.println("found solution to solve hard constraints");
                System.out.println(curr_order);
                best_order = new ArrayList(curr_order);
            }
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
            
            int task = tasksLeft.get(0);

            tasksLeft.remove(0);
     
            System.out.println("current task" + task);
            String current_assignment = "" + machine + "," + task;

            // if the current assignment is forbidden, do not perform this assignment 
            if( !forbidden.contains(current_assignment)){
                curr_order.add(task);  // add a new task 
                boolean forced_partial_seen = false; 
                if(forced_partial_assignments.contains(current_assignment)){
                    forced_partial_seen = true; 
                    forced_partial_assignments.remove(current_assignment);
                }
                findBestCombo(weights, machinesLeft, tasksLeft, weight_so_far + weights[machine-1][task-1],curr_order, forbidden, forced_partial_assignments );

                if(forced_partial_seen == true){
                    forced_partial_assignments.add(current_assignment);
                }

                curr_order.remove(curr_order.size()-1);         // remove the item just added to current order 

            }
            tasksLeft.add(task);


        }
    }
}